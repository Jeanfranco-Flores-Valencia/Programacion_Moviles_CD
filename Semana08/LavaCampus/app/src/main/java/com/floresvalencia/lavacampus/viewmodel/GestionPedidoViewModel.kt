package com.floresvalencia.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floresvalencia.lavacampus.data.local.relation.PedidoConDetalle
import com.floresvalencia.lavacampus.data.repository.LavaRepository
import com.floresvalencia.lavacampus.model.Casilleros
import com.floresvalencia.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Gestion de un pedido por el administrador. Maquina de estados con condiciones:
 * - Solo se avanza al estado siguiente (EstadoPedido.siguiente()).
 * - Para pasar a LISTO se exige un casillero elegido y libre.
 * - Para pasar a ENTREGADO se exige pagado = true.
 * - Solo se cancela desde RECIBIDO o EN_PROCESO.
 */
class GestionPedidoViewModel(
    private val repo: LavaRepository,
    private val pedidoId: Int
) : ViewModel() {

    val pedido: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /** Casilleros usados por OTROS pedidos LISTO (el del propio pedido no cuenta como ocupado). */
    val casillerosOcupados: StateFlow<Set<String>> = combine(
        repo.observarCasillerosOcupados(), pedido
    ) { ocupados, p ->
        ocupados.filter { it != p?.pedido?.casillero }.toSet()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    private val _elegido = MutableStateFlow<String?>(null)

    /** Casillero elegido; si no se eligio ninguno, el que ya tiene el pedido. */
    val casilleroElegido: StateFlow<String?> = combine(_elegido, pedido) { elegido, p ->
        elegido ?: p?.pedido?.casillero
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun elegirCasillero(c: String) {
        if (c in casillerosOcupados.value) {
            _mensaje.value = "El casillero $c ya está ocupado"
            return
        }
        _elegido.value = c
        _mensaje.value = null
    }

    fun marcarPagado(b: Boolean) {
        viewModelScope.launch { repo.marcarPagado(pedidoId, b) }
    }

    fun avanzar() {
        val p = pedido.value?.pedido ?: return
        val actual = EstadoPedido.desde(p.estado)
        val siguiente = actual.siguiente()
        if (siguiente == null) {
            _mensaje.value = "El pedido ya está ${actual.etiqueta.lowercase()}"
            return
        }
        when (siguiente) {
            EstadoPedido.LISTO -> {
                val casillero = casilleroElegido.value
                if (!Casilleros.esValido(casillero)) {
                    _mensaje.value = "Elige un casillero libre para pasar a Listo"
                    return
                }
                if (casillero in casillerosOcupados.value) {
                    _mensaje.value = "El casillero $casillero ya está ocupado"
                    return
                }
                guardar(EstadoPedido.LISTO, casillero)
            }

            EstadoPedido.ENTREGADO -> {
                if (!p.pagado) {
                    _mensaje.value = "No se entrega un pedido sin cobrar. Marca 'Cobrado' primero"
                    return
                }
                guardar(EstadoPedido.ENTREGADO, p.casillero)
            }

            else -> guardar(siguiente, p.casillero)
        }
    }

    fun cancelar() {
        val p = pedido.value?.pedido ?: return
        if (!puedeCancelar(p.estado)) {
            _mensaje.value = "Solo se cancelan pedidos Recibidos o En proceso"
            return
        }
        guardar(EstadoPedido.CANCELADO, null)
    }

    private fun guardar(estado: EstadoPedido, casillero: String?) {
        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, estado, casillero)
            _mensaje.value = "Pedido marcado como ${estado.etiqueta}"
        }
    }

    companion object {
        /** El administrador cancela RECIBIDO o EN_PROCESO, nunca LISTO ni ENTREGADO. */
        fun puedeCancelar(estado: String): Boolean =
            estado == EstadoPedido.RECIBIDO.name || estado == EstadoPedido.EN_PROCESO.name
    }
}

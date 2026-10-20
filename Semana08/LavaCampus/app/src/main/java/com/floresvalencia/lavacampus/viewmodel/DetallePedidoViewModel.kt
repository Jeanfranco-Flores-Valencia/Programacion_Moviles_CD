package com.floresvalencia.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floresvalencia.lavacampus.data.local.relation.PedidoConDetalle
import com.floresvalencia.lavacampus.data.repository.LavaRepository
import com.floresvalencia.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Detalle del pedido del estudiante. Como observa un Flow de Room,
 * si el administrador cambia el estado la pantalla se actualiza sola.
 */
class DetallePedidoViewModel(
    private val repo: LavaRepository,
    private val pedidoId: Int
) : ViewModel() {

    val pedido: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Regla: el estudiante solo cancela si el pedido esta en RECIBIDO (aun no empezo el lavado). */
    val puedeCancelar: StateFlow<Boolean> = pedido
        .map { it != null && puedeCancelarEstudiante(it.pedido.estado) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun cancelar() {
        val actual = pedido.value ?: return
        if (!puedeCancelarEstudiante(actual.pedido.estado)) return
        viewModelScope.launch {
            repo.cambiarEstado(actual.pedido.id, EstadoPedido.CANCELADO, null)
        }
    }

    companion object {
        fun puedeCancelarEstudiante(estado: String): Boolean = estado == EstadoPedido.RECIBIDO.name
    }
}

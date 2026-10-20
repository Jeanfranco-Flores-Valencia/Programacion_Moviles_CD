package com.floresvalencia.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floresvalencia.lavacampus.data.local.entity.Servicio
import com.floresvalencia.lavacampus.data.local.relation.PedidoConDetalle
import com.floresvalencia.lavacampus.data.repository.LavaRepository
import com.floresvalencia.lavacampus.model.EstadoPedido
import com.floresvalencia.lavacampus.model.Unidad
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Contadores del estudiante (se muestran en su Perfil). */
data class EstadisticasEstudiante(
    val activos: Int = 0,
    val totales: Int = 0,
    val kilos: Double = 0.0
)

/** Pedidos del estudiante separados en Activos e Historial, y tarifas vigentes. */
class MisPedidosViewModel(
    private val repo: LavaRepository,
    usuarioId: Int
) : ViewModel() {

    private val misPedidos = repo.observarPedidosDeUsuario(usuarioId)

    /** Activos = RECIBIDO, EN_PROCESO y LISTO. */
    val activos: StateFlow<List<PedidoConDetalle>> = misPedidos
        .map { lista -> lista.filter { EstadoPedido.desde(it.pedido.estado).esActivo } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Historial = ENTREGADO y CANCELADO. */
    val historial: StateFlow<List<PedidoConDetalle>> = misPedidos
        .map { lista -> lista.filter { EstadoPedido.desde(it.pedido.estado).esFinal } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Tarifas: solo servicios ACTIVOS (un servicio desactivado desaparece de aqui). */
    val tarifas: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Pedidos activos, pedidos totales (sin cancelados) y kilos lavados. */
    val estadisticas: StateFlow<EstadisticasEstudiante> = misPedidos
        .map { lista ->
            val validos = lista.filter { it.pedido.estado != EstadoPedido.CANCELADO.name }
            EstadisticasEstudiante(
                activos = validos.count { EstadoPedido.desde(it.pedido.estado).esActivo },
                totales = validos.size,
                kilos = validos.sumOf { p ->
                    p.detalles.filter { Unidad.desde(it.servicio.unidad) == Unidad.KG }
                        .sumOf { it.detalle.cantidad }
                }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EstadisticasEstudiante())

    /** Misma regla del detalle: solo se cancela en RECIBIDO. */
    fun puedeCancelar(p: PedidoConDetalle): Boolean =
        DetallePedidoViewModel.puedeCancelarEstudiante(p.pedido.estado)

    fun cancelar(p: PedidoConDetalle) {
        if (!puedeCancelar(p)) return
        viewModelScope.launch { repo.cambiarEstado(p.pedido.id, EstadoPedido.CANCELADO, null) }
    }
}

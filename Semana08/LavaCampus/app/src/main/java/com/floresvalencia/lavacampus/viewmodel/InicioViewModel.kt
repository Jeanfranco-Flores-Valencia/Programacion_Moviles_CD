package com.floresvalencia.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floresvalencia.lavacampus.data.local.entity.Usuario
import com.floresvalencia.lavacampus.data.local.relation.PedidoConDetalle
import com.floresvalencia.lavacampus.data.repository.LavaRepository
import com.floresvalencia.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/** Inicio del estudiante: su pedido activo con seguimiento y sus ultimos pedidos. */
class InicioViewModel(
    repo: LavaRepository,
    val estudiante: Usuario
) : ViewModel() {

    // Visibilidad: el estudiante solo ve SUS pedidos (ordenados del mas reciente al mas antiguo)
    private val misPedidos = repo.observarPedidosDeUsuario(estudiante.id)

    /** El mas reciente en estado RECIBIDO, EN_PROCESO o LISTO. */
    val pedidoActivo: StateFlow<PedidoConDetalle?> = misPedidos
        .map { lista -> lista.firstOrNull { EstadoPedido.valueOf(it.pedido.estado).esActivo } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Los 2 ultimos pedidos. */
    val recientes: StateFlow<List<PedidoConDetalle>> = misPedidos
        .map { it.take(2) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

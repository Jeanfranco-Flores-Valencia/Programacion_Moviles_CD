package com.floresvalencia.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floresvalencia.lavacampus.data.local.relation.PedidoConDetalle
import com.floresvalencia.lavacampus.data.repository.LavaRepository
import com.floresvalencia.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/** Chips de filtro de la bandeja. */
enum class FiltroBandeja(val etiqueta: String, val estado: EstadoPedido?) {
    TODOS("Todos", null),
    RECIBIDO("Recibido", EstadoPedido.RECIBIDO),
    EN_PROCESO("En proceso", EstadoPedido.EN_PROCESO),
    LISTO("Listo", EstadoPedido.LISTO)
}

/** Bandeja del administrador: ve TODOS los pedidos, filtrados por chip y buscador. */
class BandejaViewModel(
    private val repo: LavaRepository,
    busquedaInicial: String = ""   // Mejora IA: llega desde Clientes con el nombre del estudiante
) : ViewModel() {

    private val _filtro = MutableStateFlow(FiltroBandeja.TODOS)
    val filtro: StateFlow<FiltroBandeja> = _filtro.asStateFlow()

    private val _busqueda = MutableStateFlow(busquedaInicial)
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    /** Pedidos filtrados por el chip elegido y por el texto (nombre del estudiante o numero). */
    val pedidos: StateFlow<List<PedidoConDetalle>> = combine(
        repo.observarPedidos(), _filtro, _busqueda
    ) { lista, filtro, texto ->
        val q = texto.trim()
        lista
            .filter { filtro.estado == null || it.pedido.estado == filtro.estado.name }
            .filter { coincide(it, q) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Mejora IA: cantidad de pedidos por chip (se muestra en cada filtro). */
    val conteos: StateFlow<Map<FiltroBandeja, Int>> = repo.observarPedidos()
        .map { lista ->
            FiltroBandeja.entries.associateWith { f ->
                if (f.estado == null) lista.size else lista.count { it.pedido.estado == f.estado.name }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    /** Mejora IA: ids de los pedidos cuya fecha de entrega ya paso y no estan entregados ni cancelados. */
    val atrasados: StateFlow<Set<Int>> = repo.observarPedidosAtrasados(LocalDate.now().toString())
        .map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private fun coincide(p: PedidoConDetalle, q: String): Boolean {
        if (q.isEmpty()) return true
        if (p.usuario.nombre.contains(q, ignoreCase = true)) return true
        val numero = q.removePrefix("#").trimStart('0')
        return numero.isNotEmpty() && numero.all { it.isDigit() } && p.pedido.id.toString().contains(numero)
    }

    fun elegirFiltro(f: FiltroBandeja) {
        _filtro.value = f
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    /** Accion rapida "Iniciar": RECIBIDO -> EN_PROCESO (un solo paso). */
    fun iniciar(p: PedidoConDetalle) {
        if (p.pedido.estado != EstadoPedido.RECIBIDO.name) return
        viewModelScope.launch {
            repo.cambiarEstado(p.pedido.id, EstadoPedido.EN_PROCESO, p.pedido.casillero)
            _mensaje.value = "Pedido #${p.pedido.id} en proceso"
        }
    }

    /** Accion rapida "Entregar": solo si el pedido esta LISTO y pagado. */
    fun entregar(p: PedidoConDetalle) {
        if (p.pedido.estado != EstadoPedido.LISTO.name) return
        if (!p.pedido.pagado) {
            _mensaje.value = "No se entrega un pedido sin cobrar. Ábrelo y marca 'Cobrado'"
            return
        }
        viewModelScope.launch {
            repo.cambiarEstado(p.pedido.id, EstadoPedido.ENTREGADO, p.pedido.casillero)
            _mensaje.value = "Pedido #${p.pedido.id} entregado"
        }
    }
}

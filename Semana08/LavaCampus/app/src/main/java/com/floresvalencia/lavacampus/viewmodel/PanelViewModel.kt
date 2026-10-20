package com.floresvalencia.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floresvalencia.lavacampus.data.repository.LavaRepository
import com.floresvalencia.lavacampus.model.EstadoPedido
import com.floresvalencia.lavacampus.ui.components.graficos.DatoGrafico
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

/** Panel del administrador: contadores, ingresos de la semana y datos para los graficos. */
class PanelViewModel(repo: LavaRepository) : ViewModel() {

    val hoy: LocalDate = LocalDate.now()

    /** Lunes de esta semana (si hoy es lunes, es hoy). */
    val inicioSemana: String = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()

    /** Pedidos de hoy (sin cancelados). */
    val pedidosHoy: StateFlow<Int> = repo.observarPedidosDeFecha(hoy.toString())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val enProceso: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.EN_PROCESO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Pedidos LISTO esperando recojo. */
    val porRecoger: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.LISTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Suma del total de los pedidos pagados desde el lunes. */
    val ingresosSemana: StateFlow<Double> = repo.observarIngresosDesde(inicioSemana)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /** Dona: cantidad de pedidos por estado (activos y entregados), con el color de cada estado. */
    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        repo.observarCantidadPorEstado(EstadoPedido.RECIBIDO),
        repo.observarCantidadPorEstado(EstadoPedido.EN_PROCESO),
        repo.observarCantidadPorEstado(EstadoPedido.LISTO),
        repo.observarCantidadPorEstado(EstadoPedido.ENTREGADO)
    ) { recibido, proceso, listo, entregado ->
        listOf(
            DatoGrafico(EstadoPedido.RECIBIDO.etiqueta, recibido.toFloat(), EstadoPedido.RECIBIDO.color),
            DatoGrafico(EstadoPedido.EN_PROCESO.etiqueta, proceso.toFloat(), EstadoPedido.EN_PROCESO.color),
            DatoGrafico(EstadoPedido.LISTO.etiqueta, listo.toFloat(), EstadoPedido.LISTO.color),
            DatoGrafico(EstadoPedido.ENTREGADO.etiqueta, entregado.toFloat(), EstadoPedido.ENTREGADO.color)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Barras: ingresos por servicio de esta semana (convierte IngresoServicio en DatoGrafico). */
    val porServicio: StateFlow<List<DatoGrafico>> = repo.observarIngresosPorServicio(inicioSemana)
        .map { lista -> lista.map { DatoGrafico(etiquetaCorta(it.nombre), it.total.toFloat()) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Mejora IA: cantidad de pedidos atrasados. */
    val atrasados: StateFlow<Int> = repo.observarPedidosAtrasados(hoy.toString())
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** "Lavado y secado" -> "Lavado", "Lavado en seco" -> "Seco" para que quepa bajo la barra. */
    private fun etiquetaCorta(nombre: String): String = when {
        nombre.contains("seco", ignoreCase = true) && nombre.contains("en ", ignoreCase = true) -> "Seco"
        else -> nombre.substringBefore(" ")
    }
}

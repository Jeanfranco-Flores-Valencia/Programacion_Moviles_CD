package com.floresvalencia.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floresvalencia.lavacampus.data.local.entity.Servicio
import com.floresvalencia.lavacampus.data.prefs.PreferenciasStore
import com.floresvalencia.lavacampus.data.repository.LavaRepository
import com.floresvalencia.lavacampus.model.LineaPedido
import com.floresvalencia.lavacampus.model.Unidad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

/** Una linea del resumen: servicio elegido, cantidad y subtotal. */
data class LineaResumen(
    val servicio: Servicio,
    val cantidad: Double,
    val subtotal: Double
)

/**
 * Nuevo pedido del estudiante. Aqui viven las reglas:
 * cantidades por unidad, total, fecha de entrega (sin domingos) y limite de 3 pedidos activos.
 */
class NuevoPedidoViewModel(
    private val repo: LavaRepository,
    prefs: PreferenciasStore,
    private val usuarioId: Int,
    private val repetirId: Int = 0   // Mejora IA: id del pedido a repetir (0 = nuevo)
) : ViewModel() {

    /** Solo servicios ACTIVOS. */
    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /** Cantidad elegida por servicio (servicioId -> cantidad). 0 = apagado. */
    private val _cantidades = MutableStateFlow<Map<Int, Double>>(emptyMap())
    val cantidades: StateFlow<Map<Int, Double>> = _cantidades.asStateFlow()

    private val _nota = MutableStateFlow("")
    val nota: StateFlow<String> = _nota.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    /** Se vuelve true cuando el pedido se guardo (la vista navega a Mis pedidos). */
    private val _creado = MutableStateFlow(false)
    val creado: StateFlow<Boolean> = _creado.asStateFlow()

    private val _enviando = MutableStateFlow(false)

    /** Si la lavanderia no recibe pedidos (Ajustes del administrador) no se puede crear uno. */
    val recibiendoPedidos: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    /** Lineas elegidas (cantidad > 0) con su subtotal = cantidad x precio. */
    val lineas: StateFlow<List<LineaResumen>> = combine(servicios, _cantidades) { lista, cant ->
        lista.mapNotNull { s ->
            val c = cant[s.id] ?: 0.0
            if (c > 0.0) LineaResumen(s, c, c * s.precio) else null
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /** Suma de subtotales (antes del descuento). */
    val subtotal: StateFlow<Double> = combine(lineas, _cantidades) { l, _ -> l.sumOf { it.subtotal } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    /** Mejora IA: si el lavado por KG supera 5 kg, 10 % de descuento sobre las lineas en KG. */
    val descuento: StateFlow<Double> = combine(lineas, _cantidades) { l, _ -> calcularDescuento(l) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    /** Total = suma de subtotales - descuento. Se recalcula en cada cambio. */
    val total: StateFlow<Double> = combine(subtotal, descuento) { s, d -> s - d }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    /** Hoy + el mayor tiempo de entrega de los servicios elegidos; si cae domingo pasa al lunes. */
    val fechaEntrega: StateFlow<LocalDate?> = combine(lineas, _cantidades) { l, _ ->
        l.maxOfOrNull { it.servicio.horasEntrega }?.let { calcularFechaEntrega(it) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /** Habilitado solo si hay al menos un servicio con cantidad > 0 y la lavanderia recibe pedidos. */
    val puedeConfirmar: StateFlow<Boolean> = combine(lineas, recibiendoPedidos, _enviando) { l, recibe, enviando ->
        l.isNotEmpty() && recibe && !enviando
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        if (repetirId > 0) viewModelScope.launch { cargarRepeticion() }
    }

    /**
     * Mejora IA: carga las mismas lineas de un pedido anterior.
     * Los precios son los ACTUALES porque el total se calcula con los servicios activos de hoy.
     * Si un servicio ya no esta activo, se omite y se avisa.
     */
    private suspend fun cargarRepeticion() {
        val anterior = repo.observarPedido(repetirId).first() ?: return
        val activos = repo.observarServiciosActivos().first().associateBy { it.id }
        val cantidades = anterior.detalles
            .filter { it.detalle.servicioId in activos }
            .associate { d ->
                val s = activos.getValue(d.detalle.servicioId)
                val max = if (Unidad.desde(s.unidad) == Unidad.KG) MAX_KG else MAX_PIEZAS
                d.detalle.servicioId to d.detalle.cantidad.coerceAtMost(max)
            }
        _cantidades.value = cantidades
        _nota.value = anterior.pedido.nota
        if (cantidades.size < anterior.detalles.size) {
            _mensaje.value = "Algunos servicios del pedido anterior ya no están disponibles"
        }
    }

    /**
     * KG: pasos de 0.5, minimo 1.0 al activar y maximo 10.0.
     * PRENDA y UNIDAD: pasos de 1, maximo 30. Con 0 el servicio queda apagado.
     */
    fun cambiarCantidad(servicioId: Int, delta: Double) {
        val servicio = servicios.value.firstOrNull { it.id == servicioId } ?: return
        val actual = _cantidades.value[servicioId] ?: 0.0
        val nueva = when (Unidad.desde(servicio.unidad)) {
            Unidad.KG -> when {
                delta > 0 && actual < MIN_KG -> MIN_KG
                delta > 0 -> minOf(actual + PASO_KG, MAX_KG)
                actual <= MIN_KG -> 0.0
                else -> actual - PASO_KG
            }
            Unidad.PRENDA, Unidad.UNIDAD ->
                (actual + if (delta > 0) 1.0 else -1.0).coerceIn(0.0, MAX_PIEZAS)
        }
        _cantidades.value = _cantidades.value + (servicioId to nueva)
        _mensaje.value = null
    }

    fun cambiarNota(texto: String) {
        _nota.value = texto.take(120)
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun confirmar() {
        val elegidas = lineas.value
        if (elegidas.isEmpty() || _enviando.value) return
        if (!recibiendoPedidos.value) {
            _mensaje.value = "La lavandería no está recibiendo pedidos"
            return
        }
        val entrega = fechaEntrega.value ?: return
        _enviando.value = true
        viewModelScope.launch {
            try {
                // Limite: maximo 3 pedidos activos por estudiante
                if (repo.contarActivos(usuarioId) >= MAX_ACTIVOS) {
                    _mensaje.value = "Ya tienes 3 pedidos activos"
                    return@launch
                }
                repo.crearPedido(
                    usuarioId = usuarioId,
                    lineas = elegidas.map { LineaPedido(it.servicio.id, it.cantidad) },
                    fechaEntrega = entrega.toString(),
                    nota = _nota.value.trim(),
                    descuento = calcularDescuento(elegidas)
                )
                _creado.value = true
            } catch (e: Exception) {
                _mensaje.value = "No se pudo registrar el pedido. Intenta de nuevo"
            } finally {
                _enviando.value = false
            }
        }
    }

    companion object {
        const val PASO_KG = 0.5
        const val MIN_KG = 1.0
        const val MAX_KG = 10.0
        const val MAX_PIEZAS = 30.0
        const val MAX_ACTIVOS = 3
        const val UMBRAL_DESCUENTO_KG = 5.0
        const val PORCENTAJE_DESCUENTO = 0.10

        /** Descuento por volumen: 10 % del lavado por KG cuando supera 5 kg (redondeado a centimos). */
        fun calcularDescuento(lineas: List<LineaResumen>): Double {
            val enKg = lineas.filter { Unidad.desde(it.servicio.unidad) == Unidad.KG }
            val kilos = enKg.sumOf { it.cantidad }
            if (kilos <= UMBRAL_DESCUENTO_KG) return 0.0
            val base = enKg.sumOf { it.subtotal }
            return Math.round(base * PORCENTAJE_DESCUENTO * 100) / 100.0
        }

        /** 24 h = 1 dia, 48 h = 2 dias... La lavanderia no atiende domingos: pasa al lunes. */
        fun calcularFechaEntrega(horas: Int, desde: LocalDate = LocalDate.now()): LocalDate {
            val dias = (horas + 23) / 24
            var fecha = desde.plusDays(dias.toLong())
            if (fecha.dayOfWeek == DayOfWeek.SUNDAY) fecha = fecha.plusDays(1)
            return fecha
        }
    }
}

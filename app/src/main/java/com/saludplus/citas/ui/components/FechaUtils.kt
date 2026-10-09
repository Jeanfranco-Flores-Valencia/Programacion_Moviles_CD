package com.saludplus.citas.ui.components

import java.time.DayOfWeek
import java.time.LocalDate

/**
 * Utilidades de fechas para el calendario dinámico (Fase 2).
 * Se usan nombres propios en español porque el Locale del sistema
 * escribe "septiembre" y en Perú se usa "setiembre".
 */
object FechaUtils {

    private val DIAS = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    private val DIAS_CORTOS = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
    private val MESES = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"
    )

    fun esDiaHabil(fecha: LocalDate): Boolean =
        fecha.dayOfWeek != DayOfWeek.SATURDAY && fecha.dayOfWeek != DayOfWeek.SUNDAY

    /** Los próximos [cantidad] días hábiles desde [desde] (incluido si es hábil). */
    fun diasHabiles(desde: LocalDate, cantidad: Int = 5): List<LocalDate> =
        generateSequence(desde) { it.plusDays(1) }
            .filter { esDiaHabil(it) }
            .take(cantidad)
            .toList()

    /** Primer día hábil a partir de [fecha] (incluida). */
    fun proximoDiaHabil(fecha: LocalDate): LocalDate = diasHabiles(fecha, 1).first()

    fun nombreDiaCorto(fecha: LocalDate): String = DIAS_CORTOS[fecha.dayOfWeek.value - 1]

    fun nombreMes(fecha: LocalDate): String = MESES[fecha.monthValue - 1]

    /** "Octubre 2026", o "Octubre – Noviembre 2026" si la semana cruza de mes. */
    fun tituloMes(dias: List<LocalDate>): String {
        if (dias.isEmpty()) return ""
        val inicio = dias.first()
        val fin = dias.last()
        return when {
            inicio.year != fin.year -> "${nombreMes(inicio)} ${inicio.year} – ${nombreMes(fin)} ${fin.year}"
            inicio.month != fin.month -> "${nombreMes(inicio)} – ${nombreMes(fin)} ${fin.year}"
            else -> "${nombreMes(inicio)} ${inicio.year}"
        }
    }

    /** "2026-09-16" -> "Miércoles 16 de setiembre 2026" */
    fun fechaLarga(fechaIso: String): String = try {
        val f = LocalDate.parse(fechaIso)
        "${DIAS[f.dayOfWeek.value - 1]} ${f.dayOfMonth} de ${nombreMes(f).lowercase()} ${f.year}"
    } catch (e: Exception) {
        fechaIso
    }
}

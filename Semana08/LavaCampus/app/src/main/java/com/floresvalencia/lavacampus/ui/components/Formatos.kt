package com.floresvalencia.lavacampus.ui.components

import com.floresvalencia.lavacampus.model.Unidad
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Funciones de formato para mostrar datos (solo presentacion, sin reglas de negocio). */

private val localePe: Locale = Locale.forLanguageTag("es-PE")
private val fmtCorta = DateTimeFormatter.ofPattern("EEE d MMM", localePe)
private val fmtDiaMes = DateTimeFormatter.ofPattern("d MMM", localePe)
private val fmtHora = DateTimeFormatter.ofPattern("d MMM HH:mm", localePe)

/** 40.5 -> "S/ 40.50" */
fun soles(valor: Double): String = "S/ " + String.format(Locale.US, "%,.2f", valor)

/** 3.0 -> "3", 3.5 -> "3.5" */
fun numeroTexto(valor: Double): String =
    if (valor % 1.0 == 0.0) valor.toInt().toString() else String.format(Locale.US, "%.1f", valor)

/** 3.5 + KG -> "3.5 kg", 5 + PRENDA -> "5 prendas" */
fun cantidadTexto(cantidad: Double, unidad: Unidad): String = when (unidad) {
    Unidad.KG -> "${numeroTexto(cantidad)} kg"
    Unidad.PRENDA -> if (cantidad == 1.0) "1 prenda" else "${numeroTexto(cantidad)} prendas"
    Unidad.UNIDAD -> if (cantidad == 1.0) "1 unidad" else "${numeroTexto(cantidad)} unidades"
}

private fun limpiar(texto: String) = texto.replace(".", "")

/** "2026-10-22" -> "jue 22 oct" */
fun fechaCorta(iso: String): String = runCatching { fechaCorta(LocalDate.parse(iso)) }.getOrDefault(iso)

fun fechaCorta(fecha: LocalDate): String = limpiar(fecha.format(fmtCorta))

/** "2026-10-22" -> "22 oct" */
fun fechaDiaMes(iso: String): String =
    runCatching { limpiar(LocalDate.parse(iso).format(fmtDiaMes)) }.getOrDefault(iso)

/** millis -> "20 oct 10:15" */
fun fechaHora(millis: Long): String =
    limpiar(Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(fmtHora))

/** 12 -> "#012" */
fun numeroPedido(id: Int): String = "#" + id.toString().padStart(3, '0')

/** "Ana Garcia" -> "AG" */
fun iniciales(nombre: String): String =
    nombre.trim().split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }

/** "Ana Garcia" -> "Ana G." */
fun nombreCorto(nombre: String): String {
    val partes = nombre.trim().split(" ").filter { it.isNotBlank() }
    return if (partes.size < 2) nombre else "${partes[0]} ${partes[1].first()}."
}

fun capitalizar(texto: String): String = texto.replaceFirstChar { it.uppercase() }

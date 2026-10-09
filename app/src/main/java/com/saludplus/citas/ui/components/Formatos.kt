package com.saludplus.citas.ui.components

/** "2026-10-13" -> "13/10/2026" */
fun textoFecha(fechaIso: String): String {
    val partes = fechaIso.split("-")
    return if (partes.size == 3) "${partes[2]}/${partes[1]}/${partes[0]}" else fechaIso
}

/** "09:30" -> "09:30 a 10:00" (los turnos duran 30 minutos). */
fun rangoHora(hora: String): String {
    val partes = hora.split(":")
    val h = partes.getOrNull(0)?.toIntOrNull() ?: return hora
    val m = partes.getOrNull(1)?.toIntOrNull() ?: 0
    val fin = h * 60 + m + 30
    val hh = (fin / 60).toString().padStart(2, '0')
    val mm = (fin % 60).toString().padStart(2, '0')
    return "$hora a $hh:$mm"
}

/** "Juan Pérez" -> "Juan" */
fun primerNombre(nombreCompleto: String): String =
    nombreCompleto.trim().split(" ").firstOrNull { it.isNotBlank() } ?: ""

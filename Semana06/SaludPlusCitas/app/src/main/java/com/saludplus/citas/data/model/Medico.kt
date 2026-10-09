package com.saludplus.citas.data.model

import java.time.DayOfWeek

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val sedeId: Int,
    val titulo: String,          // Ej. "Ginecóloga"
    val cmp: String,             // Colegio Médico del Perú
    val calificacion: Double,
    val numResenas: Int,
    val disponibilidad: String,  // Ej. "Disponible hoy"
    val esMujer: Boolean,
    val tipoAtencion: String = "Consulta presencial",
    val direccion: String = "Av. Principal 123, Lima",
    val telefono: String = "(01) 456-7890",
    val diasAtencion: Set<DayOfWeek> = setOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY
    ),
    val rangoHoras: String = "08:00 - 16:00",
    val fotoUrl: String = ""
)

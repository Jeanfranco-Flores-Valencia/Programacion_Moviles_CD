package com.saludplus.citas.data.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val titulo: String,          // Ej. "Ginecóloga"
    val cmp: String,             // Colegio Médico del Perú
    val calificacion: Double,
    val numResenas: Int,
    val disponibilidad: String,  // Ej. "Disponible hoy"
    val esMujer: Boolean,
    val tipoAtencion: String = "Consulta presencial",
    val direccion: String = "Av. Los Olivos 123, Lima"
)

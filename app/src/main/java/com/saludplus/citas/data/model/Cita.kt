package com.saludplus.citas.data.model

data class Cita(
    val id: Int,
    val usuarioId: Int,
    val medicoId: Int,
    val especialidadId: Int,
    val fecha: String,   // formato yyyy-MM-dd
    val hora: String,    // formato HH:mm
    val motivo: String = "",
    val tipoAtencion: String = "Consulta presencial"
)

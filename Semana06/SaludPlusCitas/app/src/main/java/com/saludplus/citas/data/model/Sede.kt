package com.saludplus.citas.data.model

data class Sede(
    val id: Int,
    val nombre: String,
    val distrito: String,
    val direccion: String,
    val telefono: String,
    val horarioAtencion: String, // Ej. "Lun a Vie 8:00–18:00"
    val especialidadesIds: List<Int>
)

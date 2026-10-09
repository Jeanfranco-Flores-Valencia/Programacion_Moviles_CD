package com.saludplus.citas.data.model

/** Modelo propio para la pantalla Resultados (reto extra). */
data class Resultado(
    val id: Int,
    val titulo: String,
    val tipo: String,
    val fecha: String,
    val medico: String,
    val listo: Boolean
)

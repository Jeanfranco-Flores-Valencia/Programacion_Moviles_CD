package com.floresvalencia.lavacampus.model

/** Resultado del GROUP BY de ingresos por servicio (panel del administrador). */
data class IngresoServicio(
    val nombre: String,
    val total: Double
)

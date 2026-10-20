package com.floresvalencia.lavacampus.model

/** Lo que elige el estudiante en Nuevo pedido: un servicio y su cantidad. */
data class LineaPedido(
    val servicioId: Int,
    val cantidad: Double
)

package com.floresvalencia.lavacampus.model

/** Fila de la consulta de clientes: estudiante + cantidad de pedidos + total gastado. */
data class ClienteResumen(
    val id: Int,
    val nombre: String,
    val codigo: String,
    val correo: String,
    val pedidos: Int,
    val gastado: Double
)

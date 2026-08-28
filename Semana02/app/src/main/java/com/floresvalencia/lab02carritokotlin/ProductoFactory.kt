package com.floresvalencia.lab02carritokotlin

object ProductoFactory {
    fun crearFisico(
        nombre: String,
        precio: Double,
        cantidad: Int,
        pesoKg: Double,
        costoEnvio: Double = 15.0
    ): Producto = ProductoFisico(nombre, precio, cantidad, pesoKg, costoEnvio)

    fun crearDigital(
        nombre: String,
        precio: Double,
        cantidad: Int,
        urlDescarga: String,
        comisionLicencia: Double = 0.0
    ): Producto = ProductoDigital(nombre, precio, cantidad, urlDescarga, comisionLicencia)
}
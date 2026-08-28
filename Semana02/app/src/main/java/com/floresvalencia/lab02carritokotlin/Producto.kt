package com.floresvalencia.lab02carritokotlin

abstract class Producto(
    val nombre: String,
    precioInicial: Double,
    cantidadInicial: Int
) {
    var precio: Double = precioInicial
        private set

    var cantidad: Int = cantidadInicial
        private set

    init {
        if (precioInicial < 0) throw CarritoException.PrecioInvalidoException(precioInicial)
        if (cantidadInicial <= 0) throw CarritoException.CantidadInvalidaException(cantidadInicial)
    }

    abstract fun calcularPrecioFinal(): Double

    open fun obtenerDetalle(): String {
        return String.format("%-25s x%-2d  S/ %8.2f", nombre, cantidad, calcularPrecioFinal())
    }
    class ProductoFisico(
        nombre: String,
        precio: Double,
        cantidad: Int,
        val pesoKg: Double,
        val costoEnvio: Double = 15.0
    ) : Producto(nombre, precio, cantidad) {

        override fun calcularPrecioFinal(): Double {
            return (precio * cantidad) + costoEnvio
        }

        override fun obtenerDetalle(): String {
            return String.format("%-25s x%-2d  S/ %8.2f  [Fisico: Envio S/ %.2f]", nombre, cantidad, calcularPrecioFinal(), costoEnvio)
        }
    }

    class ProductoDigital(
        nombre: String,
        precio: Double,
        cantidad: Int,
        val urlDescarga: String,
        val comisionLicencia: Double = 0.0
    ) : Producto(nombre, precio, cantidad) {

        override fun calcularPrecioFinal(): Double {
            return (precio + comisionLicencia) * cantidad
        }

        override fun obtenerDetalle(): String {
            return String.format("%-25s x%-2d  S/ %8.2f  [Digital: Licencia S/ %.2f]", nombre, cantidad, calcularPrecioFinal(), comisionLicencia)
        }
    }
}

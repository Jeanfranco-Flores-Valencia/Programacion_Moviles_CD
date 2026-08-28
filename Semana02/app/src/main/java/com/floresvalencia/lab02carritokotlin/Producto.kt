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
}
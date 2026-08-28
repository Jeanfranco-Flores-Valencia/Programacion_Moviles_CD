package com.floresvalencia.lab02carritokotlin

class CarritoDeCompras(
    var estrategiaDescuento: EstrategiaDescuento = SinDescuento()
) {
    private val _productos = mutableListOf<Producto>()
    val productos: List<Producto> get() = _productos.toList()

    fun agregarProducto(producto: Producto) {
        _productos.add(producto)
    }

    fun eliminarProducto(producto: Producto) {
        _productos.remove(producto)
    }

    fun obtenerCantidadItems(): Int = _productos.sumOf { it.cantidad }

    fun calcularSubtotal(): Double = _productos.sumOf { it.calcularPrecioFinal() }

    fun calcularIGV(): Double = calcularSubtotal() * 0.18

    fun calcularTotalSinDescuento(): Double = calcularSubtotal() + calcularIGV()

    fun calcularDescuento(): Double = estrategiaDescuento.calcularDescuento(calcularTotalSinDescuento())

    fun calcularTotalFinal(): Double = calcularTotalSinDescuento() - calcularDescuento()

    fun productoMasCaro(): Producto? {
        if (_productos.isEmpty()) throw CarritoException.CarritoVacioException()
        return _productos.maxByOrNull { it.precio }
    }
}
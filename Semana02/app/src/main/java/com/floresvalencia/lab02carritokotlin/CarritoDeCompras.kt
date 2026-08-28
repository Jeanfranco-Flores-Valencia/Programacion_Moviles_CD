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
    // Agrega estos métodos dentro de CarritoDeCompras

    fun imprimirDetalle() {
        println("--------- DETALLE DEL CARRITO ---------")
        _productos.forEachIndexed { i, p ->
            println("%2d. %s".format(i + 1, p.obtenerDetalle()))
        }
        println("-----------------------------------------")
    }

    fun imprimirResumen(nombreCliente: String) {
        if (_productos.isEmpty()) throw CarritoException.CarritoVacioException()

        println("=========================================")
        println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
        println("=========================================")
        println("Cliente: $nombreCliente\n")

        imprimirDetalle()

        println(String.format("%-25s: %d", "Cantidad de items", obtenerCantidadItems()))
        println(String.format("%-25s: S/ %8.2f", "Subtotal", calcularSubtotal()))
        println(String.format("%-25s: S/ %8.2f", "IGV (18%)", calcularIGV()))
        println(String.format("%-25s: S/ %8.2f", "TOTAL SIN DESCUENTO", calcularTotalSinDescuento()))

        val descto = calcularDescuento()
        if (descto > 0) {
            println("Estrategia               : ${estrategiaDescuento.obtenerDescripcion()}")
            println(String.format("%-25s: S/ %8.2f", "Descuento Aplicado", descto))
        }

        println(String.format("%-25s: S/ %8.2f", "TOTAL A PAGAR", calcularTotalFinal()))
        println("-----------------------------------------")

        productoMasCaro()?.let {
            println("Producto mas caro: ${it.nombre} (S/ %.2f)".format(it.precio))
        }
        println("\nGracias por su compra, $nombreCliente!")
    }
}
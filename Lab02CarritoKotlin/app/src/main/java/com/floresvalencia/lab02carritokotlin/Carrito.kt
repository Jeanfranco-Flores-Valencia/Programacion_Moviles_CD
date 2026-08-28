package com.floresvalencia.lab02carritokotlin
data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)
fun calcularSubtotal(productos: List<Producto>): Double { var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}
fun calcularIGV(subtotal: Double): Double {
    val igv = subtotal * 0.18
    return igv
// TODO: devuelve el 18% del subtotal
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    val total = subtotal + igv
    return total
// TODO: devuelve la suma de ambos
}
fun main() {
    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("=========================================")
    val nombreCliente = "Jeanfranco Flores"
    val carrito = mutableListOf<Producto>()
    println("Cliente: $nombreCliente")
    println()
    carrito.add(Producto("Audifonos Logitech", 250.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Laptop ASUS", 5000.0, 2))
    carrito.add(Producto("Macbook Pro", 7600.0, 2))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }
    println()
    mostrarDetalle(carrito)

    println("Cantidad de productos : ${carrito.size}")

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println(String.format("%-22s: S/ %7.2f", "Subtotal", subtotal))
    println(String.format("%-22s: S/ %7.2f", "IGV (18%)", igv))
    println(String.format("%-22s: S/ %7.2f", "TOTAL A PAGAR", total))
    println("-----------------------------------------")
}
fun mostrarDetalle(productos: List<Producto>) {
    println("--------- DETALLE DEL CARRITO ---------")
    var i = 1
    for (p in productos) {
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-20s x%d  S/ %8.2f", i, p.nombre, p.cantidad, importe))
        i++
    }
    println("-----------------------------------------")
}

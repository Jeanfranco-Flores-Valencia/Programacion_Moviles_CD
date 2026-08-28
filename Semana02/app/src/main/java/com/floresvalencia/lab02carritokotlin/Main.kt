package com.floresvalencia.lab02carritokotlin
import com.floresvalencia.lab02carritokotlin.EstrategiaDescuento.DescuentoPorMonto
fun main() {
    val cliente = "Jeanfranco Flores"

    try {
        val carrito = CarritoDeCompras(estrategiaDescuento = DescuentoPorMonto())

        // Instanciación limpia utilizando el patrón Factory
        carrito.agregarProducto(
            ProductoFactory.crearFisico("Audifonos Logitech", 250.0, 1, pesoKg = 0.5, costoEnvio = 10.0)
        )
        carrito.agregarProducto(
            ProductoFactory.crearFisico("Mouse Logitech", 45.5, 2, pesoKg = 0.2, costoEnvio = 5.0)
        )
        carrito.agregarProducto(
            ProductoFactory.crearFisico("Laptop ASUS", 5000.0, 2, pesoKg = 2.5, costoEnvio = 25.0)
        )
        carrito.agregarProducto(
            ProductoFactory.crearDigital("Macbook Pro (Licencia)", 7600.0, 2, urlDescarga = "https://tecsup.edu.pe/keys/mac")
        )

        carrito.imprimirResumen(cliente)

    } catch (e: CarritoException) {
        println("\n[ERROR DE NEGOCIO]: ${e.message}")
    } catch (e: Exception) {
        println("\n[ERROR INESPERADO]: ${e.localizedMessage}")
    }
}
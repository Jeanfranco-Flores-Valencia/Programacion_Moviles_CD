package com.floresvalencia.lab02carritokotlin

import com.floresvalencia.lab02carritokotlin.Producto
import org.junit.Assert.assertEquals
import org.junit.Test

class CarritoTest {

    @Test
    fun debeCalcularSubtotalIGVYTotalCorrectamente() {
        val productos = listOf(
            Producto("Audifonos Logitech", 250.0, 1),
            Producto("Mouse Logitech", 45.5, 2),
            Producto("Laptop ASUS", 5000.0, 2),
            Producto("Macbook Pro", 7600.0, 2)

        )

        val subtotalCalculado = calcularSubtotal(productos) // 341.0
        val igvCalculado = calcularIGV(subtotalCalculado)   // 61.38
        val totalCalculado = calcularTotal(subtotalCalculado, igvCalculado) // 402.38

        assertEquals(25541.0, subtotalCalculado, 0.01)
        assertEquals(4597.38, igvCalculado, 0.01)
        assertEquals(30138.38, totalCalculado, 0.01)
    }

    @Test
    fun debeCalcularDescuentoCorrectamente() {
        assertEquals(600.0, calcularDescuento(6000.0), 0.01) // > 5000 (10%)
        assertEquals(200.0, calcularDescuento(4000.0), 0.01) // > 3000 (5%)
        assertEquals(0.0, calcularDescuento(1000.0), 0.01)   // < 3000 (0%)
    }

    @Test
    fun probarEjecucionCompletaDeMain() {
        // Llama directamente a tu función main() completa sin modificar nada
        main()
    }
}
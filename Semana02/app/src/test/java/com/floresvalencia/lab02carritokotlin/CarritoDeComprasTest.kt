package com.floresvalencia.lab02carritokotlin
import com.floresvalencia.lab02carritokotlin.EstrategiaDescuento.DescuentoPorMonto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CarritoDeComprasTest {

    private lateinit var carrito: CarritoDeCompras

    @Before
    fun setUp() {
        carrito = CarritoDeCompras(estrategiaDescuento = DescuentoPorMonto())
    }

    @Test
    fun debeCalcularTotalesCorrectamente() {
        // Producto físico: (100 * 2) + 15 de envío = 215.00
        val pFisico = ProductoFactory.crearFisico("Teclado", 100.0, 2, pesoKg = 1.0, costoEnvio = 15.0)
        carrito.agregarProducto(pFisico)

        val subtotalEsperado = 215.0
        val igvEsperado = subtotalEsperado * 0.18
        val totalEsperado = subtotalEsperado + igvEsperado

        assertEquals(subtotalEsperado, carrito.calcularSubtotal(), 0.01)
        assertEquals(igvEsperado, carrito.calcularIGV(), 0.01)
        assertEquals(totalEsperado, carrito.calcularTotalSinDescuento(), 0.01)
        assertEquals(0.0, carrito.calcularDescuento(), 0.01)
    }

    @Test
    fun debeAplicarDescuentoMayoresA5000() {
        // Producto digital: (3000 * 2) = 6000.00
        val pDigital = ProductoFactory.crearDigital("Curso Kotlin", 3000.0, 2, urlDescarga = "https://tecsup.edu.pe")
        carrito.agregarProducto(pDigital)

        val totalSinDescuento = carrito.calcularTotalSinDescuento()
        val descuentoEsperado = totalSinDescuento * 0.10

        assertTrue(totalSinDescuento > 5000.0)
        assertEquals(descuentoEsperado, carrito.calcularDescuento(), 0.01)
    }

    @Test(expected = CarritoException.CarritoVacioException::class)
    fun debeLanzarExcepcionSiCarritoEstaVacio() {
        carrito.imprimirResumen("Cliente Prueba")
    }

    @Test(expected = CarritoException.PrecioInvalidoException::class)
    fun debeLanzarExcepcionSiPrecioEsNegativo() {
        ProductoFactory.crearFisico("Monitor", -500.0, 1, pesoKg = 3.0)
    }

    @Test(expected = CarritoException.CantidadInvalidaException::class)
    fun debeLanzarExcepcionSiCantidadEsCero() {
        ProductoFactory.crearDigital("Ebook", 50.0, 0, urlDescarga = "https://tecsup.edu.pe")
    }
    @Test
    fun probarImpresionDeResumenEnConsola() {
        val cliente = "Jeanfranco Flores"
        carrito.agregarProducto(
            ProductoFactory.crearFisico("Audifonos Logitech", 250.0, 1, pesoKg = 0.5, costoEnvio = 10.0)
        )
        carrito.agregarProducto(
            ProductoFactory.crearDigital("Macbook Pro (Licencia)", 7600.0, 2, urlDescarga = "https://tecsup.edu.pe")
        )

        // Esta línea imprimirá todo el reporte en la consola de Gradle/JUnit
        carrito.imprimirResumen(cliente)
    }
}
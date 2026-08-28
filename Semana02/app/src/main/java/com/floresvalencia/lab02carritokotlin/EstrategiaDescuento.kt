package com.floresvalencia.lab02carritokotlin

interface EstrategiaDescuento {
    fun calcularDescuento(total: Double): Double
    fun obtenerDescripcion(): String

    class DescuentoPorMonto : EstrategiaDescuento {
        override fun calcularDescuento(total: Double): Double {
            return when {
                total > 5000 -> total * 0.10
                total > 3000 -> total * 0.05
                else -> 0.0
            }
        }

        override fun obtenerDescripcion(): String {
            return "Descuento Escalonado (10% > S/5000, 5% > S/3000)"
        }
    }

    class DescuentoFijo(private val montoFijo: Double) : EstrategiaDescuento {
        override fun calcularDescuento(total: Double): Double = montoFijo.coerceAtMost(total)
        override fun obtenerDescripcion(): String = "Descuento Fijo de S/ %.2f".format(montoFijo)
    }

    class SinDescuento : EstrategiaDescuento {
        override fun calcularDescuento(total: Double): Double = 0.0
        override fun obtenerDescripcion(): String = "Sin descuento aplicado"
    }
}
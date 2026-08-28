package com.floresvalencia.lab02carritokotlin

interface EstrategiaDescuento {
    fun calcularDescuento(total: Double): Double
    fun obtenerDescripcion(): String
}
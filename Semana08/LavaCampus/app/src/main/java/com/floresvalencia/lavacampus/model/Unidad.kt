package com.floresvalencia.lavacampus.model

/** Forma de cobro de un servicio. */
enum class Unidad(val etiqueta: String, val sufijo: String, val corto: String) {
    KG("Kilo", "por kg", "kg"),
    PRENDA("Prenda", "por prenda", "prendas"),
    UNIDAD("Unidad", "por unidad", "und.");

    companion object {
        fun desde(texto: String): Unidad = entries.firstOrNull { it.name == texto } ?: UNIDAD
    }
}

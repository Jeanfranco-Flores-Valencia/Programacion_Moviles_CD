package com.floresvalencia.lavacampus.model

import com.floresvalencia.lavacampus.data.local.entity.Servicio
import java.util.Locale

/** Convierte la unidad en texto de precio: KG -> "S/ 8.00 por kg", PRENDA -> "S/ 2.50 por prenda". */
fun Servicio.textoPrecio(): String =
    "S/ " + String.format(Locale.US, "%.2f", precio) + " " + Unidad.desde(unidad).sufijo

package com.floresvalencia.tecsupfit.navigation

import android.net.Uri

object Rutas {
    // Pestañas del bottomBar
    const val INICIO = "inicio"
    const val RESERVAS = "reservas"
    const val RUTINAS = "rutinas"
    const val PERFIL = "perfil"

    // Flujo secuencial (con parámetros)
    const val DETALLE = "detalle/{claseId}"
    const val CONFIRMACION = "confirmacion/{claseId}/{horario}"

    fun detalle(claseId: Int) = "detalle/$claseId"

    // Uri.encode evita problemas con espacios, comas y ":" dentro de la ruta
    fun confirmacion(claseId: Int, horario: String) =
        "confirmacion/$claseId/${Uri.encode(horario)}"
}
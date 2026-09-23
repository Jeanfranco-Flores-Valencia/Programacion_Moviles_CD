package com.floresvalencia.clinicasalud.navigation

import android.net.Uri

object Rutas {
    // Rutas "patrón" que se registran en el NavHost
    const val INICIO = "inicio"
    const val PERFIL_MEDICO = "perfil_medico/{medicoId}"
    const val AGENDAR = "agendar/{medicoId}"
    const val CONFIRMACION = "confirmacion/{medicoId}/{fecha}/{hora}"
    const val MIS_CITAS = "mis_citas"
    const val HISTORIAL = "historial"

    // Funciones que arman la ruta real con los datos del ítem elegido
    fun perfilMedico(medicoId: Int) = "perfil_medico/$medicoId"
    fun agendar(medicoId: Int) = "agendar/$medicoId"

    // Uri.encode evita problemas con espacios, tildes y ":" dentro de la ruta
    fun confirmacion(medicoId: Int, fecha: String, hora: String) =
        "confirmacion/$medicoId/${Uri.encode(fecha)}/${Uri.encode(hora)}"
}
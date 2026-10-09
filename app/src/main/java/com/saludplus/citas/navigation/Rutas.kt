package com.saludplus.citas.navigation

import android.net.Uri

object Rutas {
    // Auth
    const val SPLASH = "splash"
    const val REGISTRO = "registro"
    const val LOGIN = "login"
    const val TERMINOS = "terminos"

    // Home
    const val HOME = "home"

    // Agendamiento
    const val ESPECIALIDADES = "especialidades"
    const val MEDICOS = "medicos/{especialidadId}"
    const val FECHA_HORA = "fecha_hora/{medicoId}"
    const val CONFIRMAR_CITA = "confirmar_cita/{medicoId}/{fecha}/{hora}"
    const val CITA_EXITOSA = "cita_exitosa/{citaId}"

    // Citas, perfil y otros
    const val MIS_CITAS = "mis_citas"
    const val DETALLE_CITA = "detalle_cita/{citaId}"
    const val PERFIL = "perfil"
    const val RESULTADOS = "resultados"
    const val NOTIFICACIONES = "notificaciones"

    // Constructores de rutas con parámetros
    fun medicos(especialidadId: Int) = "medicos/$especialidadId"
    fun fechaHora(medicoId: Int) = "fecha_hora/$medicoId"
    fun confirmarCita(medicoId: Int, fecha: String, hora: String) =
        "confirmar_cita/$medicoId/${Uri.encode(fecha)}/${Uri.encode(hora)}"
    fun citaExitosa(citaId: Int) = "cita_exitosa/$citaId"
    fun detalleCita(citaId: Int) = "detalle_cita/$citaId"
}

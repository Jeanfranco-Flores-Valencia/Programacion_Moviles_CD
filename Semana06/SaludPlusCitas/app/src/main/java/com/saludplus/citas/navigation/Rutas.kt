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

    // Agendamiento (Flujo obligatorio: Sedes -> Especialidades -> Médicos -> Fecha/Hora -> Confirmar -> Cita Exitosa)
    const val SEDES = "sedes"
    const val ESPECIALIDADES = "especialidades/{sedeId}"
    const val MEDICOS = "medicos/{sedeId}/{especialidadId}"
    const val FECHA_HORA = "fecha_hora/{medicoId}/{sedeId}"
    const val CONFIRMAR_CITA = "confirmar_cita/{medicoId}/{sedeId}/{fecha}/{hora}"
    const val CITA_EXITOSA = "cita_exitosa/{citaId}"

    // Doctores (desde menú lateral)
    const val DOCTORES_LISTA = "doctores_lista"
    const val DETALLE_MEDICO = "detalle_medico/{medicoId}"

    // Citas, perfil y otros
    const val MIS_CITAS = "mis_citas"
    const val DETALLE_CITA = "detalle_cita/{citaId}"
    const val PERFIL = "perfil"
    const val RESULTADOS = "resultados"
    const val NOTIFICACIONES = "notificaciones"

    // Constructores de rutas con parámetros
    fun especialidades(sedeId: Int) = "especialidades/$sedeId"
    fun medicos(sedeId: Int, especialidadId: Int) = "medicos/$sedeId/$especialidadId"
    fun fechaHora(medicoId: Int, sedeId: Int) = "fecha_hora/$medicoId/$sedeId"
    fun confirmarCita(medicoId: Int, sedeId: Int, fecha: String, hora: String) =
        "confirmar_cita/$medicoId/$sedeId/${Uri.encode(fecha)}/${Uri.encode(hora)}"
    fun citaExitosa(citaId: Int) = "cita_exitosa/$citaId"
    fun detalleCita(citaId: Int) = "detalle_cita/$citaId"
    fun detalleMedico(medicoId: Int) = "detalle_medico/$medicoId"
}

package com.floresvalencia.clinicasalud.data

// ---------- Modelos ----------

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String,   // se usa para filtrar: "Cardiología"
    val titulo: String,         // se muestra en pantalla: "Cardióloga"
    val calificacion: Double,
    val resenas: Int,
    val aniosExperiencia: Int,
    val descripcion: String
)

data class FechaCita(val diaCorto: String, val numero: Int, val diaLargo: String) {
    val texto: String get() = "$diaLargo $numero"   // ej. "Viernes 25"
}

enum class EstadoCita(val texto: String) {
    CONFIRMADA("Confirmada"),
    COMPLETADA("Completada")
}

data class Cita(
    val medico: Medico,
    val fecha: String,
    val hora: String,
    val estado: EstadoCita
)

data class ConsultaHistorial(
    val fecha: String,
    val medico: String,
    val especialidad: String,
    val diagnostico: String
)

// ---------- Datos de ejemplo (sin base de datos) ----------

object DatosClinica {

    const val NOMBRE_PACIENTE = "Juan Pérez"

    val especialidades = listOf("Todos", "Cardiología", "Pediatría", "Dermatología", "Traumatología")

    val medicos = listOf(
        Medico(1, "Dra. Ana Torres", "Cardiología", "Cardióloga", 4.9, 128, 12,
            "Especialista en arritmias e hipertensión, formación en la Clínica Mayo."),
        Medico(2, "Dr. Luis Vega", "Pediatría", "Pediatra", 4.7, 96, 8,
            "Atención integral para bebés, niños y adolescentes. Control de crecimiento y vacunación."),
        Medico(3, "Dra. Rosa Díaz", "Dermatología", "Dermatóloga", 4.8, 110, 10,
            "Tratamiento de afecciones de la piel, cabello y uñas. Dermatología clínica y estética."),
        Medico(4, "Dr. Jorge Salas", "Traumatología", "Traumatólogo", 4.6, 74, 15,
            "Lesiones deportivas, fracturas y rehabilitación de rodilla y hombro."),
        Medico(5, "Dr. Miguel Paredes", "Cardiología", "Cardiólogo", 4.5, 52, 6,
            "Control de hipertensión arterial y evaluación cardiovascular pre-deportiva.")
    )

    val fechasDisponibles = listOf(
        FechaCita("Jue", 24, "Jueves"),
        FechaCita("Vie", 25, "Viernes"),
        FechaCita("Sáb", 26, "Sábado"),
        FechaCita("Lun", 28, "Lunes")
    )

    val horasDisponibles = listOf("9:00 am", "10:30 am", "12:00 pm", "3:00 pm", "4:30 pm")

    // Citas previas (para que "Mis citas" muestre también el estado Completada)
    val citasIniciales = listOf(
        Cita(medicos[1], "Miércoles 16", "3:00 pm", EstadoCita.COMPLETADA),
        Cita(medicos[2], "Lunes 14", "10:00 am", EstadoCita.COMPLETADA)
    )

    val historial = listOf(
        ConsultaHistorial("16 Sep 2026", "Dr. Luis Vega", "Pediatría", "Control de rutina, sin observaciones."),
        ConsultaHistorial("14 Sep 2026", "Dra. Rosa Díaz", "Dermatología", "Dermatitis leve. Crema tópica por 7 días."),
        ConsultaHistorial("02 Jul 2026", "Dr. Jorge Salas", "Traumatología", "Esguince de tobillo grado I. Reposo y fisioterapia."),
        ConsultaHistorial("15 Mar 2026", "Dra. Ana Torres", "Cardiología", "Chequeo preventivo. Resultados normales.")
    )

    fun buscarMedico(id: Int): Medico = medicos.first { it.id == id }
}
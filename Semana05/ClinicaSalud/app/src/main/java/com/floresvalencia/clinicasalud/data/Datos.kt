package com.floresvalencia.clinicasalud.data
// ---------- Modelos ----------

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: Double,
    val aniosExperiencia: Int,
    val descripcion: String
)

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

    val especialidades = listOf("Todos", "Cardiología", "Pediatría", "Dermatología", "Traumatología")

    val medicos = listOf(
        Medico(1, "Dra. Ana Torres", "Cardiología", 4.9, 12,
            "Especialista en prevención y tratamiento de enfermedades del corazón. Realiza chequeos preventivos y control de arritmias."),
        Medico(2, "Dr. Luis Ramírez", "Pediatría", 4.7, 8,
            "Atención integral para bebés, niños y adolescentes. Control de crecimiento y vacunación."),
        Medico(3, "Dra. Carla Mendoza", "Dermatología", 4.8, 10,
            "Tratamiento de afecciones de la piel, cabello y uñas. Dermatología clínica y estética."),
        Medico(4, "Dr. Jorge Salas", "Traumatología", 4.6, 15,
            "Lesiones deportivas, fracturas y rehabilitación de rodilla y hombro."),
        Medico(5, "Dr. Miguel Paredes", "Cardiología", 4.5, 6,
            "Control de hipertensión arterial y evaluación cardiovascular pre-deportiva.")
    )

    val fechasDisponibles = listOf("Lun 5 Oct", "Mar 6 Oct", "Mié 7 Oct", "Jue 8 Oct")
    val horasDisponibles = listOf("08:00", "09:30", "11:00", "15:00", "16:30")

    // Citas previas (para que "Mis citas" muestre también el estado Completada)
    val citasIniciales = listOf(
        Cita(medicos[2], "Lun 14 Sep", "10:00", EstadoCita.COMPLETADA),
        Cita(medicos[1], "Vie 18 Sep", "15:30", EstadoCita.COMPLETADA)
    )

    val historial = listOf(
        ConsultaHistorial("18 Sep 2026", "Dr. Luis Ramírez", "Pediatría", "Control de rutina, sin observaciones."),
        ConsultaHistorial("14 Sep 2026", "Dra. Carla Mendoza", "Dermatología", "Dermatitis leve. Crema tópica por 7 días."),
        ConsultaHistorial("02 Jul 2026", "Dr. Jorge Salas", "Traumatología", "Esguince de tobillo grado I. Reposo y fisioterapia."),
        ConsultaHistorial("15 Mar 2026", "Dra. Ana Torres", "Cardiología", "Chequeo preventivo. Resultados normales.")
    )

    fun buscarMedico(id: Int): Medico = medicos.first { it.id == id }
}
package com.floresvalencia.tecsupfit.data

// ---------- Modelos ----------

/** Un turno es una opción de horario de la clase con sus cupos libres. */
data class Turno(val etiqueta: String, val cuposLibres: Int)

data class ClaseGym(
    val id: Int,
    val nombre: String,
    val dia: String,          // "Hoy", "Jueves", ...
    val esHoy: Boolean,
    val hora: String,         // "6:00 pm"
    val sala: String,
    val duracionMin: Int,
    val cuposTotales: Int,
    val descripcion: String,
    val turnos: List<Turno>
) {
    // Texto para las tarjetas: "6:00 pm · Sala 1" o "Jueves 5:00 pm · Sala 2"
    val horarioTexto: String
        get() = if (esHoy) "$hora · $sala" else "$dia $hora · $sala"
}

enum class EstadoReserva(val texto: String) {
    CONFIRMADA("Confirmada"),
    COMPLETADA("Completada")
}

data class Reserva(
    val clase: ClaseGym,
    val horario: String,
    val estado: EstadoReserva
)

data class Rutina(
    val nombre: String,
    val enfoque: String,
    val ejercicios: Int,
    val duracionMin: Int
)

data class Usuario(
    val nombre: String,
    val plan: String,
    val codigo: String,
    val correo: String
)

// ---------- Datos de ejemplo (sin base de datos) ----------

object DatosGym {

    const val FILTRO_HOY = "Hoy"
    const val FILTRO_SEMANA = "Esta semana"
    val filtros = listOf(FILTRO_HOY, FILTRO_SEMANA)

    val clases = listOf(
        ClaseGym(1, "Yoga funcional", "Hoy", true, "7:00 am", "Sala 2", 60, 15,
            "Posturas, respiración y movilidad para mejorar la flexibilidad y reducir el estrés.",
            listOf(Turno("Hoy, 7:00 am", 5), Turno("Jue, 7:00 am", 10), Turno("Sáb, 9:00 am", 12))),
        ClaseGym(2, "Cross Training", "Hoy", true, "6:00 pm", "Sala 1", 45, 12,
            "Entrenamiento funcional de alta intensidad. Cupos limitados.",
            listOf(Turno("Hoy, 6:00 pm", 8), Turno("Jue, 6:00 pm", 3), Turno("Sáb, 10:00 am", 0))),
        ClaseGym(3, "Spinning", "Hoy", true, "7:30 pm", "Sala 3", 50, 20,
            "Ciclismo indoor a ritmo de música. Trabaja resistencia cardiovascular y piernas.",
            listOf(Turno("Hoy, 7:30 pm", 4), Turno("Vie, 7:30 pm", 15), Turno("Dom, 9:00 am", 9))),
        ClaseGym(4, "Zumba", "Jueves", false, "5:00 pm", "Sala 2", 60, 25,
            "Baile aeróbico con ritmos latinos para quemar calorías divirtiéndote.",
            listOf(Turno("Jue, 5:00 pm", 18), Turno("Sáb, 11:00 am", 20))),
        ClaseGym(5, "Box funcional", "Viernes", false, "6:30 pm", "Sala 1", 45, 10,
            "Combinaciones de golpes y circuitos para mejorar potencia y coordinación.",
            listOf(Turno("Vie, 6:30 pm", 2), Turno("Sáb, 8:00 am", 6))),
        ClaseGym(6, "Pilates", "Sábado", false, "10:00 am", "Sala 3", 50, 12,
            "Fortalecimiento del core, postura y control del movimiento.",
            listOf(Turno("Sáb, 10:00 am", 7), Turno("Dom, 10:00 am", 0), Turno("Lun, 7:00 am", 11)))
    )

    // Reservas previas (para que "Reservas" muestre también el estado Completada)
    val reservasIniciales = listOf(
        Reserva(clases[0], "Ayer, 7:00 am", EstadoReserva.COMPLETADA),
        Reserva(clases[3], "Lun, 5:00 pm", EstadoReserva.COMPLETADA)
    )

    val rutinas = listOf(
        Rutina("Tren superior", "Pecho, espalda y brazos", 6, 40),
        Rutina("Piernas y glúteos", "Fuerza de tren inferior", 5, 35),
        Rutina("Core express", "Abdomen y zona lumbar", 4, 15),
        Rutina("Cardio HIIT", "Quema de grasa", 8, 20),
        Rutina("Movilidad", "Estiramientos y flexibilidad", 7, 25)
    )

    val usuario = Usuario(
        nombre = "Diego Ramos",
        plan = "Plan Premium",
        codigo = "U20231234",
        correo = "diego.ramos@tecsup.edu.pe"
    )

    const val CLASES_PREVIAS = 12   // clases tomadas antes de usar la app
    const val RACHA_DIAS = 3

    fun buscarClase(id: Int): ClaseGym = clases.first { it.id == id }
}
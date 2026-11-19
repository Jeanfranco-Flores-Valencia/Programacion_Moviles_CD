package com.floresv.campusgps.data

import com.floresv.campusgps.model.Lugar

/** Los 5 lugares ficticios del campus, cerca del punto base -12.0464, -77.0428 (Lima). */
class LugaresRepositoryLocal : LugaresRepository {
    override fun obtenerLugares(): List<Lugar> = listOf(
        Lugar(
            1, "Biblioteca Central", "Estudio", -12.0460, -77.0435,
            descripcion = "Tres pisos de libros y revistas, salas de estudio grupal que se reservan en el mostrador y zona silenciosa con enchufes en cada mesa.",
            horario = "Lun a sáb · 8:00 a 22:00"
        ),
        Lugar(
            2, "Cafetería Campus", "Comida", -12.0470, -77.0420,
            descripcion = "Menú del día, café, jugos y sándwiches. Tiene terraza al aire libre y mesas para trabajar entre clases.",
            horario = "Lun a vie · 7:00 a 20:00"
        ),
        Lugar(
            3, "Laboratorio de Cómputo", "Estudio", -12.0455, -77.0415,
            descripcion = "40 computadoras con Android Studio y conexión rápida. Se usa con el carné de estudiante cuando no hay clase.",
            horario = "Lun a vie · 8:00 a 21:00"
        ),
        Lugar(
            4, "Auditorio Principal", "Eventos", -12.0480, -77.0440,
            descripcion = "Capacidad para 300 personas, con proyector y sonido. Aquí se hacen las charlas, ferias y ceremonias del campus.",
            horario = "Según la agenda de eventos"
        ),
        Lugar(
            5, "Estacionamiento Norte", "Servicios", -12.0440, -77.0430,
            descripcion = "120 espacios para autos, zona de bicicletas y vigilancia. El ingreso es con el carné o con un ticket de visita.",
            horario = "Todos los días · 6:00 a 23:00"
        )
    )
}

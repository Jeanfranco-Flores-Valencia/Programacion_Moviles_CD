package com.saludplus.citas.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Sede
import com.saludplus.citas.data.model.Usuario
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Repositorio único de la app (object = una sola instancia compartida por todas las pantallas).
 * No usa base de datos: todo vive en colecciones en memoria y se pierde al cerrar la app.
 * Las listas que cambian (usuarios y citas) son "state lists" para que Compose
 * vuelva a dibujar las pantallas automáticamente cuando se agregan o eliminan elementos.
 */
object Repositorio {

    // ------------------------------------------------------------------
    // Colecciones
    // ------------------------------------------------------------------

    val usuarios = mutableStateListOf(
        Usuario(1, "Juan Pérez", "987654321", "juan@correo.com", "123456"),
        Usuario(2, "María López", "912345678", "maria@correo.com", "123456")
    )

    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set

    val sedes = listOf(
        Sede(1, "Sede Miraflores", "Miraflores", "Av. Benavides 456", "(01) 241-5000", "Lun a Sáb 08:00–20:00", listOf(1, 2, 3, 4, 5, 6, 7, 8)),
        Sede(2, "Sede San Isidro", "San Isidro", "Av. Camino Real 123", "(01) 442-3000", "Lun a Vie 08:00–19:00", listOf(1, 3, 4, 5, 8)),
        Sede(3, "Sede Surco", "Santiago de Surco", "Av. Primavera 789", "(01) 372-9000", "Lun a Sáb 08:00–18:00", listOf(1, 2, 5, 6, 7)),
        Sede(4, "Sede La Molina", "La Molina", "Av. La Molina 555", "(01) 348-1100", "Lun a Vie 08:00–18:00", listOf(1, 2, 3, 4)),
        Sede(5, "Sede San Borja", "San Borja", "Av. Javier Prado Este 2100", "(01) 475-6000", "Lun a Sáb 08:00–20:00", listOf(1, 4, 6, 8)),
        Sede(6, "Sede Jesús María", "Jesús María", "Av. Brasil 1450", "(01) 431-2200", "Lun a Vie 08:00–18:00", listOf(1, 2, 3, 5)),
        Sede(7, "Sede Los Olivos", "Los Olivos", "Av. Carlos Izaguirre 800", "(01) 521-4400", "Lun a Sáb 08:00–19:00", listOf(1, 2, 6, 7)),
        Sede(8, "Sede San Miguel", "San Miguel", "Av. La Marina 2300", "(01) 566-7800", "Lun a Vie 08:00–18:00", listOf(1, 3, 4, 5)),
        Sede(9, "Sede Lince", "Lince", "Av. Arequipa 1800", "(01) 265-3300", "Lun a Sáb 08:00–18:00", listOf(1, 2, 3, 8)),
        Sede(10, "Sede Pueblo Libre", "Pueblo Libre", "Av. Bolívar 900", "(01) 463-1100", "Lun a Vie 08:00–17:00", listOf(1, 2, 5, 7)),
        Sede(11, "Sede Barranco", "Barranco", "Av. Almirante Grau 300", "(01) 247-9900", "Lun a Vie 08:00–18:00", listOf(1, 3, 5)),
        Sede(12, "Sede Ate", "Ate", "Carretera Central Km. 7.5", "(01) 351-2000", "Lun a Sáb 08:00–19:00", listOf(1, 2, 4, 6))
    )

    val especialidades = listOf(
        Especialidad(1, "Medicina General", "Atención integral y prevención", destacada = true),
        Especialidad(2, "Pediatría", "Salud integral para niños y adolescentes", destacada = true),
        Especialidad(3, "Ginecología", "Cuidado integral de la salud de la mujer", destacada = true),
        Especialidad(4, "Cardiología", "Salud del corazón y sistema cardiovascular", destacada = true),
        Especialidad(5, "Dermatología", "Cuidado de piel, cabello y uñas", destacada = true),
        Especialidad(6, "Traumatología", "Tratamiento de huesos, músculos y articulaciones"),
        Especialidad(7, "Oftalmología", "Salud visual y examen de la vista"),
        Especialidad(8, "Neurología", "Sistema nervioso, cerebro y columna")
    )

    val medicos = listOf(
        // --- Medicina General (Especialidad 1) ---
        Medico(1, "Dr. Carlos Mendoza", 1, 1, "Médico general", "CMP: 23456", 4.8, 140, "Disponible hoy", false, "Consulta presencial", "Av. Benavides 456, Miraflores", "(01) 241-5001", setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "08:00 - 16:00", "https://randomuser.me/api/portraits/men/32.jpg"),
        Medico(2, "Dra. Lucía Fernández", 1, 1, "Médica general", "CMP: 34567", 4.6, 98, "Disponible mañana", true, "Consulta presencial", "Av. Benavides 456, Miraflores", "(01) 241-5002", setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY), "09:00 - 15:00", "https://randomuser.me/api/portraits/women/44.jpg"),
        Medico(3, "Dr. Jorge Salazar", 1, 2, "Médico general", "CMP: 45678", 4.5, 64, "Disponible esta semana", false, "Consulta presencial", "Av. Camino Real 123, San Isidro", "(01) 442-3001", setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY), "08:00 - 14:00", "https://randomuser.me/api/portraits/men/45.jpg"),
        Medico(21, "Dra. Carmen Ríos", 1, 3, "Médica general", "CMP: 46101", 4.7, 88, "Disponible hoy", true, "Consulta presencial", "Av. Primavera 789, Surco", "(01) 372-9001", setOf(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), "10:00 - 18:00", "https://randomuser.me/api/portraits/women/68.jpg"),
        Medico(22, "Dr. Roberto Gómez", 1, 4, "Médico general", "CMP: 47202", 4.4, 52, "Disponible mañana", false, "Consulta presencial", "Av. La Molina 555, La Molina", "(01) 348-1101", setOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY), "08:00 - 12:00", "https://randomuser.me/api/portraits/men/22.jpg"),
        Medico(23, "Dra. Patricia Alva", 1, 5, "Médica general", "CMP: 48303", 4.9, 110, "Disponible hoy", true, "Consulta presencial", "Av. Javier Prado Este 2100, San Borja", "(01) 475-6001", setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), "08:00 - 16:00", "https://randomuser.me/api/portraits/women/33.jpg"),

        // --- Pediatría (Especialidad 2) ---
        Medico(4, "Dra. Sofía Paredes", 2, 1, "Pediatra", "CMP: 22334", 4.9, 156, "Disponible hoy", true, "Consulta presencial", "Av. Benavides 456, Miraflores", "(01) 241-5003", setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY), "08:00 - 14:00", "https://randomuser.me/api/portraits/women/65.jpg"),
        Medico(5, "Dr. Andrés Quispe", 2, 3, "Pediatra", "CMP: 33445", 4.7, 102, "Disponible mañana", false, "Consulta presencial", "Av. Primavera 789, Surco", "(01) 372-9002", setOf(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY), "09:00 - 17:00", "https://randomuser.me/api/portraits/men/55.jpg"),
        Medico(6, "Dra. Valeria Castro", 2, 4, "Pediatra", "CMP: 44556", 4.4, 57, "Disponible esta semana", true, "Consulta presencial", "Av. La Molina 555, La Molina", "(01) 348-1102", setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), "13:00 - 19:00", "https://randomuser.me/api/portraits/women/12.jpg"),
        Medico(24, "Dr. Manuel Paredes", 2, 6, "Pediatra", "CMP: 49404", 4.6, 75, "Disponible hoy", false, "Consulta presencial", "Av. Brasil 1450, Jesús María", "(01) 431-2201", setOf(DayOfWeek.TUESDAY, DayOfWeek.FRIDAY), "08:00 - 13:00", "https://randomuser.me/api/portraits/men/11.jpg"),
        Medico(25, "Dra. Teresa Morales", 2, 7, "Pediatra", "CMP: 50505", 4.8, 92, "Disponible hoy", true, "Consulta presencial", "Av. Carlos Izaguirre 800, Los Olivos", "(01) 521-4401", setOf(DayOfWeek.MONDAY, DayOfWeek.THURSDAY), "09:00 - 16:00", "https://randomuser.me/api/portraits/women/29.jpg"),

        // --- Ginecología (Especialidad 3) ---
        Medico(7, "Dra. Ana Torres", 3, 1, "Ginecóloga", "CMP: 12345", 4.9, 120, "Disponible hoy", true, "Consulta presencial", "Av. Benavides 456, Miraflores", "(01) 241-5004", setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "08:00 - 15:00", "https://randomuser.me/api/portraits/women/50.jpg"),
        Medico(8, "Dra. Claudia Rojas", 3, 2, "Ginecóloga", "CMP: 12890", 4.8, 95, "Disponible mañana", true, "Consulta presencial", "Av. Camino Real 123, San Isidro", "(01) 442-3002", setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), "10:00 - 18:00", "https://randomuser.me/api/portraits/women/51.jpg"),
        Medico(9, "Dr. Luis Ramírez", 3, 6, "Ginecólogo", "CMP: 13456", 4.7, 88, "Disponible hoy", false, "Consulta presencial", "Av. Brasil 1450, Jesús María", "(01) 431-2202", setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY), "08:00 - 14:00", "https://randomuser.me/api/portraits/men/52.jpg"),
        Medico(10, "Dra. Mariana Soto", 3, 8, "Ginecóloga", "CMP: 14567", 4.6, 76, "Disponible esta semana", true, "Consulta presencial", "Av. La Marina 2300, San Miguel", "(01) 566-7801", setOf(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY), "09:00 - 16:00", "https://randomuser.me/api/portraits/women/53.jpg"),
        Medico(26, "Dr. Hugo Benites", 3, 9, "Ginecólogo", "CMP: 51606", 4.5, 60, "Disponible hoy", false, "Consulta presencial", "Av. Arequipa 1800, Lince", "(01) 265-3301", setOf(DayOfWeek.MONDAY, DayOfWeek.THURSDAY), "08:00 - 13:00", "https://randomuser.me/api/portraits/men/54.jpg"),

        // --- Cardiología (Especialidad 4) ---
        Medico(11, "Dr. Ricardo Vargas", 4, 1, "Cardiólogo", "CMP: 15678", 4.9, 180, "Disponible mañana", false, "Consulta presencial", "Av. Benavides 456, Miraflores", "(01) 241-5005", setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), "08:00 - 16:00", "https://randomuser.me/api/portraits/men/60.jpg"),
        Medico(12, "Dra. Patricia Núñez", 4, 2, "Cardióloga", "CMP: 16789", 4.7, 110, "Disponible hoy", true, "Consulta presencial", "Av. Camino Real 123, San Isidro", "(01) 442-3003", setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "09:00 - 17:00", "https://randomuser.me/api/portraits/women/61.jpg"),
        Medico(27, "Dr. Samuel Espinoza", 4, 5, "Cardiólogo", "CMP: 52707", 4.8, 130, "Disponible hoy", false, "Consulta presencial", "Av. Javier Prado Este 2100, San Borja", "(01) 475-6002", setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY), "08:00 - 14:00", "https://randomuser.me/api/portraits/men/62.jpg"),
        Medico(28, "Dra. Beatriz León", 4, 8, "Cardióloga", "CMP: 53808", 4.6, 85, "Disponible mañana", true, "Consulta presencial", "Av. La Marina 2300, San Miguel", "(01) 566-7802", setOf(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), "10:00 - 18:00", "https://randomuser.me/api/portraits/women/63.jpg"),

        // --- Dermatología (Especialidad 5) ---
        Medico(13, "Dra. Camila Herrera", 5, 1, "Dermatóloga", "CMP: 17890", 4.8, 132, "Disponible hoy", true, "Consulta presencial", "Av. Benavides 456, Miraflores", "(01) 241-5006", setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "08:00 - 14:00", "https://randomuser.me/api/portraits/women/70.jpg"),
        Medico(14, "Dr. Diego Chávez", 5, 3, "Dermatólogo", "CMP: 18901", 4.5, 70, "Disponible esta semana", false, "Consulta presencial", "Av. Primavera 789, Surco", "(01) 372-9003", setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), "12:00 - 18:00", "https://randomuser.me/api/portraits/men/71.jpg"),
        Medico(29, "Dra. Karen Vega", 5, 6, "Dermatóloga", "CMP: 54909", 4.9, 145, "Disponible hoy", true, "Consulta presencial", "Av. Brasil 1450, Jesús María", "(01) 431-2203", setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY), "09:00 - 15:00", "https://randomuser.me/api/portraits/women/72.jpg"),
        Medico(30, "Dr. Ernesto Paz", 5, 10, "Dermatólogo", "CMP: 55010", 4.3, 45, "Disponible mañana", false, "Consulta presencial", "Av. Bolívar 900, Pueblo Libre", "(01) 463-1101", setOf(DayOfWeek.TUESDAY, DayOfWeek.FRIDAY), "08:00 - 13:00", "https://randomuser.me/api/portraits/men/73.jpg"),

        // --- Traumatología (Especialidad 6) ---
        Medico(15, "Dr. Martín Gutiérrez", 6, 1, "Traumatólogo", "CMP: 19012", 4.8, 125, "Disponible mañana", false, "Consulta presencial", "Av. Benavides 456, Miraflores", "(01) 241-5007", setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY), "08:00 - 16:00", "https://randomuser.me/api/portraits/men/80.jpg"),
        Medico(16, "Dra. Elena Ríos", 6, 3, "Traumatóloga", "CMP: 20123", 4.6, 81, "Disponible hoy", true, "Consulta presencial", "Av. Primavera 789, Surco", "(01) 372-9004", setOf(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY), "09:00 - 17:00", "https://randomuser.me/api/portraits/women/81.jpg"),
        Medico(31, "Dr. Renzo Cárdenas", 6, 5, "Traumatólogo", "CMP: 56111", 4.7, 99, "Disponible hoy", false, "Consulta presencial", "Av. Javier Prado Este 2100, San Borja", "(01) 475-6003", setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "08:00 - 15:00", "https://randomuser.me/api/portraits/men/82.jpg"),
        Medico(32, "Dr. Víctor Navarro", 6, 7, "Traumatólogo", "CMP: 57212", 4.5, 68, "Disponible esta semana", false, "Consulta presencial", "Av. Carlos Izaguirre 800, Los Olivos", "(01) 521-4402", setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), "10:00 - 18:00", "https://randomuser.me/api/portraits/men/83.jpg"),

        // --- Oftalmología (Especialidad 7) ---
        Medico(17, "Dra. Gabriela Medina", 7, 1, "Oftalmóloga", "CMP: 21234", 4.7, 93, "Disponible hoy", true, "Consulta presencial", "Av. Benavides 456, Miraflores", "(01) 241-5008", setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "08:00 - 14:00", "https://randomuser.me/api/portraits/women/90.jpg"),
        Medico(18, "Dr. Fernando Silva", 7, 3, "Oftalmólogo", "CMP: 22345", 4.4, 49, "Disponible esta semana", false, "Consulta presencial", "Av. Primavera 789, Surco", "(01) 372-9005", setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), "14:00 - 19:00", "https://randomuser.me/api/portraits/men/91.jpg"),
        Medico(33, "Dra. Monica Yáñez", 7, 7, "Oftalmóloga", "CMP: 58313", 4.9, 112, "Disponible hoy", true, "Consulta presencial", "Av. Carlos Izaguirre 800, Los Olivos", "(01) 521-4403", setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY), "09:00 - 16:00", "https://randomuser.me/api/portraits/women/92.jpg"),
        Medico(34, "Dr. Javier Solís", 7, 10, "Oftalmólogo", "CMP: 59414", 4.6, 70, "Disponible mañana", false, "Consulta presencial", "Av. Bolívar 900, Pueblo Libre", "(01) 463-1102", setOf(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "08:00 - 14:00", "https://randomuser.me/api/portraits/men/93.jpg"),

        // --- Neurología (Especialidad 8) ---
        Medico(19, "Dr. Alberto Campos", 8, 1, "Neurólogo", "CMP: 23457", 4.8, 115, "Disponible mañana", false, "Consulta presencial", "Av. Benavides 456, Miraflores", "(01) 241-5009", setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), "08:00 - 16:00", "https://randomuser.me/api/portraits/men/18.jpg"),
        Medico(20, "Dra. Rosa Delgado", 8, 2, "Neuróloga", "CMP: 24568", 4.6, 66, "Disponible esta semana", true, "Consulta presencial", "Av. Camino Real 123, San Isidro", "(01) 442-3004", setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), "09:00 - 15:00", "https://randomuser.me/api/portraits/women/19.jpg"),
        Medico(35, "Dr. Gonzalo Huamán", 8, 5, "Neurólogo", "CMP: 60515", 4.9, 134, "Disponible hoy", false, "Consulta presencial", "Av. Javier Prado Este 2100, San Borja", "(01) 475-6004", setOf(DayOfWeek.MONDAY, DayOfWeek.THURSDAY), "08:00 - 14:00", "https://randomuser.me/api/portraits/men/25.jpg"),
        Medico(36, "Dra. Cecilia Montero", 8, 9, "Neuróloga", "CMP: 61616", 4.7, 88, "Disponible hoy", true, "Consulta presencial", "Av. Arequipa 1800, Lince", "(01) 265-3302", setOf(DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY), "09:00 - 17:00", "https://randomuser.me/api/portraits/women/26.jpg")
    )

    /** Horarios base de atención (turnos de 30 minutos). */
    val horariosBase = listOf(
        "08:00", "08:30", "09:00",
        "09:30", "10:00", "10:30",
        "11:00", "11:30", "12:00",
        "15:00", "15:30", "16:00"
    )

    /** Próximo día hábil desde mañana: así la cita de ejemplo siempre aparece en el calendario dinámico. */
    private val fechaEjemplo: String = generateSequence(LocalDate.now().plusDays(1)) { it.plusDays(1) }
        .first { it.dayOfWeek != DayOfWeek.SATURDAY && it.dayOfWeek != DayOfWeek.SUNDAY }
        .toString()

    /** Citas de ejemplo: esos horarios aparecen bloqueados. */
    val citas = mutableStateListOf(
        Cita(1, usuarioId = 2, medicoId = 7, especialidadId = 3, sedeId = 1, fecha = fechaEjemplo, hora = "09:00", motivo = "Control anual"),
        Cita(2, usuarioId = 2, medicoId = 7, especialidadId = 3, sedeId = 1, fecha = fechaEjemplo, hora = "10:30", motivo = "Resultados")
    )

    private var siguienteUsuarioId = 3
    private var siguienteCitaId = 3

    // ------------------------------------------------------------------
    // Usuarios y sesión
    // ------------------------------------------------------------------

    fun registrarUsuario(nombre: String, telefono: String, correo: String, password: String): Boolean {
        val correoLimpio = correo.trim()
        val existe = usuarios.any {
            it.telefono == telefono.trim() ||
                (correoLimpio.isNotEmpty() && it.correo.equals(correoLimpio, ignoreCase = true))
        }
        if (existe) return false

        val nuevo = Usuario(
            id = siguienteUsuarioId++,
            nombre = nombre.trim(),
            telefono = telefono.trim(),
            correo = correoLimpio,
            password = password
        )
        usuarios.add(nuevo)
        usuarioActual = nuevo
        return true
    }

    fun iniciarSesion(usuario: String, password: String): Boolean {
        val dato = usuario.trim()
        val encontrado = usuarios.find {
            (it.telefono == dato || (it.correo.isNotEmpty() && it.correo.equals(dato, ignoreCase = true))) &&
                it.password == password
        }
        usuarioActual = encontrado
        return encontrado != null
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    // ------------------------------------------------------------------
    // Sedes
    // ------------------------------------------------------------------

    fun buscarSedes(texto: String): List<Sede> {
        val filtro = texto.trim()
        if (filtro.isEmpty()) return sedes
        return sedes.filter {
            it.nombre.contains(filtro, ignoreCase = true) ||
                it.distrito.contains(filtro, ignoreCase = true) ||
                it.direccion.contains(filtro, ignoreCase = true)
        }
    }

    fun obtenerSede(id: Int): Sede? {
        return sedes.find { it.id == id }
    }

    // ------------------------------------------------------------------
    // Especialidades por Sede
    // ------------------------------------------------------------------

    fun especialidadesPorSede(sedeId: Int): List<Especialidad> {
        val sede = obtenerSede(sedeId) ?: return especialidades
        return especialidades.filter { it.id in sede.especialidadesIds }
    }

    fun buscarEspecialidades(texto: String): List<Especialidad> {
        val filtro = texto.trim()
        if (filtro.isEmpty()) return especialidades
        return especialidades.filter {
            it.nombre.contains(filtro, ignoreCase = true) ||
                it.descripcion.contains(filtro, ignoreCase = true)
        }
    }

    fun especialidadesDestacadas(cantidad: Int = 5): List<Especialidad> {
        return especialidades.filter { it.destacada }.take(cantidad)
    }

    fun obtenerEspecialidad(id: Int): Especialidad? {
        return especialidades.find { it.id == id }
    }

    // ------------------------------------------------------------------
    // Médicos
    // ------------------------------------------------------------------

    fun obtenerMedico(id: Int): Medico? {
        return medicos.find { it.id == id }
    }

    fun obtenerCita(id: Int): Cita? {
        return citas.find { it.id == id }
    }

    fun medicosPorSedeYEspecialidad(sedeId: Int, especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.sedeId == sedeId && it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    fun medicosPorSede(sedeId: Int): List<Medico> {
        return medicos
            .filter { it.sedeId == sedeId }
            .sortedByDescending { it.calificacion }
    }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> {
        val filtro = texto.trim()
        return medicos
            .filter { it.especialidadId == especialidadId && it.nombre.contains(filtro, ignoreCase = true) }
            .sortedByDescending { it.calificacion }
    }

    // ------------------------------------------------------------------
    // Horarios y citas
    // ------------------------------------------------------------------

    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val medico = obtenerMedico(medicoId) ?: return emptyList()
        val localDate = try {
            LocalDate.parse(fecha)
        } catch (e: Exception) {
            return emptyList()
        }

        if (localDate.dayOfWeek !in medico.diasAtencion) {
            return emptyList()
        }

        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }

        val esHoy = localDate == LocalDate.now()
        val horaActual = LocalTime.now()

        return horariosBase.filter { horaStr ->
            if (horaStr in ocupados) return@filter false
            if (esHoy) {
                val horaSlot = try {
                    LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"))
                } catch (e: Exception) {
                    null
                }
                if (horaSlot != null && horaSlot.isBefore(horaActual)) {
                    return@filter false
                }
            }
            true
        }
    }

    fun agendarCita(medicoId: Int, sedeId: Int, fecha: String, hora: String, motivo: String): Cita? {
        val usuario = usuarioActual ?: return null
        val medico = obtenerMedico(medicoId) ?: return null
        
        val ocupadoMedico = citas.any { it.medicoId == medicoId && it.fecha == fecha && it.hora == hora }
        if (ocupadoMedico) return null

        val ocupadoPaciente = citas.any { it.usuarioId == usuario.id && it.fecha == fecha && it.hora == hora }
        if (ocupadoPaciente) return null

        val cita = Cita(
            id = siguienteCitaId++,
            usuarioId = usuario.id,
            medicoId = medicoId,
            especialidadId = medico.especialidadId,
            sedeId = sedeId,
            fecha = fecha,
            hora = hora,
            motivo = motivo.trim(),
            tipoAtencion = medico.tipoAtencion
        )
        citas.add(cita)
        return cita
    }

    fun citasDelUsuario(): List<Cita> {
        val usuario = usuarioActual ?: return emptyList()
        return citas
            .filter { it.usuarioId == usuario.id }
            .sortedWith(compareBy<Cita>({ it.fecha }, { it.hora }))
    }

    fun cancelarCita(citaId: Int): Boolean {
        return citas.removeIf { it.id == citaId }
    }
}

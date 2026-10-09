package com.saludplus.citas.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario

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

    val especialidades = listOf(
        Especialidad(1, "Medicina General", "Atención integral", destacada = true),
        Especialidad(2, "Pediatría", "Niños y adolescentes", destacada = true),
        Especialidad(3, "Ginecología", "Salud de la mujer", destacada = true),
        Especialidad(4, "Cardiología", "Corazón y vasos sanguíneos", destacada = true),
        Especialidad(5, "Dermatología", "Piel, cabello y uñas", destacada = true),
        Especialidad(6, "Traumatología", "Huesos y articulaciones"),
        Especialidad(7, "Oftalmología", "Salud visual"),
        Especialidad(8, "Neurología", "Cerebro y sistema nervioso")
    )

    val medicos = listOf(
        // Medicina General
        Medico(1, "Dr. Carlos Mendoza", 1, "Médico general", "CMP: 23456", 4.8, 140, "Disponible hoy", esMujer = false),
        Medico(2, "Dra. Lucía Fernández", 1, "Médica general", "CMP: 34567", 4.6, 98, "Disponible mañana", esMujer = true),
        Medico(3, "Dr. Jorge Salazar", 1, "Médico general", "CMP: 45678", 4.5, 64, "Disponible esta semana", esMujer = false),
        // Pediatría
        Medico(4, "Dra. Sofía Paredes", 2, "Pediatra", "CMP: 22334", 4.9, 156, "Disponible hoy", esMujer = true),
        Medico(5, "Dr. Andrés Quispe", 2, "Pediatra", "CMP: 33445", 4.7, 102, "Disponible mañana", esMujer = false),
        Medico(6, "Dra. Valeria Castro", 2, "Pediatra", "CMP: 44556", 4.4, 57, "Disponible esta semana", esMujer = true),
        // Ginecología
        Medico(7, "Dra. Ana Torres", 3, "Ginecóloga", "CMP: 12345", 4.9, 120, "Disponible hoy", esMujer = true),
        Medico(8, "Dra. Claudia Rojas", 3, "Ginecóloga", "CMP: 12890", 4.8, 95, "Disponible mañana", esMujer = true),
        Medico(9, "Dr. Luis Ramírez", 3, "Ginecólogo", "CMP: 13456", 4.7, 88, "Disponible hoy", esMujer = false),
        Medico(10, "Dra. Mariana Soto", 3, "Ginecóloga", "CMP: 14567", 4.6, 76, "Disponible esta semana", esMujer = true),
        // Cardiología
        Medico(11, "Dr. Ricardo Vargas", 4, "Cardiólogo", "CMP: 15678", 4.9, 180, "Disponible mañana", esMujer = false),
        Medico(12, "Dra. Patricia Núñez", 4, "Cardióloga", "CMP: 16789", 4.7, 110, "Disponible hoy", esMujer = true),
        // Dermatología
        Medico(13, "Dra. Camila Herrera", 5, "Dermatóloga", "CMP: 17890", 4.8, 132, "Disponible hoy", esMujer = true),
        Medico(14, "Dr. Diego Chávez", 5, "Dermatólogo", "CMP: 18901", 4.5, 70, "Disponible esta semana", esMujer = false),
        // Traumatología
        Medico(15, "Dr. Martín Gutiérrez", 6, "Traumatólogo", "CMP: 19012", 4.8, 125, "Disponible mañana", esMujer = false),
        Medico(16, "Dra. Elena Ríos", 6, "Traumatóloga", "CMP: 20123", 4.6, 81, "Disponible hoy", esMujer = true),
        // Oftalmología
        Medico(17, "Dra. Gabriela Medina", 7, "Oftalmóloga", "CMP: 21234", 4.7, 93, "Disponible hoy", esMujer = true),
        Medico(18, "Dr. Fernando Silva", 7, "Oftalmólogo", "CMP: 22345", 4.4, 49, "Disponible esta semana", esMujer = false),
        // Neurología
        Medico(19, "Dr. Alberto Campos", 8, "Neurólogo", "CMP: 23457", 4.8, 115, "Disponible mañana", esMujer = false),
        Medico(20, "Dra. Rosa Delgado", 8, "Neuróloga", "CMP: 24568", 4.6, 66, "Disponible esta semana", esMujer = true)
    )

    /** Horarios de atención (turnos de 30 minutos). */
    val horariosBase = listOf(
        "08:00", "08:30", "09:00",
        "09:30", "10:00", "10:30",
        "11:00", "11:30", "12:00",
        "15:00", "15:30", "16:00"
    )

    /** Citas de ejemplo de otra paciente: esos horarios aparecen bloqueados. */
    val citas = mutableStateListOf(
        Cita(1, usuarioId = 2, medicoId = 7, especialidadId = 3, fecha = "2026-10-13", hora = "09:00", motivo = "Control anual"),
        Cita(2, usuarioId = 2, medicoId = 7, especialidadId = 3, fecha = "2026-10-13", hora = "10:30", motivo = "Resultados")
    )

    private var siguienteUsuarioId = 3
    private var siguienteCitaId = 3

    // ------------------------------------------------------------------
    // Usuarios y sesión
    // ------------------------------------------------------------------

    /** Registra un usuario nuevo si su teléfono/correo no existe. Deja la sesión iniciada. */
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

    /** Inicia sesión con teléfono o correo + contraseña. */
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
    // Especialidades y médicos
    // ------------------------------------------------------------------

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

    fun obtenerMedico(id: Int): Medico? {
        return medicos.find { it.id == id }
    }

    fun obtenerCita(id: Int): Cita? {
        // TODO: citas: find por id.
        TODO("Implementar obtenerCita")
    }

    /** Médicos de una especialidad, del mejor al menor calificado. */
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

    /** Horarios libres de un médico en una fecha: se quitan los ya reservados. */
    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        // TODO: citas: filter (médico y fecha) + map a horas; horariosBase: filter de las no ocupadas.
        TODO("Implementar horariosDisponibles")
    }

    /** Crea la cita del usuario en sesión. Devuelve null si el horario ya está tomado. */
    fun agendarCita(medicoId: Int, fecha: String, hora: String, motivo: String): Cita? {
        // TODO: citas: any (horario ocupado) + add. Devolver la cita creada o null.
        TODO("Implementar agendarCita")
    }

    /** Citas del usuario en sesión ordenadas por fecha y hora. */
    fun citasDelUsuario(): List<Cita> {
        // TODO: citas: filter del usuario en sesión + sortedWith (fecha y hora).
        TODO("Implementar citasDelUsuario")
    }

    fun cancelarCita(citaId: Int): Boolean {
        // TODO: citas: removeIf por id.
        TODO("Implementar cancelarCita")
    }
}

package com.floresvalencia.navlab.data

// Las contraseñas en texto plano son solo para fines académicos.
// En producción se utilizaría autenticación con backend y contraseñas hasheadas.
object StudentRepository {

    private val students = listOf(
        Student(
            id = 1,
            name = "Juan León",
            fullName = "Juan León Suiyon",
            career = "Ingeniería de Software",
            cycle = "VI Ciclo",
            studentCode = "2024-0001",
            email = "juan.leon@tecsup.edu.pe",
            password = "Juan2024*",
            phone = "+51 987 654 321",
            faculty = "Ingeniería y Tecnología",
            bio = "Estudiante destacado con interés en desarrollo Android.",
            photo = null
        ),
        Student(
            id = 2,
            name = "María García",
            fullName = "María García Torres",
            career = "Arquitectura",
            cycle = "IV Ciclo",
            studentCode = "2024-0002",
            email = "maria.garcia@tecsup.edu.pe",
            password = "Maria2024*",
            phone = "+51 987 111 222",
            faculty = "Arquitectura y Diseño",
            bio = "Apasionada por el diseño sostenible y maquetación digital.",
            photo = null
        ),
        Student(
            id = 3,
            name = "Carlos Perez",
            fullName = "Carlos Perez Rojas",
            career = "Medicina",
            cycle = "VIII Ciclo",
            studentCode = "2024-0003",
            email = "carlos.perez@tecsup.edu.pe",
            password = "Carlos2024*",
            phone = "+51 987 333 444",
            faculty = "Ciencias de la Salud",
            bio = "Interesado en la investigación clínica y telemedicina.",
            photo = null
        ),
        Student(
            id = 4,
            name = "Ana Lopez",
            fullName = "Ana Lopez Mendoza",
            career = "Derecho",
            cycle = "II Ciclo",
            studentCode = "2024-0004",
            email = "ana.lopez@tecsup.edu.pe",
            password = "Ana2024*",
            phone = "+51 987 555 666",
            faculty = "Ciencias Jurídicas",
            bio = "Entusiasta del derecho corporativo y la innovación legal.",
            photo = null
        ),
        Student(
            id = 5,
            name = "Luis Ramirez",
            fullName = "Luis Ramirez Castillo",
            career = "Administración",
            cycle = "V Ciclo",
            studentCode = "2024-0005",
            email = "luis.ramirez@tecsup.edu.pe",
            password = "Luis2024*",
            phone = "+51 987 777 888",
            faculty = "Negocios y Gestión",
            bio = "Enfocado en gestión de proyectos y analítica de negocios.",
            photo = null
        )
    )

    fun getAll(): List<Student> = students

    fun getById(id: Int): Student? = students.find { it.id == id }

    fun findByCredentials(email: String, password: String): Student? {
        val cleanEmail = email.trim()
        return students.find {
            it.email.equals(cleanEmail, ignoreCase = true) && it.password == password
        }
    }
}

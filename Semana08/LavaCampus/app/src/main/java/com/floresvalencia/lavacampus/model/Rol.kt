package com.floresvalencia.lavacampus.model

/** Perfil del usuario. Se guarda en la tabla como texto (Rol.name). */
enum class Rol(val etiqueta: String) {
    CLIENTE("Estudiante"),
    ADMIN("Administrador");

    companion object {
        /** Convierte el texto guardado en Room o DataStore; si no coincide devuelve null. */
        fun desde(texto: String?): Rol? = entries.firstOrNull { it.name == texto }
    }
}

package com.floresvalencia.lavacampus.model

/** Casilleros fisicos de recojo: C-01 a C-30. */
object Casilleros {
    val todos: List<String> = (1..30).map { "C-" + it.toString().padStart(2, '0') }

    fun esValido(c: String?): Boolean = c != null && c in todos
}

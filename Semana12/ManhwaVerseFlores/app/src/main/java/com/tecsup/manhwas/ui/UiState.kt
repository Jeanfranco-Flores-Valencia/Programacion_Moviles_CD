package com.tecsup.manhwas.ui

/** Estado de una pantalla: cargando, con datos o con un error para mostrar. */
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

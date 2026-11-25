package com.tecsup.murocampus.viewmodel

import java.io.IOException

/** Estado de una pantalla que espera datos de la red. */
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

/** Convierte una excepcion en un mensaje para el usuario. */
fun mensajeDe(e: Throwable): String = when (e) {
    is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
    // Paso 1: con Retrofit y kotlinx.serialization se agregan HttpException y SerializationException
    else -> "Error desconocido"
}

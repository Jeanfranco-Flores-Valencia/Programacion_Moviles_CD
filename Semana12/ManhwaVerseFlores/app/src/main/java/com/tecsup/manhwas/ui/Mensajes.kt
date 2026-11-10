package com.tecsup.manhwas.ui

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

/** Mensaje claro para cada tipo de error de red. */
fun mensajeDe(e: Throwable): String = when (e) {
    is IOException -> "No hay conexión a internet"
    is HttpException -> if (e.code() == 429) "La API recibió muchas peticiones. Intenta en unos segundos"
                        else "El servidor respondió con error ${e.code()}"
    is SerializationException -> "La respuesta de la API cambió de formato"
    else -> "Ocurrió un error inesperado"
}

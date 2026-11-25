package com.tecsup.murocampus.data.remote

import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/** Crea Retrofit una sola vez (object + by lazy) y lo comparte en toda la app. */
object RetrofitClient {

    private const val URL_BASE = "https://jsonplaceholder.typicode.com/"

    // Si el servidor agrega un campo que no esta en la data class, se ignora en vez de fallar
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Mejora IA: agrega el encabezado "Accept: application/json" a todas las peticiones, para pedir
     * siempre JSON sin repetirlo en cada funcion de PostApi.
     */
    private val encabezadoJson = Interceptor { cadena ->
        val peticion = cadena.request().newBuilder()
            .header("Accept", "application/json")
            .build()
        cadena.proceed(peticion)
    }

    // Muestra en Logcat (filtro OkHttp) cada peticion y respuesta. Solo para desarrollo.
    // Va despues del encabezado para que el registro muestre la peticion ya con el Accept
    private val cliente = OkHttpClient.Builder()
        .addInterceptor(encabezadoJson)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    val api: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(cliente)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PostApi::class.java)
    }
}

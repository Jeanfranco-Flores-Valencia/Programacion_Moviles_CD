package com.tecsup.murocampus.data.remote

import kotlinx.serialization.json.Json
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

    // Muestra en Logcat (filtro OkHttp) cada peticion y respuesta. Solo para desarrollo
    private val cliente = OkHttpClient.Builder()
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

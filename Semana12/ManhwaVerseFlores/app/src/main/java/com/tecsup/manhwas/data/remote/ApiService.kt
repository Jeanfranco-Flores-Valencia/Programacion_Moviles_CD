package com.tecsup.manhwas.data.remote

import com.tecsup.manhwas.data.remote.dto.RespuestaDetalleDto
import com.tecsup.manhwas.data.remote.dto.RespuestaListaDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** Endpoints de Jikan v4. */
interface ApiService {

    /**
     * Lista: manhwas (comics coreanos) de Accion (1) y Fantasia (10), los mas populares primero.
     * Ahi estan los de sistemas, mazmorras, monstruos y apocalipsis.
     */
    @GET("manga")
    suspend fun listar(
        @Query("type") tipo: String = "manhwa",
        @Query("genres") generos: String = "1,10",
        @Query("order_by") orden: String = "members",
        @Query("sort") sentido: String = "desc",
        @Query("sfw") sinContenidoAdulto: Boolean = true,
        @Query("limit") limite: Int = 25
    ): RespuestaListaDto

    /** Detalle: segundo endpoint, un manhwa por su id. */
    @GET("manga/{id}")
    suspend fun detalle(@Path("id") id: Int): RespuestaDetalleDto
}

package com.tecsup.manhwas.data.remote

import com.tecsup.manhwas.data.remote.dto.RespuestaDetalleDto
import com.tecsup.manhwas.data.remote.dto.RespuestaListaDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** Endpoints de Kitsu (https://kitsu.io/api/edge/). */
interface ApiService {

    /**
     * Lista: manhwas (comics coreanos) de Accion y Fantasia, los mas leidos primero.
     * Ahi estan los de sistemas, mazmorras, monstruos y apocalipsis (Solo Leveling, Dungeon Reset...).
     */
    @GET("manga")
    suspend fun listar(
        @Query("filter[subtype]") tipo: String = "manhwa",
        @Query("filter[categories]") categorias: String = "action,fantasy",
        @Query("sort") orden: String = "-userCount",
        @Query("page[limit]") limite: Int = 20,          // maximo que permite Kitsu
        @Query("include") incluir: String = "categories",
        @Query("fields[categories]") campos: String = "title"
    ): RespuestaListaDto

    /** Detalle: segundo endpoint, un manhwa por su id. */
    @GET("manga/{id}")
    suspend fun detalle(
        @Path("id") id: Int,
        @Query("include") incluir: String = "categories",
        @Query("fields[categories]") campos: String = "title"
    ): RespuestaDetalleDto
}

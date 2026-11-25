package com.tecsup.murocampus.data.remote

import com.tecsup.murocampus.model.CambioTitulo
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Llamadas al servicio REST. Retrofit genera el codigo: aqui solo se describen.
 * Las rutas no empiezan con "/" para no reemplazar el camino de la URL base.
 */
interface PostApi {

    /** GET posts: las 100 publicaciones. */
    @GET("posts")
    suspend fun listar(): List<Post>

    /** GET posts/{id}: @Path pone el id dentro de la ruta. */
    @GET("posts/{id}")
    suspend fun detalle(@Path("id") id: Int): Post

    /** GET posts/{id}/comments */
    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") id: Int): List<Comentario>

    /** GET posts?userId=2: @Query agrega el parametro despues del "?". */
    @GET("posts")
    suspend fun porUsuario(@Query("userId") userId: Int): List<Post>

    /** POST posts: @Body envia el objeto como JSON en el cuerpo. */
    @POST("posts")
    suspend fun crear(@Body nuevo: NuevoPost): Post

    /** Mejora IA: PATCH posts/{id} cambia solo los campos enviados y devuelve la publicacion completa. */
    @PATCH("posts/{id}")
    suspend fun editarTitulo(@Path("id") id: Int, @Body cambio: CambioTitulo): Post

    /** Mejora IA: DELETE posts/{id}. La respuesta llega vacia: Response permite revisar el codigo. */
    @DELETE("posts/{id}")
    suspend fun eliminar(@Path("id") id: Int): Response<Unit>
}

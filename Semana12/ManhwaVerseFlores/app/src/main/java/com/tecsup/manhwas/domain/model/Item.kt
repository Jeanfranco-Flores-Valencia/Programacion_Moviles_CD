package com.tecsup.manhwas.domain.model

/**
 * Modelo que usa la UI: un manhwa. No sabe nada de Retrofit ni de Room;
 * los mappers lo arman desde ItemDto (API) o ItemEntity (Room).
 */
data class Item(
    val id: Int,
    val titulo: String,
    val subtitulo: String,          // generos principales, ej. "Acción · Fantasía · Mazmorras"
    val imagenUrl: String?,
    val puntaje: Double?,
    val capitulos: Int?,
    val estado: String,             // "En publicación", "Finalizado"...
    val sinopsis: String = "",
    val publicadoEn: String = "",       // plataforma donde se publica, ej. "KakaoPage"
    val esFavorito: Boolean = false
)

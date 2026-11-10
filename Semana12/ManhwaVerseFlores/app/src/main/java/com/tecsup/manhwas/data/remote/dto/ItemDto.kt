package com.tecsup.manhwas.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Lo que responde Jikan (API publica de MyAnimeList, sin clave).
 * La lista viene envuelta en { "data": [...] } y el detalle en { "data": {...} }.
 * Todos los campos tienen valor por defecto: si la API no manda uno, la app no se cae.
 */
@Serializable
data class RespuestaListaDto(val data: List<ItemDto> = emptyList())

@Serializable
data class RespuestaDetalleDto(val data: ItemDto)

@Serializable
data class ItemDto(
    @SerialName("mal_id") val id: Int,
    val title: String = "",
    @SerialName("title_english") val tituloIngles: String? = null,
    val images: ImagenesDto? = null,
    val synopsis: String? = null,
    val score: Double? = null,
    val chapters: Int? = null,
    val status: String? = null,
    val members: Int? = null,
    val genres: List<NombreDto> = emptyList(),
    val themes: List<NombreDto> = emptyList(),
    val authors: List<NombreDto> = emptyList()
)

@Serializable
data class ImagenesDto(val jpg: ImagenDto? = null)

@Serializable
data class ImagenDto(
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("large_image_url") val largeImageUrl: String? = null
)

@Serializable
data class NombreDto(val name: String = "")

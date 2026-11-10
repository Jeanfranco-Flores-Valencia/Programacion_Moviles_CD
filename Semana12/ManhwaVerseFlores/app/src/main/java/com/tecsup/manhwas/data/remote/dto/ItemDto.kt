package com.tecsup.manhwas.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Lo que responde Kitsu (API publica, sin clave), en formato JSON:API:
 * { "data": [ { "id": "54114", "attributes": {...}, "relationships": {...} } ], "included": [...] }
 * Los generos vienen aparte en "included" y cada manhwa solo guarda sus ids.
 * Todos los campos tienen valor por defecto: si la API no manda uno, la app no se cae.
 */
@Serializable
data class RespuestaListaDto(
    val data: List<ItemDto> = emptyList(),
    val included: List<IncluidoDto> = emptyList()
)

@Serializable
data class RespuestaDetalleDto(
    val data: ItemDto,
    val included: List<IncluidoDto> = emptyList()
)

@Serializable
data class ItemDto(
    val id: String,                       // Kitsu manda el id como texto: "54114"
    val attributes: AtributosDto = AtributosDto(),
    val relationships: RelacionesDto? = null
)

@Serializable
data class AtributosDto(
    val canonicalTitle: String = "",
    val titles: Map<String, String?> = emptyMap(),
    val synopsis: String? = null,
    val averageRating: String? = null,    // "84.48" sobre 100
    val chapterCount: Int? = null,
    val status: String? = null,           // current, finished, hiatus...
    val userCount: Int? = null,           // lectores: ordena la lista
    val serialization: String? = null,    // donde se publica, ej. "KakaoPage"
    val posterImage: PosterDto? = null
)

@Serializable
data class PosterDto(
    val small: String? = null,
    val medium: String? = null,
    val large: String? = null
)

@Serializable
data class RelacionesDto(val categories: RelacionDto? = null)

@Serializable
data class RelacionDto(val data: List<ReferenciaDto> = emptyList())

@Serializable
data class ReferenciaDto(val type: String = "", val id: String = "")

/** Elemento de "included": aqui solo se piden las categorias (generos) con su titulo. */
@Serializable
data class IncluidoDto(
    val type: String = "",
    val id: String = "",
    val attributes: AtributosIncluidoDto = AtributosIncluidoDto()
)

@Serializable
data class AtributosIncluidoDto(val title: String? = null)

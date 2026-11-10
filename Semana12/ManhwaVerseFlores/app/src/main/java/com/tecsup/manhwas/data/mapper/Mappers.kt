package com.tecsup.manhwas.data.mapper

import com.tecsup.manhwas.data.local.ItemEntity
import com.tecsup.manhwas.data.remote.dto.ItemDto
import com.tecsup.manhwas.domain.model.Item

/** Generos y temas de la API traducidos (los que no estan se dejan como vienen). */
private val traducciones = mapOf(
    "Action" to "Acción", "Adventure" to "Aventura", "Fantasy" to "Fantasía", "Drama" to "Drama",
    "Comedy" to "Comedia", "Romance" to "Romance", "Mystery" to "Misterio", "Horror" to "Terror",
    "Supernatural" to "Sobrenatural", "Sci-Fi" to "Ciencia ficción", "Sports" to "Deportes",
    "Martial Arts" to "Artes marciales", "Isekai" to "Isekai", "Reincarnation" to "Reencarnación",
    "Gore" to "Gore", "Survival" to "Supervivencia", "Video Game" to "Videojuego",
    "Super Power" to "Superpoderes", "Psychological" to "Psicológico", "Time Travel" to "Viajes en el tiempo",
    "Military" to "Militar", "Mythology" to "Mitología", "Historical" to "Histórico", "School" to "Escolar"
)

private fun estadoEnEspanol(status: String?): String = when (status) {
    "Publishing" -> "En publicación"
    "Finished" -> "Finalizado"
    "On Hiatus" -> "En pausa"
    "Discontinued" -> "Cancelado"
    null, "" -> "Sin dato"
    else -> status
}

/** Quita la firma que MyAnimeList pone al final de la sinopsis. */
private fun limpiarSinopsis(texto: String?): String = texto.orEmpty()
    .lines()
    .filterNot { it.trim().startsWith("[Written by") || it.trim().startsWith("(Source:") }
    .joinToString("\n")
    .trim()

/** Dto (API) -> Entity (Room). */
fun ItemDto.aEntity(esFavorito: Boolean = false): ItemEntity = ItemEntity(
    id = id,
    titulo = tituloIngles?.takeIf { it.isNotBlank() } ?: title,
    subtitulo = (genres + themes).map { traducciones[it.name] ?: it.name }.distinct().take(4).joinToString(" · "),
    imagenUrl = images?.jpg?.largeImageUrl ?: images?.jpg?.imageUrl,
    puntaje = score,
    capitulos = chapters,
    estado = estadoEnEspanol(status),
    sinopsis = limpiarSinopsis(synopsis),
    // Jikan escribe "Apellido, Nombre": se muestra como "Nombre Apellido"
    autores = authors.joinToString(", ") { a -> a.name.split(", ").reversed().joinToString(" ") },
    popularidad = members ?: 0,
    esFavorito = esFavorito
)

/** Entity (Room) -> modelo de la UI. */
fun ItemEntity.aModelo(): Item = Item(
    id = id,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    puntaje = puntaje,
    capitulos = capitulos,
    estado = estado,
    sinopsis = sinopsis,
    autores = autores,
    esFavorito = esFavorito
)

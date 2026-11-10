package com.tecsup.manhwas.data.mapper

import com.tecsup.manhwas.data.local.ItemEntity
import com.tecsup.manhwas.data.remote.dto.IncluidoDto
import com.tecsup.manhwas.data.remote.dto.ItemDto
import com.tecsup.manhwas.domain.model.Item

/** Generos de la API traducidos (los que no estan se dejan como vienen). */
private val traducciones = mapOf(
    "Action" to "Acción", "Adventure" to "Aventura", "Fantasy" to "Fantasía", "Drama" to "Drama",
    "Comedy" to "Comedia", "Romance" to "Romance", "Mystery" to "Misterio", "Horror" to "Terror",
    "Supernatural" to "Sobrenatural", "Science Fiction" to "Ciencia ficción", "Sports" to "Deportes",
    "Martial Arts" to "Artes marciales", "Isekai" to "Isekai", "Reincarnation" to "Reencarnación",
    "Post Apocalypse" to "Apocalipsis", "Demon" to "Demonios", "Monster" to "Monstruos",
    "Magic" to "Magia", "Video Game" to "Videojuego", "Super Power" to "Superpoderes",
    "Psychological" to "Psicológico", "Time Travel" to "Viajes en el tiempo", "Military" to "Militar",
    "Thriller" to "Suspenso", "School Life" to "Escolar", "Slice of Life" to "Vida cotidiana",
    "Gunfights" to "Tiroteos", "Sword and Sorcery" to "Espada y hechicería"
)

/** Generos que no aportan al tema (son publicos objetivo, no generos). */
private val omitidos = setOf("Shounen", "Seinen", "Shoujo", "Josei")

private fun estadoEnEspanol(status: String?): String = when (status) {
    "current" -> "En publicación"
    "finished" -> "Finalizado"
    "hiatus" -> "En pausa"
    "tba", "upcoming", "unreleased" -> "Próximamente"
    null, "" -> "Sin dato"
    else -> status
}

/** Arma el mapa id -> nombre de genero con lo que vino en "included". */
fun List<IncluidoDto>.generos(): Map<String, String> =
    filter { it.type == "categories" }.associate { it.id to (it.attributes.title ?: "") }

/** Dto (API) -> Entity (Room). Necesita los generos de "included" para el subtitulo. */
fun ItemDto.aEntity(generos: Map<String, String>, esFavorito: Boolean = false): ItemEntity {
    val nombres = relationships?.categories?.data.orEmpty()
        .mapNotNull { generos[it.id] }
        .filter { it.isNotBlank() && it !in omitidos }
        .map { traducciones[it] ?: it }
    return ItemEntity(
        id = id.toInt(),
        titulo = attributes.titles["en"]?.takeIf { it.isNotBlank() } ?: attributes.canonicalTitle,
        subtitulo = nombres.distinct().take(4).joinToString(" · "),
        imagenUrl = attributes.posterImage?.large ?: attributes.posterImage?.medium,
        // Kitsu califica sobre 100; se muestra sobre 10 como en la mayoria de apps
        puntaje = attributes.averageRating?.toDoubleOrNull()?.div(10),
        capitulos = attributes.chapterCount,
        estado = estadoEnEspanol(attributes.status),
        sinopsis = attributes.synopsis.orEmpty().substringBefore("(Source:").trim(),
        publicadoEn = attributes.serialization.orEmpty(),
        popularidad = attributes.userCount ?: 0,
        esFavorito = esFavorito
    )
}

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
    publicadoEn = publicadoEn,
    esFavorito = esFavorito
)

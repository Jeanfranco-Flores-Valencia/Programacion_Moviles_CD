package com.tecsup.manhwas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Tabla de Room: lo descargado queda en el celular y se ve sin internet. */
@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val titulo: String,
    val subtitulo: String,
    val imagenUrl: String?,
    val puntaje: Double?,
    val capitulos: Int?,
    val estado: String,
    val sinopsis: String,
    val publicadoEn: String,
    val popularidad: Int,          // lectores en Kitsu: ordena la lista
    val esFavorito: Boolean = false
)

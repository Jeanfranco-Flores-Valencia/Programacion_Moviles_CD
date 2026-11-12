package com.tecsup.manhwas.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.manhwas.domain.model.Item
import com.tecsup.manhwas.ui.theme.Colores

/**
 * Mejora IA: tarjeta del catalogo como en las webs de lectura: portada con puntaje,
 * capitulos y favorito encima, y el titulo y los generos debajo (no sobre la imagen).
 */
@Composable
fun TarjetaManhwa(
    item: Item,
    onClick: () -> Unit,
    onFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(2f / 3f).clip(RoundedCornerShape(12.dp))) {
            Portada(item.imagenUrl, item.titulo, Modifier.matchParentSize())
            item.puntaje?.let { InsigniaPuntaje(it, Modifier.align(Alignment.TopStart).padding(6.dp)) }
            InsigniaCapitulos(item.capitulos, Modifier.align(Alignment.BottomStart).padding(6.dp))
            BotonIcono(
                icono = if (item.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                descripcion = if (item.esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                onClick = onFavorito,
                tinte = if (item.esFavorito) Colores.Rojo else Colores.Texto,
                fondo = Color(0xD90B0B10),
                tamano = 34,
                modifier = Modifier.align(Alignment.TopEnd).padding(6.dp)
            )
        }
        Text(
            item.titulo,
            style = MaterialTheme.typography.titleSmall,
            color = Colores.Texto,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            item.subtitulo.ifBlank { item.estado },
            style = MaterialTheme.typography.labelSmall,
            color = Colores.TextoSuave,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

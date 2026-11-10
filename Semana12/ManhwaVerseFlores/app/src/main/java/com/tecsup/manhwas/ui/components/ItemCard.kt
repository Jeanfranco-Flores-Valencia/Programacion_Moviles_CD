package com.tecsup.manhwas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.manhwas.domain.model.Item
import com.tecsup.manhwas.ui.theme.Colores
import java.util.Locale

/** Portada del manhwa con titulo, generos, puntaje y boton de favorito. */
@Composable
fun ItemCard(
    item: Item,
    onClick: () -> Unit,
    onFavorito: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f / 3f)
            .clip(RoundedCornerShape(16.dp))
            .background(Colores.Superficie)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = item.imagenUrl,
            contentDescription = item.titulo,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Degradado para que el texto se lea sobre cualquier portada
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(0.45f to Color.Transparent, 1f to Color(0xF20B0918)))
        )

        item.puntaje?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xCC0B0918))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = Colores.Dorado, modifier = Modifier.size(14.dp))
                Text(" " + String.format(Locale.US, "%.2f", it), style = MaterialTheme.typography.labelSmall, color = Colores.Texto)
            }
        }

        IconButton(
            onClick = onFavorito,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xCC0B0918))
        ) {
            Icon(
                if (item.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (item.esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                tint = if (item.esFavorito) Colores.Rojo else Colores.Texto,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.align(Alignment.BottomStart).padding(10.dp)) {
            Text(
                item.titulo,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Colores.Texto,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                item.subtitulo,
                style = MaterialTheme.typography.labelSmall,
                color = Colores.Cian,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

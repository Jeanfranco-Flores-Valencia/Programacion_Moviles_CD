package com.tecsup.murocampus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.murocampus.model.Post

/**
 * Mejora IA: publicacion del muro al estilo de una red social. Avatar a la izquierda; autor,
 * titulo, texto y la accion "Ver comentarios" a la derecha. No conoce el ViewModel.
 */
@Composable
fun PublicacionFeed(post: Post, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        AvatarUsuario(post.userId)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(start = 12.dp)) {
            EncabezadoAutor(post.userId, detalle = "#${post.id}")
            Text(post.title, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(
                post.body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                Icon(
                    Icons.AutoMirrored.Outlined.Comment, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp)
                )
                Text(
                    "Ver comentarios",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}

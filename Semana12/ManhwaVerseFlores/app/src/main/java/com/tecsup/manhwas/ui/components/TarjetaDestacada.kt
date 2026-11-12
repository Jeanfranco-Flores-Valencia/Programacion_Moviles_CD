package com.tecsup.manhwas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.manhwas.domain.model.Item
import com.tecsup.manhwas.ui.theme.Colores

/**
 * Mejora IA: el manhwa mas popular arriba del catalogo, como el banner de portada
 * de una web de lectura: la portada de fondo oscurecida y los datos a la derecha.
 */
@Composable
fun TarjetaDestacada(item: Item, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, Colores.Morado.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
    ) {
        // Fondo: la misma portada, tenue, con un degradado negro-morado encima
        Portada(item.imagenUrl, item.titulo, Modifier.matchParentSize().alpha(0.35f))
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Brush.horizontalGradient(listOf(Colores.Fondo.copy(alpha = 0.55f), Colores.MoradoOscuro.copy(alpha = 0.85f))))
        )
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(14.dp)) {
            Portada(
                item.imagenUrl,
                item.titulo,
                Modifier.width(128.dp).height(192.dp).clip(RoundedCornerShape(12.dp))
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Outlined.LocalFireDepartment, contentDescription = null, tint = Colores.Dorado)
                    Text("Más leído", style = MaterialTheme.typography.labelLarge, color = Colores.Dorado)
                }
                Text(
                    item.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    color = Colores.Texto,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    item.subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = Colores.MoradoClaro,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item.puntaje?.let { InsigniaPuntaje(it) }
                    InsigniaEstado(item.estado)
                }
                Text(
                    item.sinopsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = Colores.TextoSuave,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

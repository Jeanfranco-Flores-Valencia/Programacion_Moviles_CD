package com.tecsup.manhwas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.manhwas.ui.theme.Colores
import java.util.Locale

/** Mejora IA: chip de genero o filtro. Seleccionado = morado; si no, gris oscuro. */
@Composable
fun Chip(
    texto: String,
    modifier: Modifier = Modifier,
    seleccionado: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Text(
        texto,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Medium,
        color = if (seleccionado) Color.White else Colores.MoradoClaro,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (seleccionado) Colores.Morado else Colores.Morado.copy(alpha = 0.14f))
            .border(1.dp, if (seleccionado) Colores.Morado else Colores.Morado.copy(alpha = 0.3f), RoundedCornerShape(50))
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(horizontal = 14.dp, vertical = 7.dp)
    )
}

/** Insignia oscura translucida que va sobre la portada. */
@Composable
fun Insignia(modifier: Modifier = Modifier, contenido: @Composable () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xD90B0B10))
            .padding(horizontal = 7.dp, vertical = 3.dp)
    ) { contenido() }
}

/** Puntaje con estrella dorada, ej. ★ 8.45. */
@Composable
fun InsigniaPuntaje(puntaje: Double, modifier: Modifier = Modifier) {
    Insignia(modifier) {
        Icon(Icons.Filled.Star, contentDescription = null, tint = Colores.Dorado, modifier = Modifier.size(13.dp))
        Text(
            String.format(Locale.US, "%.1f", puntaje),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Colores.Texto
        )
    }
}

/** Estado con un punto de color: verde si sigue publicandose, morado si termino. */
@Composable
fun InsigniaEstado(estado: String, modifier: Modifier = Modifier) {
    val color = if (estado == "En publicación") Colores.Verde else Colores.MoradoClaro
    Insignia(modifier) {
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(color))
        Text(estado, style = MaterialTheme.typography.labelSmall, color = Colores.Texto)
    }
}

/** Texto pequeno de capitulos, ej. "Cap. 201". */
@Composable
fun InsigniaCapitulos(capitulos: Int?, modifier: Modifier = Modifier) {
    Insignia(modifier) {
        Text(
            if (capitulos != null) "Cap. $capitulos" else "En curso",
            style = MaterialTheme.typography.labelSmall,
            color = Colores.Texto
        )
    }
}

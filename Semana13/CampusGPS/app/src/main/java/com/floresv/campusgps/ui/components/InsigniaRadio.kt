package com.floresv.campusgps.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Etiqueta redondeada pequena; la usan "Dentro del radio" y "Más cercano". */
@Composable
fun Insignia(texto: String, fondo: Color, color: Color, modifier: Modifier = Modifier) {
    Text(
        texto,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(fondo)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

/** Etiqueta verde "Dentro del radio" que aparece bajo el nombre del lugar. */
@Composable
fun InsigniaRadio(modifier: Modifier = Modifier) {
    Insignia("Dentro del radio", MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer, modifier)
}

/** Etiqueta del lugar mas cercano, con el mismo verde del marcador. */
@Composable
fun InsigniaMasCercano(modifier: Modifier = Modifier) {
    Insignia("Más cercano", Color(0xFF43A047), Color.White, modifier)
}

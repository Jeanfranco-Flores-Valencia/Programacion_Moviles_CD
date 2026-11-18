package com.floresv.centrotareas.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Mejora IA: barra horizontal proporcional al tiempo (fraccion = ms / ms maximo), con etiqueta y valor. */
@Composable
fun BarraTiempo(etiqueta: String, milisegundos: Long, fraccion: Float, color: Color, modifier: Modifier = Modifier) {
    val ancho by animateFloatAsState(fraccion.coerceIn(0.05f, 1f), label = "barra")
    Column(modifier = modifier.fillMaxWidth()) {
        Row {
            Text(etiqueta, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
            Text("$milisegundos ms", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        }
        Box(modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(50)).background(MaterialTheme.colorScheme.surfaceVariant)) {
            Box(modifier = Modifier.fillMaxWidth(ancho).fillMaxHeight().clip(RoundedCornerShape(50)).background(color))
        }
    }
}

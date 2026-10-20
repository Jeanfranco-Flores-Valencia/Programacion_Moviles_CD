package com.floresvalencia.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Grafico de barras verticales (laboratorio de Canvas). La barra mayor va con color pleno. */
@Composable
fun GraficoBarras(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    alturaMaxima: Dp = 90.dp
) {
    val primario = MaterialTheme.colorScheme.primary
    val maximo = datos.maxOfOrNull { it.valor } ?: 0f
    if (datos.isEmpty()) {
        Text(
            "Sin ingresos registrados",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier
        )
        return
    }
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth()
    ) {
        datos.forEach { d ->
            val fraccion = if (maximo > 0f) d.valor / maximo else 0f
            val base = if (d.color.isSpecifiedColor()) d.color else primario
            val color = if (d.valor == maximo) base else base.copy(alpha = 0.35f)
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(
                    d.valor.toInt().toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Canvas(
                    modifier = Modifier
                        .width(44.dp)
                        .height(alturaMaxima * fraccion.coerceAtLeast(0.06f))
                ) {
                    drawRoundRect(color = color, cornerRadius = CornerRadius(6.dp.toPx()))
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    d.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

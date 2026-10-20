package com.floresvalencia.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Grafico de dona con leyenda a la derecha (laboratorio de Canvas). */
@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    diametro: Dp = 110.dp,
    textoCentro: String = datos.sumOf { it.valor.toDouble() }.toInt().toString(),
    subtituloCentro: String = "total"
) {
    val total = datos.sumOf { it.valor.toDouble() }.toFloat()
    val vacio = MaterialTheme.colorScheme.outlineVariant
    val primario = MaterialTheme.colorScheme.primary
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(diametro)) {
            Canvas(modifier = Modifier.size(diametro)) {
                val grosor = size.minDimension * 0.16f
                val arcoSize = Size(size.width - grosor, size.height - grosor)
                val origen = Offset(grosor / 2, grosor / 2)
                if (total <= 0f) {
                    drawArc(vacio, 0f, 360f, false, origen, arcoSize, style = Stroke(grosor))
                } else {
                    var inicio = -90f
                    datos.forEach { d ->
                        val barrido = d.valor / total * 360f
                        if (barrido > 0f) {
                            drawArc(
                                color = if (d.color.isSpecifiedColor()) d.color else primario,
                                startAngle = inicio,
                                sweepAngle = barrido,
                                useCenter = false,
                                topLeft = origen,
                                size = arcoSize,
                                style = Stroke(grosor)
                            )
                        }
                        inicio += barrido
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(textoCentro, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                Text(subtituloCentro, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.width(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            datos.forEach { d ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (d.color.isSpecifiedColor()) d.color else primario)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        d.etiqueta,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        d.valor.toInt().toString(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

internal fun androidx.compose.ui.graphics.Color.isSpecifiedColor(): Boolean =
    this != androidx.compose.ui.graphics.Color.Unspecified

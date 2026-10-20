package com.floresvalencia.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.model.EstadoPedido

/**
 * Linea de seguimiento del pedido (Recibido, En proceso, Listo, Entregado) dibujada con Canvas.
 * vertical = false -> version horizontal (Inicio). vertical = true -> version vertical (Detalle).
 * textosLaterales: texto a la derecha de cada etapa en la version vertical (fecha u hora).
 */
@Composable
fun StepperEstado(
    estado: EstadoPedido,
    modifier: Modifier = Modifier,
    vertical: Boolean = false,
    textosLaterales: List<String> = emptyList(),
    colorActivo: Color = MaterialTheme.colorScheme.primary,
    colorTexto: Color = MaterialTheme.colorScheme.onSurface
) {
    val etapas = EstadoPedido.etapas
    val indiceActual = etapas.indexOf(estado) // -1 si esta CANCELADO
    val colorInactivo = MaterialTheme.colorScheme.outlineVariant
    val fondo = MaterialTheme.colorScheme.surface

    if (!vertical) {
        Column(modifier = modifier.fillMaxWidth()) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
            ) {
                val n = etapas.size
                val radio = size.height * 0.42f
                val cy = size.height / 2
                val xs = List(n) { size.width * (it + 0.5f) / n }
                for (i in 0 until n - 1) {
                    drawLine(
                        color = if (i + 1 <= indiceActual) colorActivo else colorInactivo,
                        start = Offset(xs[i] + radio, cy),
                        end = Offset(xs[i + 1] - radio, cy),
                        strokeWidth = 3.dp.toPx()
                    )
                }
                xs.forEachIndexed { i, x ->
                    dibujarPunto(Offset(x, cy), radio, i <= indiceActual, colorActivo, colorInactivo, fondo)
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                etapas.forEachIndexed { i, etapa ->
                    Text(
                        text = etapa.etiqueta,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (i == indiceActual) FontWeight.Bold else FontWeight.Normal,
                        color = if (i <= indiceActual) colorTexto else colorTexto.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    } else {
        Column(modifier = modifier.fillMaxWidth()) {
            etapas.forEachIndexed { i, etapa ->
                Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                    val ultimo = i == etapas.lastIndex
                    Canvas(
                        modifier = Modifier
                            .width(28.dp)
                            .height(if (ultimo) 28.dp else 46.dp)
                    ) {
                        val radio = 11.dp.toPx()
                        val centro = Offset(size.width / 2, 14.dp.toPx())
                        if (!ultimo) {
                            drawLine(
                                color = if (i + 1 <= indiceActual) colorActivo else colorInactivo,
                                start = Offset(centro.x, centro.y + radio),
                                end = Offset(centro.x, size.height),
                                strokeWidth = 2.5.dp.toPx()
                            )
                        }
                        dibujarPunto(centro, radio, i <= indiceActual, colorActivo, colorInactivo, fondo)
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = etapa.etiqueta,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (i == indiceActual) FontWeight.Bold else FontWeight.Normal,
                        color = if (i <= indiceActual) colorTexto else colorTexto.copy(alpha = 0.45f),
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 4.dp)
                    )
                    Text(
                        text = textosLaterales.getOrElse(i) { if (i <= indiceActual) "" else "Pendiente" },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
        }
    }
}

/** Circulo de una etapa: lleno con check si ya se cumplio; vacio si esta pendiente. */
private fun DrawScope.dibujarPunto(
    centro: Offset,
    radio: Float,
    cumplido: Boolean,
    colorActivo: Color,
    colorInactivo: Color,
    fondo: Color
) {
    if (cumplido) {
        drawCircle(colorActivo, radius = radio, center = centro)
        val check = Path().apply {
            moveTo(centro.x - radio * 0.45f, centro.y + radio * 0.02f)
            lineTo(centro.x - radio * 0.1f, centro.y + radio * 0.36f)
            lineTo(centro.x + radio * 0.48f, centro.y - radio * 0.32f)
        }
        drawPath(check, Color.White, style = Stroke(width = radio * 0.24f, cap = StrokeCap.Round))
    } else {
        drawCircle(fondo, radius = radio, center = centro)
        drawCircle(colorInactivo, radius = radio, center = centro, style = Stroke(width = radio * 0.18f))
    }
}

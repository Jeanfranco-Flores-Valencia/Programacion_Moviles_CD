package com.floresvalencia.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Icono del servicio dibujado con Canvas segun el campo icono:
 * "wash" = lavadora, "iron" = plancha, "shirt" = camisa.
 */
@Composable
fun IconoServicio(
    icono: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 44.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    fondo: Color = MaterialTheme.colorScheme.primaryContainer
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(tamano)
            .clip(RoundedCornerShape(tamano * 0.28f))
            .background(fondo)
    ) {
        Canvas(modifier = Modifier.size(tamano * 0.56f)) {
            val trazo = Stroke(
                width = size.minDimension * 0.09f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
            when (icono) {
                "iron" -> dibujarPlancha(color, trazo)
                "shirt" -> dibujarCamisa(color, trazo)
                else -> dibujarLavadora(color, trazo)
            }
        }
    }
}

private fun DrawScope.dibujarLavadora(color: Color, trazo: Stroke) {
    val w = size.width
    val h = size.height
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.08f, h * 0.02f),
        size = Size(w * 0.84f, h * 0.96f),
        cornerRadius = CornerRadius(w * 0.12f),
        style = trazo
    )
    drawLine(color, Offset(w * 0.08f, h * 0.24f), Offset(w * 0.92f, h * 0.24f), strokeWidth = trazo.width)
    drawCircle(color, radius = w * 0.045f, center = Offset(w * 0.25f, h * 0.13f))
    drawCircle(color, radius = w * 0.045f, center = Offset(w * 0.40f, h * 0.13f))
    drawCircle(color, radius = w * 0.22f, center = Offset(w * 0.5f, h * 0.6f), style = trazo)
}

private fun DrawScope.dibujarPlancha(color: Color, trazo: Stroke) {
    val w = size.width
    val h = size.height
    val cuerpo = Path().apply {
        moveTo(w * 0.04f, h * 0.80f)
        lineTo(w * 0.96f, h * 0.80f)
        lineTo(w * 0.96f, h * 0.60f)
        cubicTo(w * 0.96f, h * 0.44f, w * 0.80f, h * 0.38f, w * 0.62f, h * 0.38f)
        lineTo(w * 0.30f, h * 0.38f)
        cubicTo(w * 0.16f, h * 0.38f, w * 0.06f, h * 0.60f, w * 0.04f, h * 0.80f)
        close()
    }
    drawPath(cuerpo, color, style = trazo)
    val asa = Path().apply {
        moveTo(w * 0.36f, h * 0.38f)
        lineTo(w * 0.42f, h * 0.18f)
        lineTo(w * 0.82f, h * 0.18f)
        lineTo(w * 0.82f, h * 0.38f)
    }
    drawPath(asa, color, style = trazo)
}

private fun DrawScope.dibujarCamisa(color: Color, trazo: Stroke) {
    val w = size.width
    val h = size.height
    val camisa = Path().apply {
        moveTo(w * 0.36f, h * 0.08f)
        lineTo(w * 0.08f, h * 0.22f)
        lineTo(w * 0.0f, h * 0.46f)
        lineTo(w * 0.20f, h * 0.52f)
        lineTo(w * 0.22f, h * 0.94f)
        lineTo(w * 0.78f, h * 0.94f)
        lineTo(w * 0.80f, h * 0.52f)
        lineTo(w * 1.0f, h * 0.46f)
        lineTo(w * 0.92f, h * 0.22f)
        lineTo(w * 0.64f, h * 0.08f)
        quadraticBezierTo(w * 0.5f, h * 0.30f, w * 0.36f, h * 0.08f)
        close()
    }
    drawPath(camisa, color, style = trazo)
}

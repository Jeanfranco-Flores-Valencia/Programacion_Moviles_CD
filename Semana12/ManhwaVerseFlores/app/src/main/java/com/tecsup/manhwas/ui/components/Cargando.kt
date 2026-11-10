package com.tecsup.manhwas.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.tecsup.manhwas.ui.theme.Colores

/** Indicador simple (login y detalle). */
@Composable
fun Cargando(texto: String = "Cargando...") {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Colores.Morado)
        Text(texto, style = MaterialTheme.typography.bodyMedium, color = Colores.TextoSuave)
    }
}

/**
 * Mejora IA: esqueleto del catalogo mientras carga, como en las webs reales:
 * rectangulos con un brillo que pasa de izquierda a derecha en lugar de un spinner.
 */
@Composable
fun EsqueletoCatalogo(columnas: Int = 2) {
    val transicion = rememberInfiniteTransition(label = "brillo")
    val x by transicion.animateFloat(
        initialValue = -400f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(tween(1300, easing = LinearEasing), RepeatMode.Restart),
        label = "x"
    )
    val brillo = Brush.linearGradient(
        colors = listOf(Colores.Superficie, Colores.SuperficieAlta, Colores.Superficie),
        start = Offset(x, 0f),
        end = Offset(x + 400f, 400f)
    )
    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(18.dp)).background(brillo))
        repeat(2) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(columnas) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                        Box(modifier = Modifier.fillMaxWidth().aspectRatio(2f / 3f).clip(RoundedCornerShape(12.dp)).background(brillo))
                        Box(modifier = Modifier.fillMaxWidth(0.8f).height(14.dp).clip(RoundedCornerShape(4.dp)).background(brillo))
                    }
                }
            }
        }
    }
}

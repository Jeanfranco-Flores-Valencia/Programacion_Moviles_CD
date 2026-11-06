package com.tecsup.authfirebaseflores

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/** Icono que flota en el fondo: posicion relativa (0..1), tamano y duracion de su vaiven. */
private class IconoFlotante(
    val icono: ImageVector,
    val x: Float,
    val y: Float,
    val tamano: Int,
    val duracion: Int
)

private val iconos = listOf(
    IconoFlotante(Icons.AutoMirrored.Outlined.MenuBook, 0.08f, 0.10f, 44, 5200),
    IconoFlotante(Icons.Outlined.School, 0.78f, 0.06f, 52, 6100),
    IconoFlotante(Icons.Outlined.Lightbulb, 0.86f, 0.34f, 36, 4700),
    IconoFlotante(Icons.Outlined.Science, 0.04f, 0.42f, 40, 5600),
    IconoFlotante(Icons.Outlined.Calculate, 0.82f, 0.62f, 38, 6400),
    IconoFlotante(Icons.Outlined.Edit, 0.10f, 0.72f, 34, 4900),
    IconoFlotante(Icons.Outlined.Translate, 0.70f, 0.86f, 40, 5800),
    IconoFlotante(Icons.Outlined.Psychology, 0.16f, 0.90f, 44, 6700)
)

/** Colores de los lomos de los libros (granate, azul, verde, ocre y cuero). */
private val lomos = listOf(
    Color(0xFF7A2E2E), Color(0xFF2E4A7A), Color(0xFF3F5E3A), Color(0xFFB08A3E), Color(0xFF6B4A32)
)

/**
 * Dibuja estantes de biblioteca de lado a lado: una tabla de madera cada cierto alto
 * y encima libros de distinto ancho, alto y color. La semilla fija hace que siempre se vea igual.
 */
private fun DrawScope.dibujarEstantes() {
    val azar = Random(11)
    val altoEstante = 150.dp.toPx()
    val tabla = 6.dp.toPx()
    var base = altoEstante
    while (base < size.height + altoEstante) {
        var x = 0f
        while (x < size.width) {
            val ancho = (10 + azar.nextInt(14)).dp.toPx()
            val alto = altoEstante * (0.55f + azar.nextFloat() * 0.35f)
            val inclinado = azar.nextInt(12) == 0
            val color = lomos[azar.nextInt(lomos.size)].copy(alpha = 0.22f)
            if (inclinado) {
                // Un libro apoyado en diagonal de vez en cuando, como en una biblioteca real
                drawRect(color, Offset(x + ancho, base - alto * 0.9f), Size(ancho, alto * 0.9f))
                x += ancho * 2.2f
            } else {
                drawRect(color, Offset(x, base - alto), Size(ancho, alto))
                // Franja dorada del titulo en el lomo
                drawRect(Paleta.Ambar.copy(alpha = 0.10f), Offset(x, base - alto * 0.8f), Size(ancho, 2.dp.toPx()))
                x += ancho + 1.dp.toPx()
            }
        }
        drawRect(Paleta.Madera.copy(alpha = 0.9f), Offset(0f, base), Size(size.width, tabla))
        base += altoEstante
    }
}

/**
 * Mejora IA: fondo comun de la app con ambiente de biblioteca de noche. Estantes con libros,
 * una luz de lectura celeste que "respira" y simbolos de estudio flotando muy suaves.
 */
@Composable
fun FondoEducativo(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val transicion = rememberInfiniteTransition(label = "fondo")
    val brillo by transicion.animateFloat(
        initialValue = 0.55f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Reverse),
        label = "brillo"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Paleta.Pizarra, Paleta.Tinta)))
    ) {
        // Estantes: no leen estado animado, asi que no se redibujan en cada cuadro
        Canvas(modifier = Modifier.fillMaxSize()) { dibujarEstantes() }

        // Velo azul para que la tarjeta se lea bien, y luz de lampara en celeste Tecsup arriba
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(Brush.verticalGradient(listOf(Paleta.Tinta.copy(alpha = 0.55f), Paleta.Tinta.copy(alpha = 0.85f))))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Paleta.Celeste.copy(alpha = 0.35f * brillo), Paleta.Celeste.copy(alpha = 0f)),
                    center = Offset(size.width * 0.5f, 0f),
                    radius = size.width * 0.9f
                ),
                radius = size.width * 0.9f,
                center = Offset(size.width * 0.5f, 0f)
            )
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            iconos.forEach { item ->
                val desplazamiento by transicion.animateFloat(
                    initialValue = -10f,
                    targetValue = 10f,
                    animationSpec = infiniteRepeatable(tween(item.duracion), RepeatMode.Reverse),
                    label = "flotar"
                )
                Icon(
                    imageVector = item.icono,
                    contentDescription = null,
                    tint = Paleta.Crema.copy(alpha = 0.10f),
                    modifier = Modifier
                        .offset(x = maxWidth * item.x, y = maxHeight * item.y)
                        .size(item.tamano.dp)
                        .graphicsLayer {
                            translationY = desplazamiento * density
                            rotationZ = desplazamiento / 2f
                        }
                )
            }
        }

        content()
    }
}

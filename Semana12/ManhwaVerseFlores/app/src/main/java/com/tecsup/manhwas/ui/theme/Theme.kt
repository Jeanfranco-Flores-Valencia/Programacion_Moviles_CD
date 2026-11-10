package com.tecsup.manhwas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Colores tipo "ventana del sistema" de los manhwas de mazmorras: violeta y azul brillante sobre negro. */
object Colores {
    val Fondo = Color(0xFF0B0918)
    val Superficie = Color(0xFF161230)
    val SuperficieAlta = Color(0xFF211B45)
    val Violeta = Color(0xFF7C5CFF)
    val Cian = Color(0xFF3FD0FF)
    val Texto = Color(0xFFEDEBFF)
    val TextoSuave = Color(0xFFA8A3C7)
    val Dorado = Color(0xFFFFC857)
    val Rojo = Color(0xFFFF5C7A)
}

private val esquema = darkColorScheme(
    primary = Colores.Violeta,
    onPrimary = Color.White,
    secondary = Colores.Cian,
    onSecondary = Colores.Fondo,
    background = Colores.Fondo,
    onBackground = Colores.Texto,
    surface = Colores.Fondo,
    onSurface = Colores.Texto,
    surfaceVariant = Colores.Superficie,
    onSurfaceVariant = Colores.TextoSuave,
    surfaceContainer = Colores.Superficie,
    surfaceContainerHigh = Colores.SuperficieAlta,
    outline = Color(0xFF3A3366),
    error = Colores.Rojo
)

@Composable
fun ManhwaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = esquema, content = content)
}

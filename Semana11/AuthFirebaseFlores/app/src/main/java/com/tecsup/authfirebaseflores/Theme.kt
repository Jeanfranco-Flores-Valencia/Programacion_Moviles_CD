package com.tecsup.authfirebaseflores

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

/**
 * Mejora IA: paleta de una biblioteca de noche con los tonos de Tecsup:
 * azul marino de fondo, celeste Tecsup para acciones y madera tenue en los estantes.
 */
object Paleta {
    val Tinta = Color(0xFF061529)        // azul noche
    val Pizarra = Color(0xFF0B2A4A)      // azul marino Tecsup
    val Madera = Color(0xFF3A2A1C)       // tablas de los estantes
    val Tarjeta = Color(0xCC0A1E36)      // azul translucido
    val Borde = Color(0x2EFFFFFF)
    val Celeste = Color(0xFF00A3E0)      // celeste Tecsup
    val CelesteClaro = Color(0xFF7FD3F2)
    val Crema = Color(0xFFEDE6D6)
    val Texto = Color(0xFFF2F6FA)
    val TextoSuave = Color(0xFFA9BACB)
    val Ambar = Color(0xFFF2B45A)
    val Exito = Color(0xFF7ED9A0)
    val Error = Color(0xFFFF9C8F)
}

private val esquema = darkColorScheme(
    primary = Paleta.Celeste,
    onPrimary = Color.White,
    secondary = Paleta.CelesteClaro,
    onSecondary = Paleta.Tinta,
    background = Paleta.Tinta,
    onBackground = Paleta.Texto,
    surface = Paleta.Pizarra,
    onSurface = Paleta.Texto,
    onSurfaceVariant = Paleta.TextoSuave,
    outline = Paleta.Borde,
    error = Paleta.Error
)

@Composable
fun TemaEducativo(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = esquema) {
        // Sin un Surface, Compose pinta el texto en negro por defecto: aqui se fija el texto claro
        // para toda la app, asi ningun Text ni Icon queda invisible sobre el fondo oscuro.
        CompositionLocalProvider(LocalContentColor provides Paleta.Texto, content = content)
    }
}

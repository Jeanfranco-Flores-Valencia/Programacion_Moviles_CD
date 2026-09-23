package com.floresvalencia.tecsupfit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Verde = Color(0xFF0F6E56)
val VerdeClaro = Color(0xFFDDF1EA)
val GrisTarjeta = Color(0xFFF0F0F0)

private val ColoresFit = lightColorScheme(
    primary = Verde,
    onPrimary = Color.White,
    primaryContainer = VerdeClaro,         // fondo de íconos y avatar
    onPrimaryContainer = Verde,
    secondaryContainer = VerdeClaro,       // indicador de la pestaña activa del bottomBar
    onSecondaryContainer = Verde,
    background = Color.White,
    onBackground = Color(0xFF1C1B1F),
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = GrisTarjeta,          // fondo de tarjetas y chips
    onSurfaceVariant = Color(0xFF6B6B6B),  // textos secundarios
    surfaceContainer = Color.White         // fondo del bottomBar
)

@Composable
fun TecsupFitTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColoresFit, content = content)
}
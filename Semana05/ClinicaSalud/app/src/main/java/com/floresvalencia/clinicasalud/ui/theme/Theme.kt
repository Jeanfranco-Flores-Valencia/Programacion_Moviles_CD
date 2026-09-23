package com.floresvalencia.clinicasalud.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Morado = Color(0xFF5B2A86)
val MoradoClaro = Color(0xFFEBDDF7)
val Lavanda = Color(0xFFF4EFF8)

private val ColoresClinica = lightColorScheme(
    primary = Morado,
    onPrimary = Color.White,
    primaryContainer = MoradoClaro,
    onPrimaryContainer = Morado,
    secondaryContainer = MoradoClaro,      // fondo del ítem seleccionado en el drawer
    onSecondaryContainer = Morado,         // texto del ítem seleccionado en el drawer
    background = Color.White,
    onBackground = Color(0xFF1C1B1F),
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Lavanda,              // fondo de tarjetas y chips
    onSurfaceVariant = Color(0xFF6B6570),  // textos secundarios (grises)
    surfaceContainerLow = Color.White      // fondo del menú lateral
)

@Composable
fun ClinicaSaludTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColoresClinica, content = content)
}
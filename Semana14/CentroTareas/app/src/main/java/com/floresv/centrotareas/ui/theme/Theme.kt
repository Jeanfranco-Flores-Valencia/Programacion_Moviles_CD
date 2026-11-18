package com.floresv.centrotareas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val esquema = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E4FF),
    onPrimaryContainer = Color(0xFF0A2A5C),
    secondary = Color(0xFF2E7D32),
    secondaryContainer = Color(0xFFF4F8FF),   // botones tonales (Tócame) en azul muy claro
    onSecondaryContainer = Color(0xFF0A2A5C),
    error = Color(0xFFC62828),
    errorContainer = Color(0xFFFFE3E0),
    onErrorContainer = Color(0xFF7A1010)
)

@Composable
fun CentroTareasTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = esquema, content = content)
}

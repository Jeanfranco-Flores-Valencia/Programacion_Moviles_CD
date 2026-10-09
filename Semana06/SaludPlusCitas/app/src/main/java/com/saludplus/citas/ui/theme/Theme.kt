package com.saludplus.citas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaClaro = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Blanco,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulOscuro,
    secondary = Celeste,
    onSecondary = Blanco,
    background = Blanco,
    onBackground = TextoPrincipal,
    surface = Blanco,
    onSurface = TextoPrincipal,
    surfaceVariant = FondoApp,
    onSurfaceVariant = TextoSecundario,
    outline = BordeSuave,
    error = Rojo
)

@Composable
fun SaludPlusTheme(content: @Composable () -> Unit) {
    // La app usa siempre el tema claro del diseño (sin colores dinámicos)
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}

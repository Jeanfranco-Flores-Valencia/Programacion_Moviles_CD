package com.saludplus.citas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = Primario,
    onPrimary = SobrePrimario,
    primaryContainer = PrimarioClaro,
    onPrimaryContainer = PrimarioOscuro,
    secondary = Secundario,
    onSecondary = TextoPrincipal,
    secondaryContainer = SecundarioClaro,
    onSecondaryContainer = PrimarioOscuro,
    tertiary = Acento,
    onTertiary = TextoPrincipal,
    tertiaryContainer = AcentoClaro,
    onTertiaryContainer = Color(0xFF6B4708),
    background = Fondo,
    onBackground = TextoPrincipal,
    surface = Superficie,
    onSurface = TextoPrincipal,
    surfaceVariant = SuperficieVariante,
    onSurfaceVariant = TextoSecundario,
    outline = Borde,
    outlineVariant = Color(0xFFE8EFEA),
    error = Error,
    onError = Color.White,
    errorContainer = ErrorClaro,
    onErrorContainer = Color(0xFF7A231C)
)

@Composable
fun SaludPlusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}

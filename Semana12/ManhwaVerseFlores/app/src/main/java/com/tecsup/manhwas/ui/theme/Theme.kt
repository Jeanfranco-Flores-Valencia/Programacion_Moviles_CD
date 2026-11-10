package com.tecsup.manhwas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/**
 * Mejora IA: paleta inspirada en webs de lectura de manhwas (fondo casi negro y acentos morados).
 * Todos los textos usan Texto o TextoSuave sobre fondos oscuros para que se lean bien.
 */
object Colores {
    val Fondo = Color(0xFF0B0B10)
    val Superficie = Color(0xFF15151D)
    val SuperficieAlta = Color(0xFF1F1F2B)
    val Borde = Color(0xFF2C2C3B)
    val Morado = Color(0xFF8B5CF6)
    val MoradoClaro = Color(0xFFC4B5FD)
    val MoradoOscuro = Color(0xFF3B1D7A)
    val Texto = Color(0xFFF4F4F7)
    val TextoSuave = Color(0xFFA6A6BA)
    val Dorado = Color(0xFFFBBF24)
    val Verde = Color(0xFF34D399)
    val Rojo = Color(0xFFF43F5E)
}

private val esquema = darkColorScheme(
    primary = Colores.Morado,
    onPrimary = Color.White,
    primaryContainer = Colores.MoradoOscuro,
    onPrimaryContainer = Colores.MoradoClaro,
    secondary = Colores.MoradoClaro,
    onSecondary = Colores.Fondo,
    background = Colores.Fondo,
    onBackground = Colores.Texto,
    surface = Colores.Fondo,
    onSurface = Colores.Texto,
    surfaceVariant = Colores.Superficie,
    onSurfaceVariant = Colores.TextoSuave,
    surfaceContainer = Colores.Superficie,
    surfaceContainerHigh = Colores.SuperficieAlta,
    surfaceContainerHighest = Colores.SuperficieAlta,
    outline = Colores.Borde,
    outlineVariant = Colores.Borde,
    error = Colores.Rojo
)

/** Titulos un poco mas gruesos, como en las webs de lectura. */
private val tipografia = Typography().let { t ->
    t.copy(
        headlineMedium = t.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
        titleLarge = t.titleLarge.copy(fontWeight = FontWeight.Bold),
        titleMedium = t.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        titleSmall = t.titleSmall.copy(fontWeight = FontWeight.SemiBold)
    )
}

@Composable
fun ManhwaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = esquema, typography = tipografia, content = content)
}

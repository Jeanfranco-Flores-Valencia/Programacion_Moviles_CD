package com.floresvalencia.lavacampus.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.floresvalencia.lavacampus.model.Rol

private fun esquemaClaro(rol: Rol): ColorScheme = lightColorScheme(
    primary = if (rol == Rol.ADMIN) Violeta else Turquesa,
    onPrimary = Blanco,
    primaryContainer = if (rol == Rol.ADMIN) VioletaContenedor else TurquesaContenedor,
    onPrimaryContainer = if (rol == Rol.ADMIN) VioletaOscuroContenedor else TurquesaOscuroContenedor,
    secondary = if (rol == Rol.ADMIN) Violeta else Turquesa,
    background = if (rol == Rol.ADMIN) FondoAdministrador else FondoEstudiante,
    onBackground = TextoPrincipal,
    surface = Blanco,
    onSurface = TextoPrincipal,
    surfaceVariant = SuperficieVariante,
    onSurfaceVariant = TextoSecundario,
    outline = TextoSecundario,
    outlineVariant = Borde,
    error = RojoError,
    onError = Blanco,
    errorContainer = ErrorContenedor,
    tertiaryContainer = Aviso,
    onTertiaryContainer = AvisoTexto
)

private fun esquemaOscuro(rol: Rol): ColorScheme = darkColorScheme(
    primary = if (rol == Rol.ADMIN) VioletaClaro else TurquesaClaro,
    onPrimary = if (rol == Rol.ADMIN) VioletaOscuroContenedor else TurquesaOscuroContenedor,
    primaryContainer = if (rol == Rol.ADMIN) VioletaOscuroContenedor else TurquesaOscuroContenedor,
    onPrimaryContainer = if (rol == Rol.ADMIN) VioletaContenedor else TurquesaContenedor,
    secondary = if (rol == Rol.ADMIN) VioletaClaro else TurquesaClaro,
    background = FondoOscuro,
    onBackground = TextoOscuro,
    surface = SuperficieOscura,
    onSurface = TextoOscuro,
    surfaceVariant = SuperficieVarianteOscura,
    onSurfaceVariant = TextoSecundarioOscuro,
    outline = TextoSecundarioOscuro,
    outlineVariant = BordeOscuro,
    error = RojoError,
    onError = Blanco,
    errorContainer = ErrorContenedor,
    tertiaryContainer = AvisoTexto,
    onTertiaryContainer = Aviso
)

/**
 * Tema de LavaCampus. Entrega un ColorScheme segun el perfil:
 * turquesa para el estudiante y violeta para el administrador.
 * En las pantallas usa MaterialTheme.colorScheme.primary, nunca un color fijo.
 */
@Composable
fun LavaTheme(
    rol: Rol = Rol.CLIENTE,
    oscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (oscuro) esquemaOscuro(rol) else esquemaClaro(rol),
        typography = LavaTypography,
        content = content
    )
}

package com.floresvalencia.lavacampus.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.floresvalencia.lavacampus.model.Rol

private fun esquemaClaro(rol: Rol): ColorScheme = lightColorScheme(
    primary = when (rol) {
        Rol.ADMIN -> Violeta
        Rol.SOPORTE -> Esmeralda
        Rol.CLIENTE -> Turquesa
    },
    onPrimary = Blanco,
    primaryContainer = when (rol) {
        Rol.ADMIN -> VioletaContenedor
        Rol.SOPORTE -> EsmeraldaContenedor
        Rol.CLIENTE -> TurquesaContenedor
    },
    onPrimaryContainer = when (rol) {
        Rol.ADMIN -> VioletaOscuroContenedor
        Rol.SOPORTE -> EsmeraldaOscuroContenedor
        Rol.CLIENTE -> TurquesaOscuroContenedor
    },
    secondary = when (rol) {
        Rol.ADMIN -> Violeta
        Rol.SOPORTE -> Esmeralda
        Rol.CLIENTE -> Turquesa
    },
    background = when (rol) {
        Rol.ADMIN -> FondoAdministrador
        Rol.SOPORTE -> FondoSoporte
        Rol.CLIENTE -> FondoEstudiante
    },
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
    primary = when (rol) {
        Rol.ADMIN -> VioletaClaro
        Rol.SOPORTE -> EsmeraldaClaro
        Rol.CLIENTE -> TurquesaClaro
    },
    onPrimary = when (rol) {
        Rol.ADMIN -> VioletaOscuroContenedor
        Rol.SOPORTE -> EsmeraldaOscuroContenedor
        Rol.CLIENTE -> TurquesaOscuroContenedor
    },
    primaryContainer = when (rol) {
        Rol.ADMIN -> VioletaOscuroContenedor
        Rol.SOPORTE -> EsmeraldaOscuroContenedor
        Rol.CLIENTE -> TurquesaOscuroContenedor
    },
    onPrimaryContainer = when (rol) {
        Rol.ADMIN -> VioletaContenedor
        Rol.SOPORTE -> EsmeraldaContenedor
        Rol.CLIENTE -> TurquesaContenedor
    },
    secondary = when (rol) {
        Rol.ADMIN -> VioletaClaro
        Rol.SOPORTE -> EsmeraldaClaro
        Rol.CLIENTE -> TurquesaClaro
    },
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
 * turquesa para el estudiante, violeta para el administrador y esmeralda para soporte.
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

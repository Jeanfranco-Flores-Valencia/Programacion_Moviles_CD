package com.saludplus.citas.ui.theme

import androidx.compose.ui.graphics.Color

// ======================================================================
// PALETA "VERDE SALVIA" - SaludPlusCitas
// ======================================================================

// Marca
val Primario = Color(0xFF3D7A5A)
val PrimarioOscuro = Color(0xFF2C5A42)
val PrimarioClaro = Color(0xFFDDEEE3)
val PrimarioMuyClaro = Color(0xFFEEF6F1)
val Secundario = Color(0xFF8CC7A1)
val SecundarioClaro = Color(0xFFE3F2E8)
val Acento = Color(0xFFE9A23B)
val AcentoClaro = Color(0xFFFBEBD0)

// Neutros
val Fondo = Color(0xFFF5F9F6)
val Superficie = Color(0xFFFFFFFF)
val SuperficieVariante = Color(0xFFEAF2EC)
val Borde = Color(0xFFDCE6DF)
val TextoPrincipal = Color(0xFF1F3A2B)
val TextoSecundario = Color(0xFF5B6F63)
val TextoDeshabilitado = Color(0xFFA3B1A8)
val SobrePrimario = Color(0xFFFFFFFF)

// Estados semánticos
val Exito = Color(0xFF2E9D5B)
val ExitoClaro = Color(0xFFE2F4E9)
val Advertencia = Color(0xFFD98E04)
val AdvertenciaClaro = Color(0xFFFCF0D6)
val Error = Color(0xFFC2453A)
val ErrorClaro = Color(0xFFFBE7E5)
val Info = Color(0xFF3F7F9C)
val InfoClaro = Color(0xFFE3F0F5)

// ======================================================================
// ALIAS DE COMPATIBILIDAD (para pantallas existentes que referencian nombres antiguos)
// ======================================================================
val AzulPrimario = Primario
val AzulOscuro = PrimarioOscuro
val AzulClaro = PrimarioClaro
val AzulMuyClaro = PrimarioMuyClaro
val Celeste = Secundario
val FondoApp = Fondo
val Blanco = Superficie
val BordeSuave = Borde
val VerdeExito = Exito
val VerdeClaro = ExitoClaro
val Morado = Primario
val MoradoClaro = PrimarioClaro
val Naranja = Acento
val NaranjaClaro = AcentoClaro
val Rosa = Secundario
val RosaClaro = SecundarioClaro
val Rojo = Error
val RojoClaro = ErrorClaro
val Amarillo = Acento

// Semáforo del calendario
data class SemaforoColor(
    val fondo: Color,
    val texto: Color,
    val indicador: Color
)

object SaludPlusColors {
    val semaforoLibre = SemaforoColor(
        fondo = Color(0xFFD7F0DF),
        texto = Color(0xFF1E7A45),
        indicador = Color(0xFF2FB36A)
    )
    val semaforoPoco = SemaforoColor(
        fondo = Color(0xFFFDEFC9),
        texto = Color(0xFF8A5A00),
        indicador = Color(0xFFF2B01E)
    )
    val semaforoLleno = SemaforoColor(
        fondo = Color(0xFFF9DCD9),
        texto = Color(0xFFA3322A),
        indicador = Color(0xFFD64A3F)
    )
    val semaforoInactivo = SemaforoColor(
        fondo = Color(0xFFEDF1EE),
        texto = Color(0xFFA3B1A8),
        indicador = Color(0xFFA3B1A8)
    )
}

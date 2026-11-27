package com.tecsup.avisoscampus.model

/** De donde salio el aviso. El texto es la etiqueta que se ve en el historial. */
enum class Origen(val texto: String) {
    BOTON("Botón"),
    RECORDATORIO("Recordatorio"),
    MODO_AVION("Modo avión"),
    CARGADOR("Cargador")   // Mejora IA
}

data class Aviso(
    val id: Int,
    val titulo: String,
    val texto: String,
    val origen: Origen,
    val hora: String
)

/** Estado del permiso de notificaciones que ve la pantalla. */
sealed interface PermisoUi {
    data object SinPedir : PermisoUi
    data object Concedido : PermisoUi
    data object Denegado : PermisoUi
}

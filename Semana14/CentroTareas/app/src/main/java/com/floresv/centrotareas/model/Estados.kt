package com.floresv.centrotareas.model

sealed interface PrimosUi {
    data object Inicial : PrimosUi
    data object Calculando : PrimosUi
    data class Listo(val cantidad: Int, val milisegundos: Long) : PrimosUi
    data object Cancelado : PrimosUi
}

sealed interface CargaUi {
    data object Inicial : CargaUi
    data object Cargando : CargaUi
    data class Exito(
        val modo: String,
        val perfil: String,
        val pedidos: Int,
        val milisegundos: Long
    ) : CargaUi
    data class Error(val mensaje: String) : CargaUi
}

/** Mejora IA: avance del conteo de primos (porcentaje de 0f a 1f y primos encontrados hasta ahora). */
data class AvancePrimos(val porcentaje: Float, val cantidad: Int, val terminado: Boolean)

/** Mejora IA: una carga exitosa con su modo ("Una por una" o "A la vez") y su tiempo. */
data class Medicion(val modo: String, val milisegundos: Long)

/** Mejora IA: promedios de cada modo y cuanto mas rapido fue cargar a la vez (null si falta un modo). */
data class ResumenTiempos(val promedioUnaPorUna: Long?, val promedioALaVez: Long?, val ahorroPorcentaje: Int?)

package com.floresv.campusgps.data

import kotlinx.coroutines.flow.Flow

/** Mejora IA: preferencias pequenas que deben sobrevivir al cerrar la app. */
interface PreferenciasRepository {
    /** Id del ultimo lugar que el usuario abrio; null si nunca abrio uno. */
    val ultimoLugar: Flow<Int?>
    suspend fun guardarUltimoLugar(id: Int)
}

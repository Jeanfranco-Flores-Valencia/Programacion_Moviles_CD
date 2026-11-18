package com.floresv.campusgps.data

import com.floresv.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
    fun playServicesDisponible(): Boolean
}

/** El GPS esta apagado o el sistema no pudo determinar la ubicacion. */
class UbicacionNoDisponibleException : Exception("No se pudo determinar la ubicación")

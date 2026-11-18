package com.floresv.campusgps.util

import android.location.Location
import com.floresv.campusgps.model.Lugar
import com.floresv.campusgps.model.Posicion

/** Distancia en metros desde la posicion del usuario hasta un lugar. Funcion pura: no guarda estado. */
fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultado = FloatArray(1)
    // Orden: (lat1, lon1, lat2, lon2, resultado)
    Location.distanceBetween(desde.latitud, desde.longitud, hasta.latitud, hasta.longitud, resultado)
    return resultado[0]
}

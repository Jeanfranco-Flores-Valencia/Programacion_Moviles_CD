package com.floresv.campusgps.data

import com.floresv.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}

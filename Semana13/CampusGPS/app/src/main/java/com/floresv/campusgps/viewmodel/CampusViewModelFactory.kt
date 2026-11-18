package com.floresv.campusgps.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.floresv.campusgps.data.LugaresRepositoryLocal
import com.floresv.campusgps.data.PreferenciasRepositoryDataStore
import com.floresv.campusgps.data.UbicacionRepositoryGoogle

/** Crea el MapaViewModel con sus repositorios. El Context se queda aqui: el ViewModel no lo recibe. */
class CampusViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MapaViewModel(
            UbicacionRepositoryGoogle(context.applicationContext),
            LugaresRepositoryLocal(),
            PreferenciasRepositoryDataStore(context.applicationContext)
        ) as T
}

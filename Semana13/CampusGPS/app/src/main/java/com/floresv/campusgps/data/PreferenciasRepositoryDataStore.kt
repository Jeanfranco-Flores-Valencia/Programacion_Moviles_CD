package com.floresv.campusgps.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Una sola instancia de DataStore por archivo, declarada a nivel de archivo. */
private val Context.preferencias: DataStore<Preferences> by preferencesDataStore("campus")

/** Implementacion con DataStore Preferences. El Context queda aqui, no en el ViewModel. */
class PreferenciasRepositoryDataStore(private val context: Context) : PreferenciasRepository {

    private val claveUltimo = intPreferencesKey("ultimo_lugar")

    override val ultimoLugar: Flow<Int?> = context.preferencias.data.map { it[claveUltimo] }

    override suspend fun guardarUltimoLugar(id: Int) {
        context.preferencias.edit { it[claveUltimo] = id }
    }
}

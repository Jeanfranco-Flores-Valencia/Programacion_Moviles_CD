package com.floresvalencia.lavacampus

import android.app.Application
import com.floresvalencia.lavacampus.data.local.DatosIniciales
import com.floresvalencia.lavacampus.data.local.LavaDatabase
import com.floresvalencia.lavacampus.data.repository.LavaRepository
import com.floresvalencia.lavacampus.data.repository.LavaRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Application: crea una sola base de datos y un solo Repository para toda la app. */
class LavaApp : Application() {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val db: LavaDatabase by lazy { LavaDatabase.obtener(this) }
    val repo: LavaRepository by lazy { LavaRepositoryRoom(db) }

    override fun onCreate() {
        super.onCreate()
        // Siembra los datos de prueba la primera vez (en segundo plano)
        appScope.launch { DatosIniciales.sembrar(db) }
    }
}

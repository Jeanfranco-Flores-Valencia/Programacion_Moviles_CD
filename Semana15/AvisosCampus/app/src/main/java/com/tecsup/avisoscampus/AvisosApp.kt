package com.tecsup.avisoscampus

import android.app.Application
import com.tecsup.avisoscampus.notificacion.Notificador

/** Se crea una sola vez, antes que cualquier pantalla. */
class AvisosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // El canal se crea una sola vez, al iniciar la app
        Notificador.crearCanal(this)
    }
}

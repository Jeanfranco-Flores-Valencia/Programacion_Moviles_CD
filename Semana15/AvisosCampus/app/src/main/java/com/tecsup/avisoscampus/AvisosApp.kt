package com.tecsup.avisoscampus

import android.app.Application

/** Se crea una sola vez, antes que cualquier pantalla. */
class AvisosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // TODO Paso 1: Notificador.crearCanal(this)
    }
}

package com.tecsup.manhwas

import android.app.Application

/** Guarda el AppContainer: vive mientras la app este abierta, aunque se gire la pantalla. */
class ManhwaApp : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}

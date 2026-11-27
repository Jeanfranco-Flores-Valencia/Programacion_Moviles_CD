package com.tecsup.avisoscampus.notificacion

import android.content.Context
import com.tecsup.avisoscampus.model.Aviso

/** Todo lo que habla con el sistema de notificaciones. Lo usa la pantalla, nunca el ViewModel. */
object Notificador {
    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        // TODO Paso 1: si SDK_INT >= O, crear NotificationChannel y registrarlo
    }

    // TODO Paso 2: fun tienePermiso(context: Context): Boolean

    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO Paso 3: si no tienePermiso(context) -> return false
        // TODO Paso 3: construir la notificacion con NotificationCompat.Builder
        // TODO Paso 3: NotificationManagerCompat.from(context).notify(aviso.id, ...)
        return true
    }
}

package com.tecsup.avisoscampus.notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.tecsup.avisoscampus.model.Aviso

/** Todo lo que habla con el sistema de notificaciones. Lo usa la pantalla, nunca el ViewModel. */
object Notificador {
    const val CANAL = "avisos"

    /** Desde Android 8 (O) toda notificacion necesita un canal. Crearlo otra vez no hace nada. */
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Notificaciones de la app"
            }
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
        }
    }

    // TODO Paso 2: fun tienePermiso(context: Context): Boolean

    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO Paso 3: si no tienePermiso(context) -> return false
        // TODO Paso 3: construir la notificacion con NotificationCompat.Builder
        // TODO Paso 3: NotificationManagerCompat.from(context).notify(aviso.id, ...)
        return true
    }
}

package com.tecsup.manhwas.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tecsup.manhwas.MainActivity
import com.tecsup.manhwas.R

/** Implementacion con NotificationCompat: canal propio y permiso POST_NOTIFICATIONS. */
class NotificadorAndroid(private val context: Context) : Notificador {

    init {
        val canal = NotificationChannel(CANAL, "Sincronización", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "Avisa cuando el catálogo se actualiza"
        }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }

    @SuppressLint("MissingPermission") // el permiso se revisa justo antes
    override fun mostrar(titulo: String, texto: String) {
        // Permiso negado: la app sigue normal, solo sin aviso
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return

        // Al tocar la notificacion se abre la app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val alTocar = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificacion = NotificationCompat.Builder(context, CANAL)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setContentIntent(alTocar)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(context).notify(ID, notificacion)
    }

    private companion object {
        const val CANAL = "sincronizacion"
        const val ID = 1
    }
}

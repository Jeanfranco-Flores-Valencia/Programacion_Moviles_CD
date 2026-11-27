package com.tecsup.avisoscampus.notificacion

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
import com.tecsup.avisoscampus.MainActivity
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.model.Origen

/** Todo lo que habla con el sistema de notificaciones. Lo usa la pantalla, nunca el ViewModel. */
object Notificador {
    const val CANAL = "avisos"
    /** Mejora IA: canal de importancia alta (aparece como ventana emergente). Lo usa el recordatorio. */
    const val CANAL_URGENTE = "urgentes"

    /** Desde Android 8 (O) toda notificacion necesita un canal. Crearlo otra vez no hace nada. */
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Notificaciones de la app"
            }
            val urgente = NotificationChannel(CANAL_URGENTE, "Urgentes", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Recordatorios que no se pueden pasar por alto"
            }
            context.getSystemService(NotificationManager::class.java).createNotificationChannels(listOf(canal, urgente))
        }
    }

    /** Antes de Android 13 no hay que pedir permiso: siempre se puede notificar. */
    fun tienePermiso(context: Context): Boolean =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) true
        else ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    /** Muestra el aviso como notificacion. Devuelve false (sin lanzar error) si no hay permiso. */
    @SuppressLint("MissingPermission")   // el permiso se revisa en la primera linea
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        if (!tienePermiso(context)) return false
        // Al tocar la notificacion se abre MainActivity (FLAG_IMMUTABLE: nadie puede cambiar el Intent)
        val intent = Intent(context, MainActivity::class.java)
        val pendiente = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        // Mejora IA: el recordatorio va por el canal urgente; lo demas por "Avisos"
        val urgente = aviso.origen == Origen.RECORDATORIO
        val notificacion = NotificationCompat.Builder(context, if (urgente) CANAL_URGENTE else CANAL)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setPriority(if (urgente) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendiente)
            .setAutoCancel(true)   // se quita del panel al tocarla
            .build()
        // El id del aviso hace que cada notificacion sea distinta (con el mismo id se reemplazaria)
        NotificationManagerCompat.from(context).notify(aviso.id, notificacion)
        return true
    }
}

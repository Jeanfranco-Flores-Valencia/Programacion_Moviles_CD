package com.tecsup.murocampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.ui.colorDeUsuario

/** Mejora IA: comentario como respuesta en un hilo. El avatar lleva la inicial del correo. */
@Composable
fun ComentarioHilo(comentario: Comentario, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)) {
        Avatar(
            texto = comentario.email.take(1).uppercase(),
            color = colorDeUsuario(comentario.id),
            tamano = 32.dp
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.padding(start = 12.dp)) {
            Text(comentario.email, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            Text(comentario.name, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
            Text(comentario.body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

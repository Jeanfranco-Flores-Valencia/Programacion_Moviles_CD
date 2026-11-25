package com.tecsup.murocampus.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Mejora IA: nombre y usuario del autor, como "Ana · @ana" en una red social. */
@Composable
fun EncabezadoAutor(userId: Int, detalle: String? = null, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text("Usuario $userId", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
        Text(
            listOfNotNull("@usuario$userId", detalle).joinToString(" · "),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

/** Autor con su avatar al lado (detalle y pantalla de crear). */
@Composable
fun AutorConAvatar(userId: Int, detalle: String? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AvatarUsuario(userId, tamano = 44.dp)
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text("Usuario $userId", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            Text(
                listOfNotNull("@usuario$userId", detalle).joinToString(" · "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

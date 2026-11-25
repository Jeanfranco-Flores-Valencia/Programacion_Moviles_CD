package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.ui.components.AutorConAvatar
import com.tecsup.murocampus.ui.components.Aviso
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

/** Largo maximo del contenido, como el limite de caracteres de una red social. */
private const val MAXIMO = 280

/** Mejora IA: pantalla para escribir una publicacion, con el autor arriba y un contador de caracteres. */
@Composable
fun PantallaCrear(vm: PostsViewModel, modifier: Modifier = Modifier) {
    val publicacion by vm.publicacion.collectAsStateWithLifecycle()
    var titulo by rememberSaveable { mutableStateOf("") }
    var contenido by rememberSaveable { mutableStateOf("") }

    // Si se publico, se limpia el formulario. Si fallo, se conserva lo escrito para reintentar
    LaunchedEffect(publicacion) {
        if (publicacion is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    val enviando = publicacion is UiState.Cargando
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = modifier.fillMaxWidth().padding(16.dp)) {
        AutorConAvatar(userId = 1, detalle = "publicando")
        OutlinedTextField(
            value = titulo, onValueChange = { titulo = it },
            label = { Text("Título") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contenido, onValueChange = { if (it.length <= MAXIMO) contenido = it },
            label = { Text("Contenido") },
            placeholder = { Text("¿Qué está pasando en el campus?") },
            supportingText = { Text("${contenido.length}/$MAXIMO", modifier = Modifier.fillMaxWidth()) },
            modifier = Modifier.fillMaxWidth().height(160.dp)
        )
        // Deshabilitado si falta el titulo o el contenido
        Button(
            onClick = { vm.publicar(titulo, contenido) },
            enabled = titulo.isNotBlank() && contenido.isNotBlank() && !enviando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (enviando) CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
            else Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Publicar")
        }
        when (val p = publicacion) {
            is UiState.Exito -> {
                Aviso("Publicado con id ${p.datos.id}", Color(0xFF2E7D32), Color(0xFFE8F5E9))
                Text(
                    "JSONPlaceholder simula el POST: la publicación no aparece en el muro.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            is UiState.Error -> Aviso(p.mensaje, MaterialTheme.colorScheme.onErrorContainer, MaterialTheme.colorScheme.errorContainer)
            else -> Unit
        }
    }
}

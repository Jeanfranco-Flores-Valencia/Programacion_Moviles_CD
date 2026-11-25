package com.tecsup.murocampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

/** Formulario para crear una publicacion con POST. */
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth().padding(16.dp)
    ) {
        OutlinedTextField(
            value = titulo, onValueChange = { titulo = it },
            label = { Text("Título") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contenido, onValueChange = { contenido = it },
            label = { Text("Contenido") },
            modifier = Modifier.fillMaxWidth().height(140.dp)
        )
        // Deshabilitado si falta el titulo o el contenido
        Button(
            onClick = { vm.publicar(titulo, contenido) },
            enabled = titulo.isNotBlank() && contenido.isNotBlank() && !enviando
        ) {
            if (enviando) CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
            else Text("Publicar")
        }
        when (val p = publicacion) {
            is UiState.Exito -> Aviso("Publicado con id ${p.datos.id}", Color(0xFF2E7D32), Color(0xFFE8F5E9))
            is UiState.Error -> Aviso(p.mensaje, MaterialTheme.colorScheme.onErrorContainer, MaterialTheme.colorScheme.errorContainer)
            else -> Unit
        }
    }
}

/** Recuadro de resultado (verde si se publico, rojo si fallo). */
@Composable
private fun Aviso(texto: String, color: Color, fondo: Color) {
    Surface(
        color = fondo,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(texto, color = color, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(12.dp))
    }
}

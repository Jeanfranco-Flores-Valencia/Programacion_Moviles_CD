package com.tecsup.murocampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Mejora IA: modo edicion del titulo dentro de la misma publicacion. Guardar se activa solo si el
 * titulo cambio y no esta vacio.
 */
@Composable
fun EditorTitulo(tituloActual: String, onGuardar: (String) -> Unit, onCancelar: () -> Unit, modifier: Modifier = Modifier) {
    var titulo by rememberSaveable { mutableStateOf(tituloActual) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = titulo, onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp, androidx.compose.ui.Alignment.End), modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
            Button(
                onClick = { onGuardar(titulo) },
                enabled = titulo.isNotBlank() && titulo.trim() != tituloActual
            ) { Text("Guardar") }
        }
    }
}

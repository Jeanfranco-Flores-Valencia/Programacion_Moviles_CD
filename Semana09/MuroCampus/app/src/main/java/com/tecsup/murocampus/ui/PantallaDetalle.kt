package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.ui.components.AccionesPublicacion
import com.tecsup.murocampus.ui.components.AutorConAvatar
import com.tecsup.murocampus.ui.components.ComentarioHilo
import com.tecsup.murocampus.ui.components.EditorTitulo
import com.tecsup.murocampus.viewmodel.DetalleUi
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

/** Mejora IA: la publicacion completa y sus comentarios como respuestas en un hilo. */
@Composable
fun PantallaDetalle(vm: PostsViewModel, id: Int, modifier: Modifier = Modifier) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val procesando by vm.procesando.collectAsStateWithLifecycle()
    var confirmarBorrado by rememberSaveable { mutableStateOf(false) }
    var editando by rememberSaveable { mutableStateOf(false) }

    // Avisa al ViewModel que se abrio este detalle; el ViewModel decide si hace falta pedirlo
    LaunchedEffect(id) { vm.abrir(id) }

    when (val e = estado) {
        UiState.Cargando -> Cargando("Cargando publicación...", modifier)
        is UiState.Error -> ErrorConReintento(e.mensaje, onReintentar = { vm.abrir(id) }, modifier = modifier)
        // Si todavia se ve el detalle de otra publicacion, se muestra la carga hasta que llegue el nuevo
        is UiState.Exito ->
            if (e.datos.post.id == id) Detalle(
                e.datos, procesando, editando,
                onEditar = { editando = true },
                onGuardarTitulo = { editando = false; vm.editarTitulo(id, it) },
                onCancelarEdicion = { editando = false },
                onEliminar = { confirmarBorrado = true },
                modifier = modifier
            )
            else Cargando("Cargando publicación...", modifier)
    }

    // Mejora IA: confirmar antes de borrar, como en cualquier red social
    if (confirmarBorrado) {
        AlertDialog(
            onDismissRequest = { confirmarBorrado = false },
            title = { Text("¿Eliminar publicación?") },
            text = { Text("Se enviará DELETE posts/$id. JSONPlaceholder solo simula el borrado: al actualizar el muro volverá a aparecer.") },
            confirmButton = {
                TextButton(onClick = { confirmarBorrado = false; vm.eliminar(id) }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = { TextButton(onClick = { confirmarBorrado = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun Detalle(
    datos: DetalleUi,
    procesando: Boolean,
    editando: Boolean,
    onEditar: () -> Unit,
    onGuardarTitulo: (String) -> Unit,
    onCancelarEdicion: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val post = datos.post
    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp), modifier = modifier.fillMaxSize()) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
                AutorConAvatar(post.userId, detalle = "#${post.id}")
                // Mejora IA: en modo edicion el titulo se vuelve un campo con Guardar (PATCH)
                if (editando) EditorTitulo(post.title, onGuardar = onGuardarTitulo, onCancelar = onCancelarEdicion)
                else Text(post.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(post.body, style = MaterialTheme.typography.bodyLarge)
                if (!editando) AccionesPublicacion(habilitado = !procesando, onEliminar = onEliminar, onEditar = onEditar)
            }
            HorizontalDivider()
            Text(
                "Comentarios (${datos.comentarios.size})",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp)
            )
        }
        items(datos.comentarios, key = { it.id }) {
            ComentarioHilo(it)
            HorizontalDivider(modifier = Modifier.padding(start = 60.dp))
        }
    }
}

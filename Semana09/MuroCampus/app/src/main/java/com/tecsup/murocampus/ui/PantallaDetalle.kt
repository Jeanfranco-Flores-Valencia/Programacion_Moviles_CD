package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.ui.components.AutorConAvatar
import com.tecsup.murocampus.ui.components.ComentarioHilo
import com.tecsup.murocampus.viewmodel.DetalleUi
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

/** Mejora IA: la publicacion completa y sus comentarios como respuestas en un hilo. */
@Composable
fun PantallaDetalle(vm: PostsViewModel, id: Int, modifier: Modifier = Modifier) {
    val estado by vm.detalle.collectAsStateWithLifecycle()

    // Avisa al ViewModel que se abrio este detalle; el ViewModel decide si hace falta pedirlo
    LaunchedEffect(id) { vm.abrir(id) }

    when (val e = estado) {
        UiState.Cargando -> Cargando("Cargando publicación...", modifier)
        is UiState.Error -> ErrorConReintento(e.mensaje, onReintentar = { vm.abrir(id) }, modifier = modifier)
        // Si todavia se ve el detalle de otra publicacion, se muestra la carga hasta que llegue el nuevo
        is UiState.Exito ->
            if (e.datos.post.id == id) Detalle(e.datos, modifier)
            else Cargando("Cargando publicación...", modifier)
    }
}

@Composable
private fun Detalle(datos: DetalleUi, modifier: Modifier = Modifier) {
    val post = datos.post
    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp), modifier = modifier.fillMaxSize()) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
                AutorConAvatar(post.userId, detalle = "#${post.id}")
                Text(post.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(post.body, style = MaterialTheme.typography.bodyLarge)
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

package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.viewmodel.DetalleUi
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

/** Detalle de una publicacion con sus comentarios. El boton Atras del sistema vuelve a la lista. */
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
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)) {
                    EtiquetaUsuario(datos.post.userId)
                    Text(datos.post.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(datos.post.body, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            Text(
                "Comentarios (${datos.comentarios.size})",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        items(datos.comentarios, key = { it.id }) { TarjetaComentario(it) }
    }
}

@Composable
private fun TarjetaComentario(c: Comentario) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.padding(12.dp)) {
            Text(c.email, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text(c.body, style = MaterialTheme.typography.bodySmall)
        }
    }
}

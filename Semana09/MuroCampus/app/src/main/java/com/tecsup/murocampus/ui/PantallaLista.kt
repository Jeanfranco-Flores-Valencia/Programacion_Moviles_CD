package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.model.Post
import com.tecsup.murocampus.ui.components.ChipsUsuarios
import com.tecsup.murocampus.ui.components.PublicacionFeed
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

/**
 * Mejora IA: el muro como el feed de una red social. Chips de usuario arriba, publicaciones
 * separadas por lineas, deslizar hacia abajo para actualizar y el boton flotante "Publicar".
 * Solo dibuja el estado: nunca llama a la API.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onAbrir: (Int) -> Unit,
    onCrear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val refrescando by vm.refrescando.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            ChipsUsuarios(filtro, onElegir = vm::filtrar, modifier = Modifier.padding(vertical = 8.dp))
            HorizontalDivider()
            when (val e = estado) {
                UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorConReintento(e.mensaje, onReintentar = vm::cargar)
                is UiState.Exito -> PullToRefreshBox(isRefreshing = refrescando, onRefresh = vm::refrescar) {
                    Feed(e.datos, onAbrir)
                }
            }
        }
        ExtendedFloatingActionButton(
            onClick = onCrear,
            icon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
            text = { Text("Publicar") },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        )
    }
}

@Composable
private fun Feed(posts: List<Post>, onAbrir: (Int) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(bottom = 96.dp), modifier = Modifier.fillMaxSize()) {
        if (posts.isEmpty()) {
            item {
                Text(
                    "Sin publicaciones",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(32.dp)
                )
            }
        }
        items(posts, key = { it.id }) { post ->
            PublicacionFeed(post, onClick = { onAbrir(post.id) })
            HorizontalDivider()
        }
    }
}

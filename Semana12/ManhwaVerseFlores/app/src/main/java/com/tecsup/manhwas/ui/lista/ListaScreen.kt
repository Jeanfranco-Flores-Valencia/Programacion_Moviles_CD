package com.tecsup.manhwas.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.manhwas.ui.UiState
import com.tecsup.manhwas.ui.components.Cargando
import com.tecsup.manhwas.ui.components.ErrorView
import com.tecsup.manhwas.ui.components.ItemCard
import com.tecsup.manhwas.ui.theme.Colores

/** Catalogo: la pantalla solo observa el estado y avisa los clics al ViewModel. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    vm: ListaViewModel,
    onItem: (Int) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val aviso by vm.aviso.collectAsStateWithLifecycle()
    val refrescando by vm.refrescando.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Colores.Fondo,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("ManhwaVerse", fontWeight = FontWeight.Bold)
                        Text("Acción · Fantasía · Mazmorras", style = MaterialTheme.typography.labelMedium, color = Colores.Cian)
                    }
                },
                actions = {
                    if (refrescando) {
                        CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.padding(12.dp).size(22.dp))
                    } else {
                        IconButton(onClick = vm::refrescar) { Icon(Icons.Outlined.Refresh, contentDescription = "Actualizar") }
                    }
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Colores.Fondo)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            aviso?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Colores.Dorado.copy(alpha = 0.15f))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Outlined.CloudOff, contentDescription = null, tint = Colores.Dorado, modifier = Modifier.size(18.dp))
                    Text(it, style = MaterialTheme.typography.bodySmall, color = Colores.Texto)
                }
            }

            when (val e = estado) {
                is UiState.Cargando -> Cargando("Abriendo el portal del catálogo...")
                is UiState.Error -> ErrorView(e.mensaje, onReintentar = vm::refrescar)
                is UiState.Exito -> if (e.datos.isEmpty()) {
                    ErrorView("No hay manhwas guardados. Toca Actualizar para descargarlos.", onReintentar = vm::refrescar, icono = Icons.Outlined.Inbox)
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(e.datos, key = { it.id }) { item ->
                            ItemCard(item = item, onClick = { onItem(item.id) }, onFavorito = { vm.alternarFavorito(item.id) })
                        }
                    }
                }
            }
        }
    }
}

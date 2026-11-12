package com.tecsup.manhwas.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.manhwas.ui.UiState
import com.tecsup.manhwas.ui.components.Cargando
import com.tecsup.manhwas.ui.components.EncabezadoSeccion
import com.tecsup.manhwas.ui.components.ErrorView
import com.tecsup.manhwas.ui.components.TarjetaManhwa
import com.tecsup.manhwas.ui.theme.Colores

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(vm: FavoritosViewModel, onItem: (Int) -> Unit, onExplorar: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Colores.Fondo,
        topBar = {
            TopAppBar(
                title = { Text("Mi biblioteca", color = Colores.Texto) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Colores.Fondo)
            )
        }
    ) { padding ->
        when (val e = estado) {
            is UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(e.mensaje)
            is UiState.Exito -> if (e.datos.isEmpty()) {
                // Mejora IA: el estado vacio lleva directo al catalogo
                ErrorView(
                    "Aún no tienes favoritos. Toca el corazón de un manhwa para guardarlo aquí.",
                    onReintentar = onExplorar,
                    icono = Icons.Filled.FavoriteBorder,
                    textoBoton = "Explorar catálogo",
                    iconoBoton = Icons.Outlined.AutoStories
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 140.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize().padding(padding)
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        EncabezadoSeccion("Guardados", detalle = "${e.datos.size} títulos")
                    }
                    items(e.datos, key = { it.id }) { item ->
                        TarjetaManhwa(item, onClick = { onItem(item.id) }, onFavorito = { vm.alternarFavorito(item.id) })
                    }
                }
            }
        }
    }
}

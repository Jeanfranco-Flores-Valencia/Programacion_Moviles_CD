package com.tecsup.manhwas.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.manhwas.ui.UiState
import com.tecsup.manhwas.ui.components.Aviso
import com.tecsup.manhwas.ui.components.BarraBusqueda
import com.tecsup.manhwas.ui.components.BotonIcono
import com.tecsup.manhwas.ui.components.Chip
import com.tecsup.manhwas.ui.components.EncabezadoSeccion
import com.tecsup.manhwas.ui.components.ErrorView
import com.tecsup.manhwas.ui.components.EsqueletoCatalogo
import com.tecsup.manhwas.ui.components.Logo
import com.tecsup.manhwas.ui.components.TarjetaDestacada
import com.tecsup.manhwas.ui.components.TarjetaManhwa
import com.tecsup.manhwas.ui.components.TipoAviso
import com.tecsup.manhwas.ui.theme.Colores

/**
 * Catalogo. La pantalla solo observa el estado y avisa los clics al ViewModel.
 * Mejora IA: diseno de web de lectura (destacado, busqueda, chips de genero y cuadricula de portadas).
 */
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
    val visibles by vm.visibles.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val generos by vm.generos.collectAsStateWithLifecycle()
    val genero by vm.genero.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Colores.Fondo,
        topBar = {
            TopAppBar(
                title = { Logo() },
                actions = {
                    if (refrescando) {
                        CircularProgressIndicator(color = Colores.Morado, strokeWidth = 2.dp, modifier = Modifier.padding(10.dp).size(22.dp))
                    } else {
                        BotonIcono(Icons.Outlined.Refresh, "Actualizar", vm::refrescar)
                    }
                    BotonIcono(Icons.AutoMirrored.Outlined.Logout, "Cerrar sesión", onCerrarSesion, Modifier.padding(horizontal = 8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Colores.Fondo)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val e = estado) {
                is UiState.Cargando -> EsqueletoCatalogo()
                is UiState.Error -> ErrorView(e.mensaje, onReintentar = vm::refrescar)
                is UiState.Exito -> if (e.datos.isEmpty()) {
                    ErrorView(
                        "No hay manhwas guardados.",
                        onReintentar = vm::refrescar,
                        icono = Icons.Outlined.Inbox,
                        textoBoton = "Descargar catálogo"
                    )
                } else {
                    val filtrando = busqueda.isNotBlank() || genero != null
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 140.dp),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        aviso?.let {
                            item(span = { GridItemSpan(maxLineSpan) }) { Aviso(it, TipoAviso.SinConexion) }
                        }
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            BarraBusqueda(busqueda, vm::buscar)
                        }
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                item { Chip("Todos", seleccionado = genero == null, onClick = { vm.elegirGenero(null) }) }
                                items(generos) { g -> Chip(g, seleccionado = genero == g, onClick = { vm.elegirGenero(g) }) }
                            }
                        }
                        // El mas leido arriba, solo cuando no se esta buscando
                        if (!filtrando) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                TarjetaDestacada(e.datos.first(), onClick = { onItem(e.datos.first().id) })
                            }
                        }
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            EncabezadoSeccion(
                                if (filtrando) "Resultados" else "Populares",
                                detalle = "${visibles.size} títulos"
                            )
                        }
                        if (visibles.isEmpty()) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                    ErrorView("No se encontró ningún manhwa con ese filtro.", icono = Icons.Outlined.SearchOff)
                                }
                            }
                        }
                        items(visibles, key = { it.id }) { item ->
                            TarjetaManhwa(item, onClick = { onItem(item.id) }, onFavorito = { vm.alternarFavorito(item.id) })
                        }
                    }
                }
            }
        }
    }
}

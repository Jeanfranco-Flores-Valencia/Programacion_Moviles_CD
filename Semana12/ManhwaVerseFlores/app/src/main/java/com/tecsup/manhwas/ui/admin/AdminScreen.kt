package com.tecsup.manhwas.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.manhwas.ui.UiState
import com.tecsup.manhwas.ui.components.Aviso
import com.tecsup.manhwas.ui.components.BotonIcono
import com.tecsup.manhwas.ui.components.BotonPeligro
import com.tecsup.manhwas.ui.components.BotonPrimario
import com.tecsup.manhwas.ui.components.Cargando
import com.tecsup.manhwas.ui.components.EncabezadoSeccion
import com.tecsup.manhwas.ui.components.TarjetaEstadistica
import com.tecsup.manhwas.ui.components.TipoAviso
import com.tecsup.manhwas.ui.theme.Colores

/** Panel solo para el administrador: totales, Sincronizar y Limpiar cache. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(vm: AdminViewModel, onCerrarSesion: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val ocupado by vm.ocupado.collectAsStateWithLifecycle()
    var confirmarLimpieza by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = Colores.Fondo,
        topBar = {
            TopAppBar(
                title = { Text("Panel del administrador", color = Colores.Texto) },
                actions = {
                    BotonIcono(Icons.AutoMirrored.Outlined.Logout, "Cerrar sesión", onCerrarSesion, Modifier.padding(horizontal = 8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Colores.Fondo)
            )
        }
    ) { padding ->
        when (val e = estado) {
            is UiState.Cargando -> Cargando()
            is UiState.Error -> Aviso(e.mensaje, TipoAviso.Error, Modifier.padding(padding).padding(16.dp))
            is UiState.Exito -> Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp)
            ) {
                EncabezadoSeccion("Resumen del catálogo")
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaEstadistica(e.datos.guardados, "Manhwas guardados", Icons.Outlined.AutoStories, Colores.MoradoClaro, Modifier.weight(1f))
                    TarjetaEstadistica(e.datos.favoritos, "Favoritos", Icons.Outlined.Favorite, Colores.Rojo, Modifier.weight(1f))
                }

                EncabezadoSeccion("Acciones")
                Text(
                    "Sincronizar descarga otra vez el catálogo y conserva los favoritos. Limpiar caché borra todo lo guardado en el celular.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Colores.TextoSuave
                )
                BotonPrimario("Sincronizar", vm::sincronizar, icono = Icons.Outlined.Sync, cargando = ocupado)
                BotonPeligro("Limpiar caché", { confirmarLimpieza = true }, icono = Icons.Outlined.DeleteSweep, habilitado = !ocupado)

                mensaje?.let { Aviso(it, TipoAviso.Info) }
            }
        }
    }

    if (confirmarLimpieza) {
        AlertDialog(
            onDismissRequest = { confirmarLimpieza = false },
            title = { Text("¿Limpiar caché?") },
            text = { Text("Se borrarán todos los manhwas guardados y los favoritos del celular.") },
            confirmButton = {
                TextButton(onClick = { confirmarLimpieza = false; vm.limpiarCache() }) { Text("Limpiar", color = Colores.Rojo) }
            },
            dismissButton = { TextButton(onClick = { confirmarLimpieza = false }) { Text("Cancelar", color = Colores.MoradoClaro) } },
            containerColor = Colores.Superficie,
            titleContentColor = Colores.Texto,
            textContentColor = Colores.TextoSuave
        )
    }
}

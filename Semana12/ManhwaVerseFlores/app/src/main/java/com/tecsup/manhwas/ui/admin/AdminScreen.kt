package com.tecsup.manhwas.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.manhwas.ui.UiState
import com.tecsup.manhwas.ui.components.Cargando
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
                title = { Text("Panel del administrador", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Colores.Fondo)
            )
        }
    ) { padding ->
        when (val e = estado) {
            is UiState.Cargando -> Cargando()
            is UiState.Error -> Text(e.mensaje, color = Colores.Rojo, modifier = Modifier.padding(padding).padding(16.dp))
            is UiState.Exito -> Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Total("Manhwas guardados", e.datos.guardados, Colores.Cian, Modifier.weight(1f))
                    Total("Favoritos", e.datos.favoritos, Colores.Rojo, Modifier.weight(1f))
                }

                Button(
                    onClick = vm::sincronizar,
                    enabled = !ocupado,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    if (ocupado) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                    } else {
                        Icon(Icons.Outlined.Sync, contentDescription = null)
                        Text("  Sincronizar")
                    }
                }
                OutlinedButton(
                    onClick = { confirmarLimpieza = true },
                    enabled = !ocupado,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Icon(Icons.Outlined.DeleteSweep, contentDescription = null, tint = Colores.Rojo)
                    Text("  Limpiar caché", color = Colores.Rojo)
                }

                mensaje?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Colores.Texto,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Colores.Superficie)
                            .padding(14.dp)
                    )
                }
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
            dismissButton = { TextButton(onClick = { confirmarLimpieza = false }) { Text("Cancelar") } },
            containerColor = Colores.Superficie
        )
    }
}

@Composable
private fun Total(titulo: String, valor: Int, color: Color, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Colores.Superficie)
            .padding(16.dp)
    ) {
        Text(valor.toString(), fontSize = 34.sp, fontWeight = FontWeight.Bold, color = color)
        Text(titulo, style = MaterialTheme.typography.bodySmall, color = Colores.TextoSuave)
    }
}

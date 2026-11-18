package com.floresv.campusgps.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.floresv.campusgps.model.UbicacionUi
import com.floresv.campusgps.ui.components.BarraSuperior
import com.floresv.campusgps.ui.components.ChipsCategorias
import com.floresv.campusgps.ui.components.TarjetaMasCercano
import com.floresv.campusgps.viewmodel.MapaViewModel

private val permisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

private fun tienePermiso(contexto: Context) =
    permisos.any { ContextCompat.checkSelfPermission(contexto, it) == PackageManager.PERMISSION_GRANTED }

/**
 * Mejora IA: pantalla al estilo de una app de mapas. El mapa ocupa toda la pantalla, arriba flota
 * una barra con el estado, abajo una hoja deslizable con el mas cercano y la lista, y un boton
 * flotante "Mi ubicacion". Solo dibuja el estado del ViewModel y le avisa los eventos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.visibles.collectAsState()
    val categoria by vm.categoria.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val contexto = LocalContext.current
    var permisoConcedido by remember { mutableStateOf(tienePermiso(contexto)) }

    // Android dibuja el cuadro de permiso; la app solo recibe la respuesta
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultado ->
        permisoConcedido = resultado.values.any { it }
        if (permisoConcedido) vm.buscar() else vm.permisoDenegado()
    }

    fun alPulsar() {
        if (estado is UbicacionUi.Buscando) return
        if (tienePermiso(contexto)) {
            permisoConcedido = true
            vm.buscar()
        } else {
            lanzador.launch(permisos)
        }
    }

    BottomSheetScaffold(
        scaffoldState = rememberBottomSheetScaffoldState(),
        sheetPeekHeight = 210.dp,
        sheetContent = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                TarjetaMasCercano(
                    masCercano = lugares.firstOrNull { it.lugar.id == masCercano?.id },
                    onIr = { masCercano?.let { vm.seleccionar(it.id) } },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(categoria ?: "Lugares del campus", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("${lugares.size} lugares", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    onClick = vm::seleccionar,
                    modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp)
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercanoId = masCercano?.id,
                permisoConcedido = permisoConcedido,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 140.dp, bottom = padding.calculateBottomPadding())
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.statusBarsPadding().padding(12.dp)
            ) {
                BarraSuperior(estado)
                ChipsCategorias(categorias = vm.categorias, elegida = categoria, onElegir = vm::elegirCategoria)
                (estado as? UbicacionUi.Error)?.let { error ->
                    MensajeError(
                        error = error,
                        onAbrirAjustes = {
                            // Abre la pantalla de la app en Ajustes para conceder el permiso a mano
                            contexto.startActivity(
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", contexto.packageName, null))
                            )
                        },
                        onReintentar = ::alPulsar
                    )
                }
            }

            ExtendedFloatingActionButton(
                onClick = ::alPulsar,
                icon = {
                    if (estado is UbicacionUi.Buscando) CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
                    else Icon(Icons.Filled.MyLocation, contentDescription = null)
                },
                text = { Text(if (estado is UbicacionUi.Buscando) "Buscando..." else "Mi ubicación") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = padding.calculateBottomPadding() + 16.dp)
            )
        }
    }
}

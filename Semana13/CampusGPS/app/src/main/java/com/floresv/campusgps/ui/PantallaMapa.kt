package com.floresv.campusgps.ui

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.floresv.campusgps.model.Lugar
import com.floresv.campusgps.model.LugarConDistancia
import com.floresv.campusgps.model.UbicacionUi
import com.floresv.campusgps.ui.components.BarraSuperior
import com.floresv.campusgps.ui.components.ChipsCategorias
import com.floresv.campusgps.ui.components.FichaLugar
import com.floresv.campusgps.ui.components.SelectorRadio
import com.floresv.campusgps.ui.components.TarjetaMasCercano
import com.floresv.campusgps.util.formatearDistancia
import com.floresv.campusgps.viewmodel.MapaViewModel

private val permisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

private fun tienePermiso(contexto: Context) =
    permisos.any { ContextCompat.checkSelfPermission(contexto, it) == PackageManager.PERMISSION_GRANTED }

/** Abre la app de mapas o navegacion del celular con el lugar como destino. */
private fun comoLlegar(contexto: Context, lugar: Lugar) {
    val geo = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${lugar.latitud},${lugar.longitud}(${Uri.encode(lugar.nombre)})"))
    try {
        contexto.startActivity(geo)
    } catch (e: ActivityNotFoundException) {
        // Sin app de mapas: se abre OpenStreetMap en el navegador
        contexto.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse("https://www.openstreetmap.org/?mlat=${lugar.latitud}&mlon=${lugar.longitud}#map=18/${lugar.latitud}/${lugar.longitud}"))
        )
    }
}

/** Comparte el nombre, la distancia y el enlace del lugar con cualquier app (WhatsApp, correo...). */
private fun compartir(contexto: Context, item: LugarConDistancia) {
    val lugar = item.lugar
    val texto = buildString {
        append(lugar.nombre)
        item.metros?.let { append(" (a ${formatearDistancia(it)} de mí)") }
        append("\nhttps://www.openstreetmap.org/?mlat=${lugar.latitud}&mlon=${lugar.longitud}#map=18/${lugar.latitud}/${lugar.longitud}")
    }
    val envio = Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, texto)
    contexto.startActivity(Intent.createChooser(envio, "Compartir lugar"))
}

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
    val radio by vm.radio.collectAsState()
    val dentroDelRadio by vm.dentroDelRadio.collectAsState()
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

    val hoja = rememberBottomSheetScaffoldState()
    // Cada vez que se pulsa "Centrar" el mapa vuelve al lugar de la ficha
    var centrar by remember { mutableIntStateOf(0) }

    // Al elegir un lugar (en la lista o en el mapa) la ficha se abre completa
    LaunchedEffect(seleccionado) {
        if (seleccionado != null) hoja.bottomSheetState.expand()
    }
    // Atras cierra la ficha antes de salir de la app
    BackHandler(enabled = seleccionado != null) { vm.seleccionar(null) }

    BottomSheetScaffold(
        scaffoldState = hoja,
        sheetPeekHeight = 210.dp,
        sheetContent = {
            val ficha = lugares.firstOrNull { it.lugar.id == seleccionado }
            if (ficha != null) {
                // Mejora IA: con un lugar elegido, la hoja muestra su ficha en vez de la lista
                FichaLugar(
                    item = ficha,
                    esMasCercano = ficha.lugar.id == masCercano?.id,
                    dentroDelRadio = ficha.lugar.id in dentroDelRadio,
                    onCerrar = { vm.seleccionar(null) },
                    onComoLlegar = { comoLlegar(contexto, ficha.lugar) },
                    onCentrar = { centrar++ },
                    onCompartir = { compartir(contexto, ficha) },
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    TarjetaMasCercano(
                        masCercano = lugares.firstOrNull { it.lugar.id == masCercano?.id },
                        onIr = { masCercano?.let { vm.seleccionar(it.id) } },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    SelectorRadio(
                        opciones = vm.opcionesRadio,
                        elegido = radio,
                        dentro = dentroDelRadio.size,
                        habilitado = estado is UbicacionUi.Lista,
                        onElegir = vm::elegirRadio,
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
                        dentroDelRadio = dentroDelRadio,
                        modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp)
                    )
                }
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
                radio = radio,
                centrar = centrar,
                onMarcador = vm::seleccionar,
                modifier = Modifier.fillMaxSize()
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

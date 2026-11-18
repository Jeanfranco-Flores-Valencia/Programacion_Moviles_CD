package com.floresv.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.floresv.campusgps.model.UbicacionUi
import com.floresv.campusgps.viewmodel.MapaViewModel

/** Ejemplo resuelto: pide la ubicacion y muestra latitud y longitud. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val contexto = LocalContext.current

    // Android dibuja el cuadro de permiso; la app solo recibe la respuesta
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) vm.buscar() else vm.permisoDenegado()
    }

    fun alPulsar() {
        val tienePermiso = listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .any { ContextCompat.checkSelfPermission(contexto, it) == PackageManager.PERMISSION_GRANTED }
        if (tienePermiso) vm.buscar()
        else lanzador.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CampusGPS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Deshabilitado mientras busca
                Button(onClick = ::alPulsar, enabled = estado !is UbicacionUi.Buscando) { Text("Obtener mi ubicación") }

                when (val e = estado) {
                    UbicacionUi.Inicial -> Text("Pulsa el botón para ver dónde estás")
                    UbicacionUi.Buscando -> Text("Buscando...")
                    is UbicacionUi.Lista -> Text("Lat: ${e.posicion.latitud}   Lon: ${e.posicion.longitud}")
                    is UbicacionUi.Error -> Text(e.mensaje, color = MaterialTheme.colorScheme.error)
                }
            }

            // El mapa ocupa el espacio libre
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                modifier = Modifier.fillMaxWidth().weight(1f)
            )

            // La lista va debajo con altura fija
            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                onClick = vm::seleccionar,
                modifier = Modifier.fillMaxWidth().height(220.dp)
            )
        }
    }
}

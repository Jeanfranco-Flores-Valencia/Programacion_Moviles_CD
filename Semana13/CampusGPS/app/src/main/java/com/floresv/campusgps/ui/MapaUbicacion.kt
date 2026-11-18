package com.floresv.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.floresv.campusgps.model.LugarConDistancia
import com.floresv.campusgps.model.UbicacionUi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

/** Mapa de Google: solo dibuja lo que recibe (estado, lugares y seleccionado). */
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    modifier: Modifier = Modifier
) {
    // Camara inicial: Lima, zoom 12
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }

    // Cuando llega la ubicacion, la camara viaja hasta ella
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val p = estado.posicion
            camara.animate(CameraUpdateFactory.newLatLngZoom(LatLng(p.latitud, p.longitud), 16f))
        }
    }

    // Al elegir un lugar en la lista, la camara viaja hasta el con zoom 17
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar ?: return@LaunchedEffect
        camara.animate(CameraUpdateFactory.newLatLngZoom(LatLng(lugar.latitud, lugar.longitud), 17f))
    }

    GoogleMap(modifier = modifier, cameraPositionState = camara) {
        if (estado is UbicacionUi.Lista) {
            Marker(
                state = rememberUpdatedMarkerState(position = LatLng(estado.posicion.latitud, estado.posicion.longitud)),
                title = "Estás aquí"
            )
        }
        // Un marcador por cada lugar del campus: azul, y verde el mas cercano
        lugares.forEach { item ->
            key(item.lugar.id) {
                val marcador = rememberUpdatedMarkerState(position = LatLng(item.lugar.latitud, item.lugar.longitud))
                // El lugar seleccionado muestra su nombre en el mapa
                LaunchedEffect(seleccionado) {
                    if (seleccionado == item.lugar.id) marcador.showInfoWindow()
                }
                Marker(
                    state = marcador,
                    title = item.lugar.nombre,
                    snippet = item.lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (item.lugar.id == masCercanoId) BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_AZURE
                    )
                )
            }
        }
    }
}

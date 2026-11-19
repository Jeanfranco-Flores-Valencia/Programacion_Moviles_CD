package com.floresv.campusgps.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.floresv.campusgps.model.LugarConDistancia
import com.floresv.campusgps.model.UbicacionUi
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

/** Colores de los marcadores (los mismos de la guia: rojo, azul y verde). */
private const val ROJO = 0xFFE53935.toInt()
private const val AZUL = 0xFF1E88E5.toInt()
private const val VERDE = 0xFF43A047.toInt()

/** Marcador por defecto de osmdroid pintado del color indicado. */
private fun icono(contexto: Context, color: Int): Drawable =
    ContextCompat.getDrawable(contexto, org.osmdroid.library.R.drawable.marker_default)!!.mutate().apply { setTint(color) }

/**
 * Mapa de OpenStreetMap (osmdroid): gratis, sin clave de API. Solo dibuja lo que recibe
 * (estado, lugares, seleccionado y mas cercano); no calcula nada ni consulta repositorios.
 */
@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val mapa = remember {
        // OpenStreetMap pide identificar la app que descarga los mosaicos
        Configuration.getInstance().userAgentValue = contexto.packageName
        MapView(contexto).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            // Camara inicial: Lima, zoom 12
            controller.setZoom(12.0)
            controller.setCenter(GeoPoint(-12.0464, -77.0428))
        }
    }

    // El mapa sigue el ciclo de vida de la pantalla
    val ciclo = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(ciclo) {
        val observador = LifecycleEventObserver { _, evento ->
            when (evento) {
                Lifecycle.Event.ON_RESUME -> mapa.onResume()
                Lifecycle.Event.ON_PAUSE -> mapa.onPause()
                else -> Unit
            }
        }
        ciclo.addObserver(observador)
        onDispose {
            ciclo.removeObserver(observador)
            mapa.onDetach()
        }
    }

    // Cuando llega la ubicacion, la camara viaja hasta ella (zoom 16)
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            mapa.controller.animateTo(GeoPoint(estado.posicion.latitud, estado.posicion.longitud), 16.0, 1000L)
        }
    }

    // Al elegir un lugar en la lista, la camara viaja hasta el con zoom 17
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar ?: return@LaunchedEffect
        mapa.controller.animateTo(GeoPoint(lugar.latitud, lugar.longitud), 17.0, 1000L)
    }

    AndroidView(
        factory = { mapa },
        modifier = modifier,
        update = { vista ->
            // Se vuelven a dibujar los marcadores con el estado actual
            InfoWindow.closeAllInfoWindowsOn(vista)
            vista.overlays.removeAll { it is Marker }

            // Un marcador por cada lugar: azul, y verde el mas cercano (nombre y categoria al tocarlo)
            lugares.forEach { item ->
                val marcador = Marker(vista).apply {
                    position = GeoPoint(item.lugar.latitud, item.lugar.longitud)
                    title = item.lugar.nombre
                    snippet = item.lugar.categoria
                    icon = icono(vista.context, if (item.lugar.id == masCercanoId) VERDE else AZUL)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                vista.overlays.add(marcador)
                // El lugar seleccionado muestra su nombre en el mapa
                if (item.lugar.id == seleccionado) marcador.showInfoWindow()
            }

            // Tu posicion en rojo
            if (estado is UbicacionUi.Lista) {
                vista.overlays.add(Marker(vista).apply {
                    position = GeoPoint(estado.posicion.latitud, estado.posicion.longitud)
                    title = "Estás aquí"
                    icon = icono(vista.context, ROJO)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                })
            }
            vista.invalidate()
        }
    )
}

package com.floresv.campusgps.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.CenterFocusStrong
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresv.campusgps.model.LugarConDistancia
import com.floresv.campusgps.util.formatearDistancia
import com.floresv.campusgps.util.minutosCaminando

/**
 * Mejora IA: ficha del lugar como en una app de mapas. Arriba el nombre y los datos rapidos, luego
 * los botones de accion, la foto, el horario y la descripcion. No conoce el ViewModel: recibe el
 * lugar y avisa cada accion.
 */
@Composable
fun FichaLugar(
    item: LugarConDistancia,
    esMasCercano: Boolean,
    dentroDelRadio: Boolean,
    onCerrar: () -> Unit,
    onComoLlegar: () -> Unit,
    onCentrar: () -> Unit,
    onCompartir: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lugar = item.lugar
    Column(verticalArrangement = Arrangement.spacedBy(14.dp), modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(lugar.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(
                    listOfNotNull(lugar.categoria, item.metros?.let { "a ${formatearDistancia(it)}" }).joinToString(" · "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 6.dp)) {
                    if (esMasCercano) InsigniaMasCercano()
                    if (dentroDelRadio) InsigniaRadio()
                }
            }
            IconButton(
                onClick = onCerrar,
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
            ) { Icon(Icons.Outlined.Close, contentDescription = "Cerrar") }
        }

        // Acciones principales, en circulos con su texto debajo
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            BotonAccion(Icons.Filled.Directions, "Cómo llegar", principal = true, onClick = onComoLlegar)
            BotonAccion(Icons.Outlined.CenterFocusStrong, "Centrar", onClick = onCentrar)
            BotonAccion(Icons.Outlined.Share, "Compartir", onClick = onCompartir)
        }

        FotoLugar(lugar, Modifier.fillMaxWidth().height(190.dp), esquinas = 20.dp)

        if (lugar.horario.isNotBlank()) DatoLugar(Icons.Outlined.Schedule, lugar.horario)
        item.metros?.let { DatoLugar(Icons.AutoMirrored.Outlined.DirectionsWalk, "${minutosCaminando(it)} min caminando") }
        if (lugar.descripcion.isNotBlank()) {
            HorizontalDivider()
            Text("Acerca de este lugar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(lugar.descripcion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun BotonAccion(icono: ImageVector, texto: String, principal: Boolean = false, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (principal) {
            FilledIconButton(onClick = onClick, modifier = Modifier.size(52.dp)) { Icon(icono, contentDescription = texto) }
        } else {
            FilledTonalIconButton(onClick = onClick, modifier = Modifier.size(52.dp)) { Icon(icono, contentDescription = texto) }
        }
        Text(texto, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun DatoLugar(icono: ImageVector, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Text(texto, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 12.dp))
    }
}

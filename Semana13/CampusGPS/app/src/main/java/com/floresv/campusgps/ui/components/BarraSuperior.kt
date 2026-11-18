package com.floresv.campusgps.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresv.campusgps.model.UbicacionUi
import java.util.Locale

/** Mejora IA: barra flotante sobre el mapa (como el buscador de Google Maps) con el estado de la ubicacion. */
@Composable
fun BarraSuperior(estado: UbicacionUi, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        shadowElevation = 6.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Icon(Icons.Filled.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text("CampusGPS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    when (estado) {
                        UbicacionUi.Inicial -> "Encuentra lo más cercano en el campus"
                        UbicacionUi.Buscando -> "Buscando tu ubicación..."
                        is UbicacionUi.Lista -> String.format(Locale.US, "Estás en %.4f, %.4f", estado.posicion.latitud, estado.posicion.longitud)
                        is UbicacionUi.Error -> "No se pudo obtener tu ubicación"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

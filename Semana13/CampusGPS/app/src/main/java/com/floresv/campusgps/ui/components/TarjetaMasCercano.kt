package com.floresv.campusgps.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresv.campusgps.model.LugarConDistancia
import com.floresv.campusgps.util.formatearDistancia

/**
 * Mejora IA: tarjeta destacada del lugar mas cercano, como "Lo mas cerca de ti" en una app de mapas.
 * Si todavia no hay ubicacion, muestra una pista y el boton queda deshabilitado.
 */
@Composable
fun TarjetaMasCercano(masCercano: LugarConDistancia?, onIr: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            if (masCercano != null) IconoCategoria(masCercano.lugar.categoria, tamano = 48.dp)
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text("Más cercano", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                if (masCercano != null) {
                    Text(masCercano.lugar.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "A ${masCercano.metros?.let(::formatearDistancia)} de ti",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                } else {
                    Text("Toca «Mi ubicación» para ordenar los lugares por distancia", style = MaterialTheme.typography.bodyMedium)
                }
            }
            FilledTonalButton(onClick = onIr, enabled = masCercano != null) {
                Icon(Icons.Outlined.NearMe, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Ir")
            }
        }
    }
}

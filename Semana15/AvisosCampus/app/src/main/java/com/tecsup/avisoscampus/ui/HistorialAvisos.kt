package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.model.Origen

/** Tarjeta Historial: los 20 avisos mas recientes. No conoce el ViewModel, solo recibe la lista. */
@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Tarjeta("Historial") {
        if (avisos.isEmpty()) {
            Text("Sin avisos todavía", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            // Column con forEach: son pocas filas y la pantalla ya se desplaza (no LazyColumn)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                avisos.take(20).forEach { FilaAviso(it) }
            }
        }
    }
}

@Composable
private fun FilaAviso(aviso: Aviso) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(10.dp)
    ) {
        EtiquetaOrigen(aviso.origen)
        Column(modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
            Text(aviso.titulo, fontWeight = FontWeight.Bold)
            Text(aviso.texto, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(aviso.hora, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** Etiqueta de color con el origen del aviso. */
@Composable
private fun EtiquetaOrigen(origen: Origen) {
    val color = when (origen) {
        Origen.BOTON -> Color(0xFF1E88E5)
        Origen.RECORDATORIO -> Color(0xFFF9A825)
        Origen.MODO_AVION -> Color(0xFF7E57C2)
    }
    Text(
        origen.texto,
        color = Color.White,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.clip(RoundedCornerShape(50)).background(color).padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

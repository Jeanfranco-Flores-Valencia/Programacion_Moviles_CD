package com.floresvalencia.clinicasalud.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.clinicasalud.data.Cita
import com.floresvalencia.clinicasalud.data.EstadoCita
import com.floresvalencia.clinicasalud.ui.components.EstadoBadge

@Composable
fun MisCitasScreen(citas: List<Cita>, onAgendarNueva: () -> Unit) {
    // Más recientes primero y, dentro de eso, las Confirmadas antes que las Completadas
    val citasOrdenadas = citas.reversed().sortedBy { it.estado.ordinal }
    val hayProximas = citas.any { it.estado == EstadoCita.CONFIRMADA }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!hayProximas) {
            item {
                Column {
                    Text(
                        text = "No tienes citas próximas.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = onAgendarNueva) { Text("Agendar una cita") }
                }
            }
        }

        items(citasOrdenadas) { cita -> CitaCard(cita) }
    }
}

@Composable
private fun CitaCard(cita: Cita) {
    val esConfirmada = cita.estado == EstadoCita.CONFIRMADA

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Barra morada a la izquierda solo en las citas Confirmadas
            if (esConfirmada) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = cita.medico.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${cita.fecha}, ${cita.hora}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                EstadoBadge(estado = cita.estado)
            }
        }
    }
}
package com.floresvalencia.clinicasalud.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.floresvalencia.clinicasalud.data.Cita
import com.floresvalencia.clinicasalud.data.EstadoCita
import com.floresvalencia.clinicasalud.ui.components.EstadoBadge
import com.floresvalencia.clinicasalud.ui.components.RojoCancelada

@Composable
fun MisCitasScreen(
    citas: List<Cita>,
    onAgendarNueva: () -> Unit,
    onCancelarCita: (Cita) -> Unit
) {
    // Cita que el usuario quiere cancelar (null = el diálogo está cerrado)
    var citaACancelar by remember { mutableStateOf<Cita?>(null) }

    // Más recientes primero; orden: Confirmadas, Completadas, Canceladas
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

        items(citasOrdenadas) { cita ->
            CitaCard(
                cita = cita,
                onCancelarClick = { citaACancelar = cita }   // solo abre el diálogo
            )
        }
    }

    // AlertDialog de confirmación: se muestra solo si hay una cita pendiente de cancelar
    val cita = citaACancelar
    if (cita != null) {
        AlertDialog(
            onDismissRequest = { citaACancelar = null },
            icon = { Icon(Icons.Filled.EventBusy, contentDescription = null, tint = RojoCancelada) },
            title = { Text("¿Cancelar cita?") },
            text = {
                Text("Se cancelará tu cita con ${cita.medico.nombre} el ${cita.fecha}, ${cita.hora}.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onCancelarCita(cita)
                        citaACancelar = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = RojoCancelada)
                ) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { citaACancelar = null }) {
                    Text("Volver")
                }
            }
        )
    }
}

@Composable
private fun CitaCard(cita: Cita, onCancelarClick: () -> Unit) {
    val esConfirmada = cita.estado == EstadoCita.CONFIRMADA
    val esCancelada = cita.estado == EstadoCita.CANCELADA

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (esCancelada) 0.7f else 1f),
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = cita.medico.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${cita.fecha}, ${cita.hora}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    // Fecha tachada si la cita fue cancelada
                    textDecoration = if (esCancelada) TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EstadoBadge(estado = cita.estado)
                    Spacer(modifier = Modifier.weight(1f))
                    // Solo las citas Confirmadas se pueden cancelar
                    if (esConfirmada) {
                        TextButton(
                            onClick = onCancelarClick,
                            colors = ButtonDefaults.textButtonColors(contentColor = RojoCancelada)
                        ) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        }
    }
}
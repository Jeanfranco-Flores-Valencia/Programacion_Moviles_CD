package com.floresvalencia.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.tecsupfit.data.EstadoReserva
import com.floresvalencia.tecsupfit.data.Reserva
import com.floresvalencia.tecsupfit.ui.components.EstadoBadge

@Composable
fun ReservasScreen(
    reservas: List<Reserva>,
    onRegistrarAsistencia: (Reserva) -> Unit
) {
    // Reserva cuya asistencia se va a confirmar (null = diálogo cerrado)
    var reservaPorConfirmar by remember { mutableStateOf<Reserva?>(null) }

    // Más recientes primero y, dentro de eso, Confirmadas antes que Completadas
    val reservasOrdenadas = reservas.reversed().sortedBy { it.estado.ordinal }
    val hayProximas = reservas.any { it.estado == EstadoReserva.CONFIRMADA }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!hayProximas) {
            item {
                Text(
                    text = "Aún no tienes reservas próximas. Reserva una clase desde Inicio.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(reservasOrdenadas) { reserva ->
            ReservaCard(
                reserva = reserva,
                onRegistrarClick = { reservaPorConfirmar = reserva }   // solo abre el diálogo
            )
        }
    }

    // AlertDialog de confirmación de asistencia
    val reserva = reservaPorConfirmar
    if (reserva != null) {
        AlertDialog(
            onDismissRequest = { reservaPorConfirmar = null },
            icon = {
                Icon(Icons.Filled.TaskAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            },
            title = { Text("¿Registrar asistencia?") },
            text = {
                Text(
                    "Confirma que asististe a ${reserva.clase.nombre} " +
                            "(${reserva.horario} · ${reserva.clase.sala}). " +
                            "Se sumará a tus clases tomadas y a tu racha."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onRegistrarAsistencia(reserva)
                    reservaPorConfirmar = null
                }) {
                    Text("Sí, asistí")
                }
            },
            dismissButton = {
                TextButton(onClick = { reservaPorConfirmar = null }) {
                    Text("Aún no")
                }
            }
        )
    }
}

@Composable
private fun ReservaCard(reserva: Reserva, onRegistrarClick: () -> Unit) {
    val esConfirmada = reserva.estado == EstadoReserva.CONFIRMADA

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Barra verde a la izquierda solo en las reservas Confirmadas
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
                    text = reserva.clase.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = reserva.horario,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EstadoBadge(estado = reserva.estado)
                    Spacer(modifier = Modifier.weight(1f))
                    // Solo las reservas Confirmadas pueden registrar asistencia
                    if (esConfirmada) {
                        TextButton(onClick = onRegistrarClick) {
                            Icon(Icons.Filled.HowToReg, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Registrar asistencia")
                        }
                    }
                }
            }
        }
    }
}
package com.floresvalencia.tecsupfit.ui.screens

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
import com.floresvalencia.tecsupfit.data.EstadoReserva
import com.floresvalencia.tecsupfit.data.Reserva
import com.floresvalencia.tecsupfit.ui.components.EstadoBadge

@Composable
fun ReservasScreen(reservas: List<Reserva>) {
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

        items(reservasOrdenadas) { reserva -> ReservaCard(reserva) }
    }
}

@Composable
private fun ReservaCard(reserva: Reserva) {
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
                EstadoBadge(estado = reserva.estado)
            }
        }
    }
}
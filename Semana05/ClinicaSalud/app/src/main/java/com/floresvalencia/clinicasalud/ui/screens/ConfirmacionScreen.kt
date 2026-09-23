package com.floresvalencia.clinicasalud.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.clinicasalud.data.Medico
import com.floresvalencia.clinicasalud.ui.components.VerdeConfirmada
import com.floresvalencia.clinicasalud.ui.components.VerdeConfirmadaFondo

@Composable
fun ConfirmacionScreen(
    medico: Medico,
    fecha: String,
    hora: String,
    onVerMisCitas: () -> Unit,
    onVolverInicio: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Círculo verde con check
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(VerdeConfirmadaFondo),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = VerdeConfirmada,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("¡Cita agendada!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // Resumen con los datos que llegaron por parámetro
        Text(medico.nombre, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("$fecha, $hora", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(28.dp))

        FilledTonalButton(
            onClick = onVerMisCitas,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Ver mis citas")
        }
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(onClick = onVolverInicio) {
            Text("Volver al inicio")
        }
    }
}

package com.floresvalencia.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.clinicasalud.data.Medico
import com.floresvalencia.clinicasalud.ui.components.AvatarMedico
import com.floresvalencia.clinicasalud.ui.components.Calificacion

@Composable
fun PerfilMedicoScreen(medico: Medico, onAgendarClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Cabecera centrada
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AvatarMedico(tamano = 96.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = medico.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${medico.titulo} · ${medico.aniosExperiencia} años exp.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Calificacion(valor = medico.calificacion, resenas = medico.resenas)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = medico.descripcion, style = MaterialTheme.typography.bodyMedium)

        // Empuja el botón hasta abajo de la pantalla
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onAgendarClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Agendar cita", fontWeight = FontWeight.SemiBold)
        }
    }
}
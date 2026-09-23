package com.floresvalencia.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.clinicasalud.data.Cita
import com.floresvalencia.clinicasalud.data.DatosClinica
import com.floresvalencia.clinicasalud.data.EstadoCita
import com.floresvalencia.clinicasalud.ui.components.AvatarIniciales

@Composable
fun PerfilPacienteScreen(citas: List<Cita>) {
    val proximas = citas.count { it.estado == EstadoCita.CONFIRMADA }
    val completadas = citas.count { it.estado == EstadoCita.COMPLETADA }

    val datos = listOf(
        "Edad" to "21 años",
        "Tipo de sangre" to "O+",
        "Seguro" to "EsSalud",
        "Correo" to "juan.perez@correo.com"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AvatarIniciales(nombre = DatosClinica.NOMBRE_PACIENTE, tamano = 88.dp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(DatosClinica.NOMBRE_PACIENTE, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Paciente", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Estadistica("$proximas", "Citas próximas", Modifier.weight(1f))
            Estadistica("$completadas", "Completadas", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                datos.forEach { (etiqueta, valor) ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(etiqueta, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(valor, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun Estadistica(valor: String, etiqueta: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(valor, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(etiqueta, style = MaterialTheme.typography.bodySmall)
        }
    }
}
package com.floresvalencia.clinicasalud.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.clinicasalud.data.DatosClinica
import com.floresvalencia.clinicasalud.data.Medico
import com.floresvalencia.clinicasalud.ui.components.AvatarMedico
import com.floresvalencia.clinicasalud.ui.components.Calificacion

@Composable
fun InicioScreen(onMedicoClick: (Medico) -> Unit) {
    // Estado local: especialidad elegida en la fila de chips
    var especialidadSeleccionada by remember { mutableStateOf("Todos") }

    val medicosFiltrados =
        if (especialidadSeleccionada == "Todos") DatosClinica.medicos
        else DatosClinica.medicos.filter { it.especialidad == especialidadSeleccionada }

    Column(modifier = Modifier.fillMaxSize()) {

        // LazyRow: chips de especialidad (seleccionado = morado relleno)
        LazyRow(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(DatosClinica.especialidades) { especialidad ->
                FilterChip(
                    selected = especialidad == especialidadSeleccionada,
                    onClick = { especialidadSeleccionada = especialidad },
                    label = { Text(especialidad) },
                    shape = RoundedCornerShape(50),
                    border = null,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        Text(
            text = "Médicos disponibles",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 10.dp)
        )

        // LazyColumn: lista de médicos
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(medicosFiltrados, key = { it.id }) { medico ->
                MedicoCard(medico = medico, onClick = { onMedicoClick(medico) })
            }
        }
    }
}

@Composable
private fun MedicoCard(medico: Medico, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarMedico()
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medico.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = medico.titulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Calificacion(valor = medico.calificacion)
        }
    }
}
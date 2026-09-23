package com.floresvalencia.clinicasalud.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.clinicasalud.data.DatosClinica
import com.floresvalencia.clinicasalud.data.Medico
import com.floresvalencia.clinicasalud.ui.components.AvatarIniciales
import com.floresvalencia.clinicasalud.ui.components.Calificacion
import com.floresvalencia.clinicasalud.ui.components.TituloSeccion

@Composable
fun InicioScreen(onMedicoClick: (Medico) -> Unit) {
    // Estado local: especialidad elegida en la fila de chips
    var especialidadSeleccionada by remember { mutableStateOf("Todos") }

    val medicosFiltrados =
        if (especialidadSeleccionada == "Todos") DatosClinica.medicos
        else DatosClinica.medicos.filter { it.especialidad == especialidadSeleccionada }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "¿Qué especialista necesitas hoy?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
        )

        TituloSeccion("Especialidades")

        // LazyRow: chips de especialidad
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(DatosClinica.especialidades) { especialidad ->
                FilterChip(
                    selected = especialidad == especialidadSeleccionada,
                    onClick = { especialidadSeleccionada = especialidad },
                    label = { Text(especialidad) }
                )
            }
        }

        TituloSeccion("Médicos disponibles (${medicosFiltrados.size})")

        // LazyColumn: lista de médicos
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarIniciales(nombre = medico.nombre)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medico.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = medico.especialidad,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Calificacion(valor = medico.calificacion)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Ver perfil")
        }
    }
}
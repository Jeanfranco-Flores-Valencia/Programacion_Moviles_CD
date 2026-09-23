package com.floresvalencia.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.clinicasalud.data.DatosClinica
import com.floresvalencia.clinicasalud.data.FechaCita
import com.floresvalencia.clinicasalud.data.Medico
import com.floresvalencia.clinicasalud.ui.components.OpcionUnicaChip

@Composable
fun AgendarCitaScreen(
    medico: Medico,
    onConfirmar: (fecha: String, hora: String) -> Unit
) {
    // Selección única: UNA variable por grupo (null = todavía no elige)
    var fechaSeleccionada by remember { mutableStateOf<FechaCita?>(null) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    val fecha = fechaSeleccionada
    val hora = horaSeleccionada

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        // Médico elegido (llegó por parámetro de navegación)
        Text(
            text = "Con ${medico.nombre} · ${medico.titulo}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Etiqueta("Selecciona fecha")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(DatosClinica.fechasDisponibles) { opcion ->
                OpcionUnicaChip(
                    textoSuperior = opcion.diaCorto,
                    textoPrincipal = "${opcion.numero}",
                    seleccionado = opcion == fechaSeleccionada,
                    onClick = { fechaSeleccionada = opcion }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Etiqueta("Selecciona hora")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(DatosClinica.horasDisponibles) { opcion ->
                OpcionUnicaChip(
                    textoPrincipal = opcion,
                    seleccionado = opcion == horaSeleccionada,
                    onClick = { horaSeleccionada = opcion }
                )
            }
        }

        // Empuja el resumen y el botón hacia abajo
        Spacer(modifier = Modifier.weight(1f))

        if (fecha != null && hora != null) {
            Text(
                text = "Tu cita: ${fecha.texto}, $hora",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        // Solo se habilita cuando hay fecha Y hora elegidas
        Button(
            onClick = { if (fecha != null && hora != null) onConfirmar(fecha.texto, hora) },
            enabled = fecha != null && hora != null,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp)
        ) {
            Text("Confirmar cita", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun Etiqueta(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
}
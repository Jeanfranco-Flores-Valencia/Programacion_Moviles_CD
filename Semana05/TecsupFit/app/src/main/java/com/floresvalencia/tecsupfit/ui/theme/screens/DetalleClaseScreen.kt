package com.floresvalencia.tecsupfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.tecsupfit.data.ClaseGym
import com.floresvalencia.tecsupfit.data.Turno
import com.floresvalencia.tecsupfit.ui.components.IconoClase
import com.floresvalencia.tecsupfit.ui.components.OpcionUnicaChip

@Composable
fun DetalleClaseScreen(
    clase: ClaseGym,
    onReservar: (horario: String) -> Unit
) {
    // Selección única: UNA variable guarda el turno elegido (null = aún no elige)
    var turnoSeleccionado by remember { mutableStateOf<Turno?>(null) }
    val turno = turnoSeleccionado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
    ) {
        IconoClase(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(120.dp),
            tamanoIcono = 64.dp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(clase.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                text = "${clase.horarioTexto} · ${clase.duracionMin} min",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(clase.descripcion, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Elige horario",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
        )

        // LazyRow de turnos: selección única; los turnos sin cupo se deshabilitan
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(clase.turnos) { opcion ->
                val hayCupos = opcion.cuposLibres > 0
                OpcionUnicaChip(
                    texto = if (hayCupos) opcion.etiqueta else "${opcion.etiqueta} · Lleno",
                    seleccionado = opcion == turnoSeleccionado,
                    onClick = { turnoSeleccionado = opcion },
                    habilitado = hayCupos
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cupos del turno elegido
        Text(
            text = if (turno != null) "${turno.cuposLibres} de ${clase.cuposTotales} cupos disponibles"
            else "Elige un horario para ver los cupos",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Solo se habilita cuando hay un horario elegido
        Button(
            onClick = { if (turno != null) onReservar(turno.etiqueta) },
            enabled = turno != null,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Reservar cupo", fontWeight = FontWeight.SemiBold)
        }
    }
}
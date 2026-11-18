package com.floresv.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresv.centrotareas.ui.components.AnilloProgreso
import com.floresv.centrotareas.ui.components.BotonPrincipal
import com.floresv.centrotareas.ui.components.BotonSecundario
import com.floresv.centrotareas.ui.components.EstadoTarea
import com.floresv.centrotareas.ui.components.FilaAcciones
import com.floresv.centrotareas.ui.components.TarjetaTarea

/** Tarjeta 1: tarea con progreso. Recibe el estado y avisa los clics; no conoce el ViewModel. */
@Composable
fun TarjetaProgreso(
    progreso: Float,
    corriendo: Boolean,
    mensaje: String,
    onIniciar: () -> Unit,
    onCancelar: () -> Unit
) {
    val estado = when {
        corriendo -> EstadoTarea.EN_CURSO
        mensaje == "Terminó" -> EstadoTarea.COMPLETADA
        mensaje == "Cancelado" -> EstadoTarea.CANCELADA
        else -> EstadoTarea.LISTA
    }
    TarjetaTarea("Tarea con progreso", "Simula 4 s de trabajo en segundo plano", Icons.Outlined.Timer, estado) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AnilloProgreso(progreso)
            Column {
                Text(mensaje, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    if (corriendo) "Toca «Tócame» arriba: sigue respondiendo" else "Iniciar lanza una corrutina con viewModelScope",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        // Iniciar se apaga mientras corre; Cancelar solo se enciende mientras corre
        FilaAcciones {
            BotonPrincipal("Iniciar", Icons.Outlined.PlayArrow, habilitado = !corriendo, onClick = onIniciar)
            BotonSecundario("Cancelar", Icons.Outlined.Close, habilitado = corriendo, onClick = onCancelar)
        }
    }
}

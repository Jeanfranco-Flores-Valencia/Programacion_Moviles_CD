package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonSecundario
import com.saludplus.citas.ui.components.EncabezadoMedico
import com.saludplus.citas.ui.components.FechaUtils
import com.saludplus.citas.ui.components.FilaDetalle
import com.saludplus.citas.ui.components.MensajeVacio
import com.saludplus.citas.ui.components.TarjetaBase
import com.saludplus.citas.ui.components.rangoHora
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.BordeSuave
import com.saludplus.citas.ui.theme.Rojo

@Composable
fun DetalleCitaScreen(navController: NavHostController, citaId: Int) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Detalle de cita", onAtras = { navController.popBackStack() }) }
    ) { padding ->
        if (cita == null || medico == null) {
            MensajeVacio(
                icono = Icons.Filled.EventBusy,
                titulo = "Cita no encontrada",
                mensaje = "Esta cita ya no existe o fue cancelada.",
                modifier = Modifier.padding(padding)
            )
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            EncabezadoMedico(medico, mostrarCmp = true)
            Spacer(Modifier.height(8.dp))
            TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                    FilaDetalle(Icons.Filled.MedicalServices, "Especialidad", especialidad?.nombre ?: "")
                    HorizontalDivider(color = BordeSuave)
                    FilaDetalle(Icons.Filled.CalendarMonth, "Fecha", FechaUtils.fechaLarga(cita.fecha))
                    HorizontalDivider(color = BordeSuave)
                    FilaDetalle(Icons.Filled.Schedule, "Hora", rangoHora(cita.hora))
                    HorizontalDivider(color = BordeSuave)
                    FilaDetalle(Icons.Filled.LocationOn, "Dirección", medico.direccion)
                    HorizontalDivider(color = BordeSuave)
                    FilaDetalle(
                        Icons.Filled.Description,
                        "Motivo de consulta",
                        cita.motivo.ifBlank { "No especificado" }
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            BotonSecundario(texto = "Cancelar cita", onClick = { mostrarDialogo = true }, color = Rojo)
            Spacer(Modifier.height(24.dp))
        }

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                icon = { Icon(Icons.Filled.Warning, contentDescription = null, tint = Rojo) },
                title = { Text("¿Cancelar cita?") },
                text = { Text("Se liberará el horario de ${medico.nombre}. Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(onClick = {
                        mostrarDialogo = false
                        Repositorio.cancelarCita(cita.id)
                        navController.popBackStack()
                    }) {
                        Text("Sí, cancelar", color = Rojo, fontWeight = FontWeight.SemiBold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo = false }) { Text("No") }
                }
            )
        }
    }
}

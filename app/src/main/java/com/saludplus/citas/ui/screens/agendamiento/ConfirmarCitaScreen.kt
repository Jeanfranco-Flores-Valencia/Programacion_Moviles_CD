package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.EncabezadoMedico
import com.saludplus.citas.ui.components.FilaDetalle
import com.saludplus.citas.ui.components.rangoHora
import com.saludplus.citas.ui.components.textoFecha
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.BordeSuave
import com.saludplus.citas.ui.theme.Rojo
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun ConfirmarCitaScreen(navController: NavHostController, medicoId: Int, fecha: String, hora: String) {
    val medico = Repositorio.obtenerMedico(medicoId)
    var motivo by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Confirmar cita", onAtras = { navController.popBackStack() }) },
        bottomBar = {
            BotonPrincipal(
                texto = "Agendar cita",
                onClick = {
                    val cita = Repositorio.agendarCita(medicoId, fecha, hora, motivo)
                    if (cita != null) {
                        // popUpTo: se borra del historial todo el flujo de agendamiento
                        navController.navigate(Rutas.citaExitosa(cita.id)) {
                            popUpTo(Rutas.HOME)
                        }
                    } else {
                        error = "Ese horario ya no está disponible. Vuelve y elige otro."
                    }
                },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            if (medico != null) EncabezadoMedico(medico, mostrarCmp = true)
            Spacer(Modifier.height(8.dp))

            FilaDetalle(Icons.Filled.CalendarMonth, "Fecha", textoFecha(fecha))
            HorizontalDivider(color = BordeSuave)
            FilaDetalle(Icons.Filled.Schedule, "Hora", rangoHora(hora))
            HorizontalDivider(color = BordeSuave)
            FilaDetalle(Icons.Filled.MedicalServices, "Tipo de atención", medico?.tipoAtencion ?: "Consulta presencial")
            HorizontalDivider(color = BordeSuave)
            FilaDetalle(Icons.Filled.LocationOn, "Dirección", medico?.direccion ?: "")

            Spacer(Modifier.height(16.dp))
            Row {
                Text("Motivo de consulta ", style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
                Text("(opcional)", style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it.take(200) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                placeholder = { Text("Ej. Consulta de rutina", color = TextoSecundario) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulPrimario,
                    unfocusedBorderColor = BordeSuave
                )
            )

            if (error != null) {
                Spacer(Modifier.height(8.dp))
                Text(error!!, color = Rojo, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Business
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
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.EncabezadoMedico
import com.saludplus.citas.ui.components.FechaUtils
import com.saludplus.citas.ui.components.FilaDetalle
import com.saludplus.citas.ui.components.HeroHeader
import com.saludplus.citas.ui.components.StepperAgendamiento
import com.saludplus.citas.ui.components.TarjetaBase
import com.saludplus.citas.ui.components.rangoHora
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.BordeSuave
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.Rojo
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun ConfirmarCitaScreen(navController: NavHostController, medicoId: Int, sedeId: Int, fecha: String, hora: String) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val sede = Repositorio.obtenerSede(sedeId)
    var motivo by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = FondoApp,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Blanco)
                    .padding(20.dp)
            ) {
                if (error != null) {
                    Text(error!!, color = Rojo, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(8.dp))
                }
                BotonPrincipal(
                    texto = "Confirmar y agendar cita",
                    onClick = {
                        val cita = Repositorio.agendarCita(medicoId, sedeId, fecha, hora, motivo)
                        if (cita != null) {
                            navController.navigate(Rutas.citaExitosa(cita.id)) {
                                popUpTo(Rutas.HOME)
                            }
                        } else {
                            error = "Ese horario ya no está disponible o ya tienes una cita a esa hora."
                        }
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            HeroHeader(
                titulo = "Confirmar Cita",
                subtitulo = "Revisa los detalles antes de agendar",
                onAtras = { navController.popBackStack() }
            )

            StepperAgendamiento(pasoActual = 5)

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(8.dp))
                if (medico != null) EncabezadoMedico(medico, mostrarCmp = true)
                Spacer(Modifier.height(12.dp))

                TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        FilaDetalle(Icons.Filled.Business, "Sede", sede?.nombre ?: "Sede Principal")
                        HorizontalDivider(color = BordeSuave)
                        FilaDetalle(Icons.Filled.CalendarMonth, "Fecha", FechaUtils.fechaLarga(fecha))
                        HorizontalDivider(color = BordeSuave)
                        FilaDetalle(Icons.Filled.Schedule, "Hora", rangoHora(hora))
                        HorizontalDivider(color = BordeSuave)
                        FilaDetalle(Icons.Filled.MedicalServices, "Tipo de atención", medico?.tipoAtencion ?: "Consulta presencial")
                        HorizontalDivider(color = BordeSuave)
                        FilaDetalle(Icons.Filled.LocationOn, "Dirección", sede?.direccion ?: medico?.direccion ?: "")
                    }
                }

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
                        unfocusedBorderColor = BordeSuave,
                        focusedContainerColor = Blanco,
                        unfocusedContainerColor = Blanco
                    )
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

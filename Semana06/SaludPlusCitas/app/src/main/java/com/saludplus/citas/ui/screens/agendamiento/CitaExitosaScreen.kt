package com.saludplus.citas.ui.screens.agendamiento

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.BotonSecundario
import com.saludplus.citas.ui.components.EncabezadoMedico
import com.saludplus.citas.ui.components.FechaUtils
import com.saludplus.citas.ui.components.FilaDetalle
import com.saludplus.citas.ui.components.IconoEnCaja
import com.saludplus.citas.ui.components.TarjetaBase
import com.saludplus.citas.ui.components.rangoHora
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.BordeSuave
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.VerdeClaro
import com.saludplus.citas.ui.theme.VerdeExito

@Composable
fun CitaExitosaScreen(navController: NavHostController, citaId: Int) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    // Como se llegó con popUpTo, "Atrás" vuelve directo a Inicio
    val volverAlInicio = { navController.popBackStack(Rutas.HOME, inclusive = false) }

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Cita agendada", onAtras = { volverAlInicio() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            IconoEnCaja(
                icono = Icons.Filled.CheckCircle,
                colorFondo = VerdeClaro,
                colorIcono = VerdeExito,
                tamano = 96.dp,
                circular = true
            )
            Spacer(Modifier.height(16.dp))
            Text("¡Cita agendada!", style = MaterialTheme.typography.headlineMedium, color = TextoPrincipal)
            Spacer(Modifier.height(4.dp))
            Text(
                "Tu cita fue registrada correctamente. Te esperamos 15 minutos antes.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))

            if (cita != null && medico != null) {
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
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            BotonPrincipal(
                texto = "Ver mis citas",
                onClick = {
                    navController.navigate(Rutas.MIS_CITAS) {
                        popUpTo(Rutas.HOME)
                    }
                }
            )
            Spacer(Modifier.height(10.dp))
            BotonSecundario(texto = "Volver al inicio", onClick = { volverAlInicio() })
            Spacer(Modifier.height(24.dp))
        }
    }
}

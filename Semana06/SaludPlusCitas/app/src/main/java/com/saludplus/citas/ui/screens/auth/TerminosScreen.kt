package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario

private val secciones = listOf(
    "1. Uso de la aplicación" to "La App Paciente de la Clínica SaludPlus permite registrarte, consultar especialidades y médicos, y agendar citas médicas. El uso de la app es personal e intransferible.",
    "2. Datos personales" to "Tus datos (nombre, teléfono y correo) se usan únicamente para identificarte y gestionar tus citas. En esta versión los datos se guardan solo en la memoria del dispositivo y se eliminan al cerrar la app.",
    "3. Citas médicas" to "Cada cita queda reservada para el médico, la fecha y la hora elegidas. Un horario reservado deja de estar disponible para otros pacientes.",
    "4. Cancelaciones" to "Puedes cancelar una cita desde el detalle de la cita. Te recomendamos hacerlo con al menos 24 horas de anticipación.",
    "5. Puntualidad" to "Preséntate 15 minutos antes de tu cita con tu documento de identidad. Si llegas tarde, la atención podría reprogramarse.",
    "6. Responsabilidad" to "La app no reemplaza la atención de emergencia. Ante una urgencia acude al servicio de emergencias más cercano.",
    "7. Cambios" to "La Clínica SaludPlus puede actualizar estos términos. Los cambios se informarán dentro de la aplicación."
)

@Composable
fun TerminosScreen(navController: NavHostController) {
    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Términos y condiciones", onAtras = { navController.popBackStack() }) },
        bottomBar = {
            BotonPrincipal(
                texto = "Entendido",
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            secciones.forEach { (titulo, texto) ->
                Text(titulo, style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
                Spacer(Modifier.height(4.dp))
                Text(texto, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

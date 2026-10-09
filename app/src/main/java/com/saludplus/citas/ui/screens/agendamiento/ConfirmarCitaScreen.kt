package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun ConfirmarCitaScreen(navController: NavHostController, medicoId: Int, fecha: String, hora: String) {
    // TODO: Resumen: médico, fecha, hora, tipo de atención y dirección.
    // TODO: Motivo de consulta (opcional).
    // TODO: Repositorio.agendarCita(...) y navegar a Cita agendada con popUpTo.

    PantallaEnConstruccion(
        titulo = "7. Confirmar cita ($fecha $hora)",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Agendar cita" to { navController.navigate(Rutas.citaExitosa(1)) { popUpTo(Rutas.HOME) } }
        )
    )
}

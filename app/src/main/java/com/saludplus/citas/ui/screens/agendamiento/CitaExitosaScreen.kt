package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun CitaExitosaScreen(navController: NavHostController, citaId: Int) {
    // TODO: Mensaje de éxito y resumen de la cita (Repositorio.obtenerCita).
    // TODO: Botones: Ver mis citas y Volver al inicio.

    PantallaEnConstruccion(
        titulo = "9. Cita agendada (citaId = $citaId)",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Ver mis citas" to { navController.navigate(Rutas.MIS_CITAS) },
            "Volver al inicio" to { navController.popBackStack(Rutas.HOME, false) }
        )
    )
}

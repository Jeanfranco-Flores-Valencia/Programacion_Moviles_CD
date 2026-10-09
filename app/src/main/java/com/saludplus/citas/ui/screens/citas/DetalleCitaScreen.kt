package com.saludplus.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun DetalleCitaScreen(navController: NavHostController, citaId: Int) {
    // TODO: (Reto extra) Detalle de la cita.
    // TODO: Cancelar con AlertDialog y Repositorio.cancelarCita(citaId).

    PantallaEnConstruccion(
        titulo = "12. Detalle de cita (citaId = $citaId)",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Volver" to { navController.popBackStack() }
        )
    )
}

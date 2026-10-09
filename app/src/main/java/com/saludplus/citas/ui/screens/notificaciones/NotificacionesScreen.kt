package com.saludplus.citas.ui.screens.notificaciones

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun NotificacionesScreen(navController: NavHostController) {
    // TODO: (Reto extra) Usar map sobre las citas del usuario para crear recordatorios.

    PantallaEnConstruccion(
        titulo = "14. Notificaciones",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Volver" to { navController.popBackStack() }
        )
    )
}

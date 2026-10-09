package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun FechaHoraScreen(navController: NavHostController, medicoId: Int) {
    // TODO: Datos del médico.
    // TODO: Selección de día.
    // TODO: LazyVerticalGrid con Repositorio.horariosDisponibles(medicoId, fecha).
    // TODO: Continuar solo habilitado con día y hora elegidos.

    PantallaEnConstruccion(
        titulo = "6. Fecha y hora (medicoId = $medicoId)",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Continuar" to { navController.navigate(Rutas.confirmarCita(medicoId, "2026-10-13", "09:30")) }
        )
    )
}

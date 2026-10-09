package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun MedicosScreen(navController: NavHostController, especialidadId: Int) {
    // TODO: Título 'Médicos de <especialidad>'.
    // TODO: LazyColumn con Repositorio.medicosPorEspecialidad(especialidadId) (filter + sortedByDescending).
    // TODO: Al tocar un médico navegar a Fecha y hora con su id.

    PantallaEnConstruccion(
        titulo = "5. Médicos (especialidadId = $especialidadId)",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Elegir médico" to { navController.navigate(Rutas.fechaHora(7)) }
        )
    )
}

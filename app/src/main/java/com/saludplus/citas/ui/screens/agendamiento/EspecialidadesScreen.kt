package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun EspecialidadesScreen(navController: NavHostController) {
    // TODO: Buscador + LazyColumn con Repositorio.buscarEspecialidades(texto).
    // TODO: Al tocar una especialidad navegar a Médicos con su id.

    PantallaEnConstruccion(
        titulo = "4. Especialidades",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Ver médicos de Ginecología" to { navController.navigate(Rutas.medicos(3)) }
        )
    )
}

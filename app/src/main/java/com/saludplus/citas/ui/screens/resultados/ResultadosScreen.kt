package com.saludplus.citas.ui.screens.resultados

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun ResultadosScreen(navController: NavHostController) {
    // TODO: (Reto extra) Modelo propio y lista fija de resultados.

    PantallaEnConstruccion(
        titulo = "13. Resultados",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Volver" to { navController.popBackStack() }
        )
    )
}

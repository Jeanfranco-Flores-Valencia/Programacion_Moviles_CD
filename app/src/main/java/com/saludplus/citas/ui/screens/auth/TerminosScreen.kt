package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun TerminosScreen(navController: NavHostController) {
    // TODO: (Reto extra) Texto de términos con scroll o AlertDialog.

    PantallaEnConstruccion(
        titulo = "15. Términos y condiciones",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Volver" to { navController.popBackStack() }
        )
    )
}

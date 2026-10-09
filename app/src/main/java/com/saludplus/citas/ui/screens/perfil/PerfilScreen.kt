package com.saludplus.citas.ui.screens.perfil

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun PerfilScreen(navController: NavHostController) {
    // TODO: Datos del usuario en sesión (Repositorio.usuarioActual).
    // TODO: Cerrar sesión y volver al Splash limpiando el historial.

    PantallaEnConstruccion(
        titulo = "11. Perfil / Mis datos",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Cerrar sesión" to { navController.navigate(Rutas.SPLASH) }
        )
    )
}

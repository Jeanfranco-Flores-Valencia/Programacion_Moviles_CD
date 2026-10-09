package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun SplashScreen(navController: NavHostController) {
    // TODO: Logo, nombre de la clínica, eslogan e ilustración (Image + Column).
    // TODO: Botón 'Comenzar' -> Registro y enlace 'Ya tengo una cuenta' -> Login.

    PantallaEnConstruccion(
        titulo = "1. Splash",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Comenzar" to { navController.navigate(Rutas.REGISTRO) },
            "Ya tengo una cuenta" to { navController.navigate(Rutas.LOGIN) }
        )
    )
}

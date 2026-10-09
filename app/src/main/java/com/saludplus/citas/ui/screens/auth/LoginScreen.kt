package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun LoginScreen(navController: NavHostController) {
    // TODO: Teléfono/correo y contraseña.
    // TODO: Repositorio.iniciarSesion(...) (find en la lista de usuarios).
    // TODO: Si es correcto ir a Inicio; si no, mostrar error.

    PantallaEnConstruccion(
        titulo = "8. Iniciar sesión",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Ingresar" to { navController.navigate(Rutas.HOME) },
            "Regístrate" to { navController.navigate(Rutas.REGISTRO) }
        )
    )
}

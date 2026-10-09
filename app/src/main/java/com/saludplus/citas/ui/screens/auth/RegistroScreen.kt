package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun RegistroScreen(navController: NavHostController) {
    // TODO: Estados para nombre, teléfono, correo (opcional) y contraseña con OutlinedTextField.
    // TODO: Validaciones: nombre, teléfono de 9 dígitos, correo válido si se escribe, contraseña mínima.
    // TODO: Repositorio.registrarUsuario(...) y navegar a Inicio.
    // TODO: Enlaces a Términos y a Iniciar sesión.

    PantallaEnConstruccion(
        titulo = "2. Registro",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Registrarme" to { navController.navigate(Rutas.HOME) },
            "Términos y condiciones" to { navController.navigate(Rutas.TERMINOS) },
            "Iniciar sesión" to { navController.navigate(Rutas.LOGIN) }
        )
    )
}

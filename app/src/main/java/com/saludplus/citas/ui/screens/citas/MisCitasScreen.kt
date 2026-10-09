package com.saludplus.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun MisCitasScreen(navController: NavHostController) {
    // TODO: LazyColumn con Repositorio.citasDelUsuario().
    // TODO: Mensaje cuando la lista está vacía.

    PantallaEnConstruccion(
        titulo = "10. Mis citas",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Ver detalle" to { navController.navigate(Rutas.detalleCita(1)) }
        )
    )
}

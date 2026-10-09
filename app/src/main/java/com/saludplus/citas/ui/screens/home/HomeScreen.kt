package com.saludplus.citas.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.screens.PantallaEnConstruccion

@Composable
fun HomeScreen(navController: NavHostController) {
    // TODO: Scaffold con saludo '¡Hola, <nombre>!' y campana de notificaciones.
    // TODO: Tarjetas: Agendar cita, Mis citas, Mis datos, Resultados.
    // TODO: LazyRow de especialidades destacadas.
    // TODO: NavigationBar (bottomBar) con Inicio, Citas, Resultados y Perfil.

    PantallaEnConstruccion(
        titulo = "3. Inicio",
        descripcion = "Pantalla en construcción",
        acciones = listOf(
            "Agendar cita" to { navController.navigate(Rutas.ESPECIALIDADES) },
            "Mis citas" to { navController.navigate(Rutas.MIS_CITAS) },
            "Perfil" to { navController.navigate(Rutas.PERFIL) },
            "Resultados" to { navController.navigate(Rutas.RESULTADOS) },
            "Notificaciones" to { navController.navigate(Rutas.NOTIFICACIONES) }
        )
    )
}

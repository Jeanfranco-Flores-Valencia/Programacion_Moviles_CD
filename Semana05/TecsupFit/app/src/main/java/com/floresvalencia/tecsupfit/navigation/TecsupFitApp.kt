package com.floresvalencia.tecsupfit.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.floresvalencia.tecsupfit.data.DatosGym
import com.floresvalencia.tecsupfit.data.EstadoReserva
import com.floresvalencia.tecsupfit.data.Reserva
import com.floresvalencia.tecsupfit.ui.screens.*

private data class Pestana(val ruta: String, val titulo: String, val icono: ImageVector)

// 4 pestañas del bottomBar
private val pestanas = listOf(
    Pestana(Rutas.INICIO, "Inicio", Icons.Filled.Home),
    Pestana(Rutas.RESERVAS, "Reservas", Icons.Filled.EventAvailable),
    Pestana(Rutas.RUTINAS, "Rutinas", Icons.Filled.FitnessCenter),
    Pestana(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecsupFitApp() {
    val navController = rememberNavController()

    // Estado compartido entre pantallas (sin ViewModel): lista de reservas
    val reservas = remember {
        mutableStateListOf<Reserva>().apply { addAll(DatosGym.reservasIniciales) }
    }

    // La ruta actual decide: título, colores, flecha, visibilidad del bottomBar y pestaña resaltada.
    // currentBackStackEntryAsState() es un State: al cambiar de pantalla, todo se recompone.
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val esInicio = rutaActual == Rutas.INICIO
    val mostrarAtras = rutaActual == Rutas.DETALLE
    val mostrarBottomBar = pestanas.any { it.ruta == rutaActual }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (esInicio) {
                        Column {
                            Text("TECSUP Fit", fontWeight = FontWeight.Bold)
                            Text(
                                text = "Hola, ${DatosGym.usuario.nombre.substringBefore(" ")}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    } else {
                        Text(
                            text = tituloDePantalla(rutaActual),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    if (mostrarAtras) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                colors = if (esInicio) {
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        },
        bottomBar = {
            // Visible solo en las 4 pestañas (oculto en Detalle y Confirmación, como en la figura)
            if (mostrarBottomBar) {
                Column {
                    HorizontalDivider()
                    NavigationBar {
                        pestanas.forEach { pestana ->
                            val seleccionada = rutaActual == pestana.ruta
                            NavigationBarItem(
                                selected = seleccionada,
                                onClick = {
                                    navController.navigate(pestana.ruta) {
                                        popUpTo(Rutas.INICIO)      // no acumular pestañas en la pila
                                        launchSingleTop = true     // no duplicar la misma pantalla
                                    }
                                },
                                icon = { Icon(pestana.icono, contentDescription = pestana.titulo) },
                                label = {
                                    Text(
                                        text = pestana.titulo,
                                        fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // El padding del Scaffold (topBar + bottomBar) se aplica al NavHost -> TODAS las pantallas
        NavHost(
            navController = navController,
            startDestination = Rutas.INICIO,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1) Inicio
            composable(Rutas.INICIO) {
                InicioScreen(
                    onClaseClick = { clase -> navController.navigate(Rutas.detalle(clase.id)) }
                )
            }

            // 2) Detalle de clase (recibe claseId; el usuario elige horario)
            composable(
                route = Rutas.DETALLE,
                arguments = listOf(navArgument("claseId") { type = NavType.IntType })
            ) { entry ->
                val claseId = entry.arguments?.getInt("claseId") ?: 1
                val clase = DatosGym.buscarClase(claseId)
                DetalleClaseScreen(
                    clase = clase,
                    onReservar = { horario ->
                        reservas.add(Reserva(clase, horario, EstadoReserva.CONFIRMADA))
                        navController.navigate(Rutas.confirmacion(clase.id, horario)) {
                            popUpTo(Rutas.INICIO)
                        }
                    }
                )
            }

            // 3) Confirmación (recibe claseId y horario)
            composable(
                route = Rutas.CONFIRMACION,
                arguments = listOf(
                    navArgument("claseId") { type = NavType.IntType },
                    navArgument("horario") { type = NavType.StringType }
                )
            ) { entry ->
                val claseId = entry.arguments?.getInt("claseId") ?: 1
                val horario = entry.arguments?.getString("horario") ?: ""
                ConfirmacionScreen(
                    clase = DatosGym.buscarClase(claseId),
                    horario = horario,
                    onVerReservas = {
                        navController.navigate(Rutas.RESERVAS) { popUpTo(Rutas.INICIO) }
                    },
                    onVolverInicio = { navController.popBackStack(Rutas.INICIO, inclusive = false) }
                )
            }

            // Pestañas del bottomBar
            composable(Rutas.RESERVAS) {
                ReservasScreen(reservas = reservas)
            }
            composable(Rutas.RUTINAS) {
                RutinasScreen()
            }
            composable(Rutas.PERFIL) {
                PerfilScreen(reservas = reservas)
            }
        }
    }
}

private fun tituloDePantalla(ruta: String?): String = when (ruta) {
    Rutas.INICIO -> "TECSUP Fit"
    Rutas.DETALLE -> "Detalle de clase"
    Rutas.CONFIRMACION -> "Confirmación"
    Rutas.RESERVAS -> "Mis reservas"
    Rutas.RUTINAS -> "Rutinas"
    Rutas.PERFIL -> "Mi perfil"
    else -> "TECSUP Fit"
}
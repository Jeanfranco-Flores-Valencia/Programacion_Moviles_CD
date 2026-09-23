package com.floresvalencia.tecsupfit.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.floresvalencia.tecsupfit.data.DatosGym
import com.floresvalencia.tecsupfit.ui.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecsupFitApp() {
    val navController = rememberNavController()

    // Ruta actual -> define el título, el color y la flecha de la topBar
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val esInicio = rutaActual == Rutas.INICIO
    val mostrarAtras = rutaActual == Rutas.DETALLE

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
                // Verde en Inicio, blanca en las demás pantallas
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
        }
    ) { innerPadding ->
        // El padding del Scaffold se aplica al NavHost -> llega a TODAS las pantallas
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

            // 2) Detalle de clase (recibe claseId)
            composable(
                route = Rutas.DETALLE,
                arguments = listOf(navArgument("claseId") { type = NavType.IntType })
            ) { entry ->
                val claseId = entry.arguments?.getInt("claseId") ?: 1
                val clase = DatosGym.buscarClase(claseId)
                DetalleClaseScreen(
                    clase = clase,
                    onReservar = { horario -> /* Se conecta en el commit 7 */ }
                )
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
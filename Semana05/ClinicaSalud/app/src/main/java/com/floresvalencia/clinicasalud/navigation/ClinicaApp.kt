package com.floresvalencia.clinicasalud.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.floresvalencia.clinicasalud.data.DatosClinica
import com.floresvalencia.clinicasalud.ui.screens.InicioScreen
import com.floresvalencia.clinicasalud.ui.screens.PerfilMedicoScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicaApp() {
    val navController = rememberNavController()

    // Ruta actual -> define el título y si se muestra la flecha "atrás"
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tituloDePantalla(rutaActual)) },
                navigationIcon = {
                    if (rutaActual == Rutas.PERFIL_MEDICO) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
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
                    onMedicoClick = { medico ->
                        navController.navigate(Rutas.perfilMedico(medico.id))
                    }
                )
            }

            // 2) Perfil del médico (recibe medicoId)
            composable(
                route = Rutas.PERFIL_MEDICO,
                arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
            ) { entry ->
                val medicoId = entry.arguments?.getInt("medicoId") ?: 1
                val medico = DatosClinica.buscarMedico(medicoId)
                PerfilMedicoScreen(
                    medico = medico,
                    onAgendarClick = { /* Se conecta en el commit 6 */ }
                )
            }
        }
    }
}

private fun tituloDePantalla(ruta: String?): String = when (ruta) {
    Rutas.INICIO -> "Clínica Salud+"
    Rutas.PERFIL_MEDICO -> "Perfil del médico"
    Rutas.AGENDAR -> "Agendar cita"
    Rutas.CONFIRMACION -> "Confirmación"
    Rutas.MIS_CITAS -> "Mis citas"
    Rutas.HISTORIAL -> "Historial médico"
    else -> "Clínica Salud+"
}
package com.floresvalencia.clinicasalud.navigation

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
import com.floresvalencia.clinicasalud.data.Cita
import com.floresvalencia.clinicasalud.data.DatosClinica
import com.floresvalencia.clinicasalud.data.EstadoCita
import com.floresvalencia.clinicasalud.ui.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicaApp() {
    val navController = rememberNavController()

    // Estado compartido entre pantallas (sin ViewModel): lista de citas
    val citas = remember {
        mutableStateListOf<Cita>().apply { addAll(DatosClinica.citasIniciales) }
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val esInicio = rutaActual == Rutas.INICIO
    val mostrarAtras = rutaActual == Rutas.PERFIL_MEDICO || rutaActual == Rutas.AGENDAR

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (esInicio) {
                        Column {
                            Text("Clínica Salud+", fontWeight = FontWeight.Bold)
                            Text(
                                text = "Hola, ${DatosClinica.NOMBRE_PACIENTE.substringBefore(" ")}",
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
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
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
                    onAgendarClick = { navController.navigate(Rutas.agendar(medico.id)) }
                )
            }

            // 3) Agendar cita (recibe medicoId; el usuario elige fecha y hora)
            composable(
                route = Rutas.AGENDAR,
                arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
            ) { entry ->
                val medicoId = entry.arguments?.getInt("medicoId") ?: 1
                val medico = DatosClinica.buscarMedico(medicoId)
                AgendarCitaScreen(
                    medico = medico,
                    onConfirmar = { fecha, hora ->
                        // Se guarda la cita en la lista compartida
                        citas.add(Cita(medico, fecha, hora, EstadoCita.CONFIRMADA))
                        navController.navigate(Rutas.confirmacion(medico.id, fecha, hora)) {
                            // Al retroceder desde Confirmación se vuelve a Inicio, no al formulario
                            popUpTo(Rutas.INICIO)
                        }
                    }
                )
            }

            // 4) Confirmación (recibe medicoId, fecha y hora)
            composable(
                route = Rutas.CONFIRMACION,
                arguments = listOf(
                    navArgument("medicoId") { type = NavType.IntType },
                    navArgument("fecha") { type = NavType.StringType },
                    navArgument("hora") { type = NavType.StringType }
                )
            ) { entry ->
                val medicoId = entry.arguments?.getInt("medicoId") ?: 1
                val fecha = entry.arguments?.getString("fecha") ?: ""
                val hora = entry.arguments?.getString("hora") ?: ""
                ConfirmacionScreen(
                    medico = DatosClinica.buscarMedico(medicoId),
                    fecha = fecha,
                    hora = hora,
                    onVerMisCitas = { /* Se conecta en el commit 9 */ },
                    onVolverInicio = { navController.popBackStack(Rutas.INICIO, inclusive = false) }
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
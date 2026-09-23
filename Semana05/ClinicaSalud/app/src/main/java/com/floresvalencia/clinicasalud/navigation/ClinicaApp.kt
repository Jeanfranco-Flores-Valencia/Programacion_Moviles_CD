package com.floresvalencia.clinicasalud.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.floresvalencia.clinicasalud.data.Cita
import com.floresvalencia.clinicasalud.data.DatosClinica
import com.floresvalencia.clinicasalud.data.EstadoCita
import com.floresvalencia.clinicasalud.ui.components.AvatarIniciales
import com.floresvalencia.clinicasalud.ui.screens.*
import kotlinx.coroutines.launch

private data class OpcionMenu(val ruta: String, val titulo: String, val icono: ImageVector)

// Destinos del menú lateral
private val opcionesMenu = listOf(
    OpcionMenu(Rutas.INICIO, "Inicio", Icons.Filled.Home),
    OpcionMenu(Rutas.MIS_CITAS, "Mis citas", Icons.Filled.CalendarMonth),
    OpcionMenu(Rutas.HISTORIAL, "Historial médico", Icons.Filled.History),
    OpcionMenu(Rutas.PERFIL_PACIENTE, "Perfil", Icons.Filled.Person)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicaApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Estado compartido entre pantallas (sin ViewModel): lista de citas
    val citas = remember {
        mutableStateListOf<Cita>().apply { addAll(DatosClinica.citasIniciales) }
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val esInicio = rutaActual == Rutas.INICIO
    val esSeccionDelMenu = opcionesMenu.any { it.ruta == rutaActual }
    val mostrarAtras = rutaActual == Rutas.PERFIL_MEDICO || rutaActual == Rutas.AGENDAR

    // Si el drawer está abierto, el botón atrás del celular lo cierra
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    // El drawer ENVUELVE al Scaffold: se dibuja encima de toda la pantalla, incluida la topBar
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Encabezado: avatar JP + nombre + rol
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarIniciales(nombre = DatosClinica.NOMBRE_PACIENTE, tamano = 52.dp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = DatosClinica.NOMBRE_PACIENTE,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Paciente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
                Spacer(modifier = Modifier.height(12.dp))

                opcionesMenu.forEach { opcion ->
                    NavigationDrawerItem(
                        icon = { Icon(opcion.icono, contentDescription = null) },
                        label = { Text(opcion.titulo) },
                        selected = rutaActual == opcion.ruta,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(opcion.ruta) {
                                popUpTo(Rutas.INICIO)      // no apilar pantallas del menú
                                launchSingleTop = true     // no duplicar la misma pantalla
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                        when {
                            // Secciones del menú: ícono ☰ que abre el drawer
                            esSeccionDelMenu -> IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                            }
                            // Flujo secuencial: flecha atrás
                            mostrarAtras -> IconButton(onClick = { navController.navigateUp() }) {
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
                            citas.add(Cita(medico, fecha, hora, EstadoCita.CONFIRMADA))
                            navController.navigate(Rutas.confirmacion(medico.id, fecha, hora)) {
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
                        onVerMisCitas = {
                            navController.navigate(Rutas.MIS_CITAS) { popUpTo(Rutas.INICIO) }
                        },
                        onVolverInicio = { navController.popBackStack(Rutas.INICIO, inclusive = false) }
                    )
                }

                // Destinos del menú lateral
                composable(Rutas.MIS_CITAS) {
                    MisCitasScreen(
                        citas = citas,
                        onAgendarNueva = { navController.popBackStack(Rutas.INICIO, inclusive = false) },
                        onCancelarCita = { cita ->
                            val indice = citas.indexOf(cita)
                            if (indice != -1) {
                                citas[indice] = cita.copy(estado = EstadoCita.CANCELADA)

                                // Snackbar con opción de deshacer
                                scope.launch {
                                    val resultado = snackbarHostState.showSnackbar(
                                        message = "Cita con ${cita.medico.nombre} cancelada",
                                        actionLabel = "Deshacer",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (resultado == SnackbarResult.ActionPerformed) {
                                        citas[indice] = cita   // vuelve a quedar Confirmada
                                    }
                                }
                            }
                        }
                    )
                }
                composable(Rutas.HISTORIAL) {
                    HistorialScreen()
                }
                composable(Rutas.PERFIL_PACIENTE) {
                    PerfilPacienteScreen(citas = citas)
                }
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
    Rutas.PERFIL_PACIENTE -> "Mi perfil"
    else -> "Clínica Salud+"
}
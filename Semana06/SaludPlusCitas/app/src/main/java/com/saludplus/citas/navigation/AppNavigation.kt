package com.saludplus.citas.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.ui.components.AppNavigationDrawer
import com.saludplus.citas.ui.screens.agendamiento.CitaExitosaScreen
import com.saludplus.citas.ui.screens.agendamiento.ConfirmarCitaScreen
import com.saludplus.citas.ui.screens.agendamiento.EspecialidadesScreen
import com.saludplus.citas.ui.screens.agendamiento.FechaHoraScreen
import com.saludplus.citas.ui.screens.agendamiento.MedicosScreen
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.auth.TerminosScreen
import com.saludplus.citas.ui.screens.citas.DetalleCitaScreen
import com.saludplus.citas.ui.screens.citas.MisCitasScreen
import com.saludplus.citas.ui.screens.doctores.DetalleMedicoScreen
import com.saludplus.citas.ui.screens.doctores.DoctoresScreen
import com.saludplus.citas.ui.screens.home.HomeScreen
import com.saludplus.citas.ui.screens.notificaciones.NotificacionesScreen
import com.saludplus.citas.ui.screens.perfil.PerfilScreen
import com.saludplus.citas.ui.screens.resultados.ResultadosScreen
import com.saludplus.citas.ui.screens.sedes.SedesScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Rutas.SPLASH

    AppNavigationDrawer(
        drawerState = drawerState,
        scope = scope,
        navController = navController,
        currentRoute = currentRoute
    ) {
        NavHost(navController = navController, startDestination = Rutas.SPLASH) {

            // ---------- Auth ----------
            composable(Rutas.SPLASH) { SplashScreen(navController) }
            composable(Rutas.REGISTRO) { RegistroScreen(navController) }
            composable(Rutas.LOGIN) { LoginScreen(navController) }
            composable(Rutas.TERMINOS) { TerminosScreen(navController) }

            // ---------- Home ----------
            composable(Rutas.HOME) { HomeScreen(navController, drawerState, scope) }

            // ---------- Sedes & Agendamiento ----------
            composable(Rutas.SEDES) { SedesScreen(navController) }

            composable(
                route = Rutas.ESPECIALIDADES,
                arguments = listOf(navArgument("sedeId") { type = NavType.IntType })
            ) { entry ->
                EspecialidadesScreen(
                    navController = navController,
                    sedeId = entry.arguments?.getInt("sedeId") ?: 1
                )
            }

            composable(
                route = Rutas.MEDICOS,
                arguments = listOf(
                    navArgument("sedeId") { type = NavType.IntType },
                    navArgument("especialidadId") { type = NavType.IntType }
                )
            ) { entry ->
                MedicosScreen(
                    navController = navController,
                    sedeId = entry.arguments?.getInt("sedeId") ?: 1,
                    especialidadId = entry.arguments?.getInt("especialidadId") ?: 1
                )
            }

            composable(
                route = Rutas.FECHA_HORA,
                arguments = listOf(
                    navArgument("medicoId") { type = NavType.IntType },
                    navArgument("sedeId") { type = NavType.IntType }
                )
            ) { entry ->
                FechaHoraScreen(
                    navController = navController,
                    medicoId = entry.arguments?.getInt("medicoId") ?: 0,
                    sedeId = entry.arguments?.getInt("sedeId") ?: 1
                )
            }

            composable(
                route = Rutas.CONFIRMAR_CITA,
                arguments = listOf(
                    navArgument("medicoId") { type = NavType.IntType },
                    navArgument("sedeId") { type = NavType.IntType },
                    navArgument("fecha") { type = NavType.StringType },
                    navArgument("hora") { type = NavType.StringType }
                )
            ) { entry ->
                ConfirmarCitaScreen(
                    navController = navController,
                    medicoId = entry.arguments?.getInt("medicoId") ?: 0,
                    sedeId = entry.arguments?.getInt("sedeId") ?: 1,
                    fecha = entry.arguments?.getString("fecha").orEmpty(),
                    hora = entry.arguments?.getString("hora").orEmpty()
                )
            }

            composable(
                route = Rutas.CITA_EXITOSA,
                arguments = listOf(navArgument("citaId") { type = NavType.IntType })
            ) { entry ->
                CitaExitosaScreen(
                    navController = navController,
                    citaId = entry.arguments?.getInt("citaId") ?: 0
                )
            }

            // ---------- Doctores (Drawer) ----------
            composable(Rutas.DOCTORES_LISTA) { DoctoresScreen(navController) }

            composable(
                route = Rutas.DETALLE_MEDICO,
                arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
            ) { entry ->
                DetalleMedicoScreen(
                    navController = navController,
                    medicoId = entry.arguments?.getInt("medicoId") ?: 0
                )
            }

            // ---------- Citas ----------
            composable(Rutas.MIS_CITAS) { MisCitasScreen(navController) }

            composable(
                route = Rutas.DETALLE_CITA,
                arguments = listOf(navArgument("citaId") { type = NavType.IntType })
            ) { entry ->
                DetalleCitaScreen(
                    navController = navController,
                    citaId = entry.arguments?.getInt("citaId") ?: 0
                )
            }

            // ---------- Perfil, resultados y notificaciones ----------
            composable(Rutas.PERFIL) { PerfilScreen(navController) }
            composable(Rutas.RESULTADOS) { ResultadosScreen(navController) }
            composable(Rutas.NOTIFICACIONES) { NotificacionesScreen(navController) }
        }
    }
}

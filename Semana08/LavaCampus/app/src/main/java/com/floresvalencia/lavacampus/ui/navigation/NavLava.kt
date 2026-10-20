package com.floresvalencia.lavacampus.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.floresvalencia.lavacampus.LavaApp
import com.floresvalencia.lavacampus.model.Rol
import com.floresvalencia.lavacampus.ui.administrador.ServiciosScreen
import com.floresvalencia.lavacampus.ui.estudiante.SplashScreen
import com.floresvalencia.lavacampus.ui.theme.LavaTheme
import com.floresvalencia.lavacampus.viewmodel.LavaViewModelFactory
import com.floresvalencia.lavacampus.viewmodel.ServiciosViewModel

@Composable
fun NavLava() {
    val app = LocalContext.current.applicationContext as LavaApp
    val nav = rememberNavController()

    // TODO (Paso 6): el tema depende del perfil activo y del modo oscuro guardado en DataStore
    LavaTheme(rol = Rol.ADMIN) {
        Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
            NavHost(
                navController = nav,
                startDestination = Rutas.SPLASH,
                modifier = Modifier.padding(padding)
            ) {
                composable(Rutas.SPLASH) {
                    // TODO (Paso 6): si hay perfil guardado ir a su grafo; si no, al login
                    val irAServicios = {
                        nav.navigate(Rutas.SERVICIOS) { popUpTo(Rutas.SPLASH) { inclusive = true } }
                    }
                    // TODO (Paso 6): los botones llevaran al login o al perfil guardado
                    SplashScreen(onComenzar = irAServicios, onTengoCuenta = irAServicios)
                }
                composable(Rutas.SERVICIOS) {
                    val vm: ServiciosViewModel = viewModel(factory = LavaViewModelFactory { ServiciosViewModel(app.repo) })
                    ServiciosScreen(vm, onEditar = { /* TODO (Paso 13): abrir FormularioServicio */ })
                }
                // TODO: agrega aqui los destinos de cada paso
            }
        }
    }
}

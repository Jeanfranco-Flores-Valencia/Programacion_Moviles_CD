package com.tecsup.manhwas.ui.navigation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.manhwas.AppContainer
import com.tecsup.manhwas.domain.model.Rol
import com.tecsup.manhwas.ui.UiState
import com.tecsup.manhwas.ui.ViewModelFactory
import com.tecsup.manhwas.ui.admin.AdminScreen
import com.tecsup.manhwas.ui.components.Cargando
import com.tecsup.manhwas.ui.detalle.DetalleScreen
import com.tecsup.manhwas.ui.favoritos.FavoritosScreen
import com.tecsup.manhwas.ui.lista.ListaScreen
import com.tecsup.manhwas.ui.login.LoginScreen
import com.tecsup.manhwas.ui.login.LoginViewModel

object Rutas {
    const val LOGIN = "login"
    const val LISTA = "lista"
    const val DETALLE = "detalle/{id}"
    const val FAVORITOS = "favoritos"
    const val ADMIN = "admin"

    fun detalle(id: Int) = "detalle/$id"
}

private data class Pestana(val ruta: String, val etiqueta: String, val icono: ImageVector)

@Composable
fun AppNav(container: AppContainer) {
    val nav = rememberNavController()
    val fabrica = remember(container) { ViewModelFactory(container) }

    // La sesion vive a nivel de la Activity: la comparten todas las pantallas
    val loginVm: LoginViewModel = viewModel(factory = fabrica)
    val verificado by loginVm.verificado.collectAsStateWithLifecycle()
    val estadoSesion by loginVm.estado.collectAsStateWithLifecycle()

    // Android 13+: se pide el permiso de notificaciones una vez. Si lo niega, la app sigue igual.
    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    if (!verificado) {
        Cargando("Verificando sesión...")
        return
    }

    val sesion = (estadoSesion as? UiState.Exito)?.datos
    val inicio = remember { if (sesion != null) Rutas.LISTA else Rutas.LOGIN }

    // Al ingresar, se pasa al catalogo y el login sale de la pila
    LaunchedEffect(sesion) {
        if (sesion != null && nav.currentDestination?.route == Rutas.LOGIN) {
            nav.navigate(Rutas.LISTA) { popUpTo(Rutas.LOGIN) { inclusive = true } }
        }
    }

    fun cerrarSesion() {
        loginVm.cerrarSesion()
        nav.navigate(Rutas.LOGIN) { popUpTo(nav.graph.id) { inclusive = true } }
    }

    // El rol decide que pestanas se ven: el panel solo para el administrador
    val pestanas = buildList {
        add(Pestana(Rutas.LISTA, "Catálogo", Icons.Outlined.AutoStories))
        add(Pestana(Rutas.FAVORITOS, "Favoritos", Icons.Outlined.FavoriteBorder))
        if (sesion?.rol == Rol.ADMIN) add(Pestana(Rutas.ADMIN, "Panel", Icons.Outlined.AdminPanelSettings))
    }
    val entrada by nav.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    Scaffold(
        // Cada pantalla maneja sus propios margenes de sistema (su TopAppBar); aqui no se duplican
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (sesion != null && pestanas.any { it.ruta == rutaActual }) {
                NavigationBar {
                    pestanas.forEach { p ->
                        NavigationBarItem(
                            selected = rutaActual == p.ruta,
                            onClick = { irAPestana(nav, p.ruta) },
                            icon = { Icon(p.icono, contentDescription = p.etiqueta) },
                            label = { Text(p.etiqueta) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(navController = nav, startDestination = inicio, modifier = Modifier.padding(padding)) {
            composable(Rutas.LOGIN) {
                LoginScreen(loginVm)
            }
            composable(Rutas.LISTA) {
                ListaScreen(
                    vm = viewModel(factory = fabrica),
                    onItem = { nav.navigate(Rutas.detalle(it)) },
                    onCerrarSesion = ::cerrarSesion
                )
            }
            composable(Rutas.DETALLE, arguments = listOf(navArgument("id") { type = NavType.IntType })) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                DetalleScreen(vm = viewModel(factory = ViewModelFactory(container, id)), onAtras = { nav.popBackStack() })
            }
            composable(Rutas.FAVORITOS) {
                FavoritosScreen(vm = viewModel(factory = fabrica), onItem = { nav.navigate(Rutas.detalle(it)) })
            }
            composable(Rutas.ADMIN) {
                // Doble control: aunque se llegue a la ruta, un usuario no ve el panel
                if (sesion?.rol == Rol.ADMIN) AdminScreen(vm = viewModel(factory = fabrica), onCerrarSesion = ::cerrarSesion)
            }
        }
    }
}

/** Cambia de pestana sin apilar pantallas repetidas. */
private fun irAPestana(nav: NavHostController, ruta: String) {
    nav.navigate(ruta) {
        popUpTo(Rutas.LISTA) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

package com.floresvalencia.lab06tecsupstore

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Lista elevada: sobrevive al cambiar de pantalla
    val productos = remember { mutableStateListOf<Producto>() }

    // Se recalcula solo cada vez que cambia la lista
    val cantidadFavoritos = productos.count { it.favorito }

    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route ?: Rutas.INICIO

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                rutaActual = rutaActual,
                cantidadFavoritos = cantidadFavoritos,
                onNavegar = { ruta ->
                    scope.launch { drawerState.close() }
                    if (ruta != rutaActual) {
                        navController.navigate(ruta) {
                            popUpTo(Rutas.INICIO) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                onCerrarSesion = {
                    scope.launch { drawerState.close() }
                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.INICIO) { inclusive = true }
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Flores Valencia Jeanfranco") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Rutas.INICIO,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Rutas.INICIO) {
                    PantallaCarrito(productos = productos)
                }
                composable(Rutas.PEDIDOS) {
                    PantallaSimple(
                        titulo = "Mis pedidos",
                        mensaje = "Aún no tienes pedidos registrados.",
                        icono = Icons.Default.ShoppingCart
                    )
                }
                composable(Rutas.FAVORITOS) {
                    PantallaFavoritos(productos = productos)
                }
                composable(Rutas.PERFIL) {
                    PantallaSimple(
                        titulo = "Perfil",
                        mensaje = "Flores Valencia Jeanfranco\nDiseño y Desarrollo de Software · 4to ciclo",
                        icono = Icons.Default.Person
                    )
                }
            }
        }
    }
}
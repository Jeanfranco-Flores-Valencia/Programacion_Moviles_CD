package com.floresvalencia.lavacampus.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.floresvalencia.lavacampus.LavaApp
import com.floresvalencia.lavacampus.model.Rol
import com.floresvalencia.lavacampus.ui.administrador.AjustesScreen
import com.floresvalencia.lavacampus.ui.administrador.BandejaScreen
import com.floresvalencia.lavacampus.ui.administrador.ClientesScreen
import com.floresvalencia.lavacampus.ui.administrador.FormularioServicioScreen
import com.floresvalencia.lavacampus.ui.administrador.GestionPedidoScreen
import com.floresvalencia.lavacampus.ui.administrador.PanelScreen
import com.floresvalencia.lavacampus.ui.administrador.ServiciosScreen
import com.floresvalencia.lavacampus.ui.estudiante.DetallePedidoScreen
import com.floresvalencia.lavacampus.ui.estudiante.InicioScreen
import com.floresvalencia.lavacampus.ui.estudiante.LoginScreen
import com.floresvalencia.lavacampus.ui.estudiante.RegistroScreen
import com.floresvalencia.lavacampus.ui.estudiante.MisPedidosScreen
import com.floresvalencia.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.floresvalencia.lavacampus.ui.estudiante.PerfilScreen
import com.floresvalencia.lavacampus.ui.estudiante.SplashScreen
import com.floresvalencia.lavacampus.ui.estudiante.TarifasScreen
import com.floresvalencia.lavacampus.ui.theme.LavaTheme
import com.floresvalencia.lavacampus.viewmodel.BandejaViewModel
import com.floresvalencia.lavacampus.viewmodel.ClientesViewModel
import com.floresvalencia.lavacampus.viewmodel.DetallePedidoViewModel
import com.floresvalencia.lavacampus.viewmodel.GestionPedidoViewModel
import com.floresvalencia.lavacampus.viewmodel.InicioViewModel
import com.floresvalencia.lavacampus.viewmodel.LavaViewModelFactory
import com.floresvalencia.lavacampus.viewmodel.MisPedidosViewModel
import com.floresvalencia.lavacampus.viewmodel.NuevoPedidoViewModel
import com.floresvalencia.lavacampus.viewmodel.PanelViewModel
import com.floresvalencia.lavacampus.viewmodel.ServiciosViewModel
import com.floresvalencia.lavacampus.viewmodel.SesionViewModel

/** Crea un ViewModel con la fabrica de la app (cada destino tiene su propia instancia). */
@Composable
private inline fun <reified VM : ViewModel> lavaViewModel(noinline crear: () -> VM): VM =
    viewModel(factory = LavaViewModelFactory(crear))

private data class ItemBarra(val ruta: String, val etiqueta: String, val icono: ImageVector)

/** 4 items para el estudiante. */
private val itemsEstudiante = listOf(
    ItemBarra(Rutas.INICIO, "Inicio", Icons.Filled.Home),
    ItemBarra(Rutas.MIS_PEDIDOS, "Pedidos", Icons.AutoMirrored.Filled.List),
    ItemBarra(Rutas.TARIFAS, "Tarifas", Icons.Outlined.LocalOffer),
    ItemBarra(Rutas.PERFIL, "Perfil", Icons.Outlined.Person)
)

/** 5 items para el administrador. */
private val itemsAdmin = listOf(
    ItemBarra(Rutas.PANEL, "Panel", Icons.Filled.BarChart),
    ItemBarra(Rutas.BANDEJA, "Pedidos", Icons.AutoMirrored.Filled.List),
    ItemBarra(Rutas.SERVICIOS, "Servicios", Icons.Outlined.LocalOffer),
    ItemBarra(Rutas.CLIENTES, "Clientes", Icons.Outlined.Group),
    ItemBarra(Rutas.AJUSTES, "Ajustes", Icons.Outlined.Settings)
)

@Composable
fun NavLava() {
    val app = LocalContext.current.applicationContext as LavaApp
    val nav = rememberNavController()

    // La sesion vive a nivel de la Activity: la comparten ambos grafos
    val sesion: SesionViewModel = lavaViewModel { SesionViewModel(app.repo, app.preferencias) }
    val usuario by sesion.usuarioActual.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val rol = Rol.desde(usuario?.rol) ?: Rol.CLIENTE

    val entrada by nav.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    /** Navega al grafo del perfil (o al login) limpiando toda la pila. */
    fun irAGrafo(destino: Rol?) {
        val ruta = when (destino) {
            Rol.ADMIN -> Rutas.GRAFO_ADMIN
            Rol.CLIENTE -> Rutas.GRAFO_ESTUDIANTE
            null -> Rutas.LOGIN
        }
        nav.navigate(ruta) {
            popUpTo(nav.graph.id) { inclusive = true }
            launchSingleTop = true
        }
    }

    LavaTheme(rol = rol, oscuro = oscuro) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = { BarraInferior(rutaActual, nav) }
        ) { padding ->
            NavHost(
                navController = nav,
                startDestination = Rutas.SPLASH,
                modifier = Modifier.padding(padding)
            ) {
                composable(Rutas.SPLASH) {
                    val cargando by sesion.cargando.collectAsState()
                    // El Splash no avanza solo: espera a que el usuario toque un boton
                    var destino by remember { mutableStateOf<String?>(null) }
                    SplashScreen(
                        onComenzar = { destino = Rutas.REGISTRO },   // Mejora IA: primero se registra
                        onTengoCuenta = { destino = Rutas.LOGIN }
                    )
                    LaunchedEffect(destino, cargando) {
                        val elegido = destino ?: return@LaunchedEffect
                        if (cargando) return@LaunchedEffect
                        destino = null
                        val guardado = Rol.desde(sesion.usuarioActual.value?.rol)
                        if (guardado != null) {
                            // Si ya hay perfil guardado salta el login y va directo a su pantalla principal
                            irAGrafo(guardado)
                        } else {
                            nav.navigate(elegido) { launchSingleTop = true }
                        }
                    }
                }
                // Mejora IA: registro -> login (el correo llega ya escrito)
                composable(Rutas.REGISTRO) {
                    RegistroScreen(
                        vm = sesion,
                        onAtras = { nav.popBackStack() },
                        onRegistrado = {
                            nav.navigate(Rutas.LOGIN) {
                                popUpTo(Rutas.REGISTRO) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onTengoCuenta = {
                            nav.navigate(Rutas.LOGIN) {
                                popUpTo(Rutas.REGISTRO) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable(Rutas.LOGIN) {
                    LoginScreen(
                        vm = sesion,
                        onIngresar = { irAGrafo(it) },
                        onRegistrarse = { nav.navigate(Rutas.REGISTRO) { launchSingleTop = true } }
                    )
                }

                // ---------------- Grafo del estudiante ----------------
                navigation(startDestination = Rutas.INICIO, route = Rutas.GRAFO_ESTUDIANTE) {
                    composable(Rutas.INICIO) {
                        val u = usuario ?: return@composable
                        val vm = lavaViewModel { InicioViewModel(app.repo, u) }
                        InicioScreen(
                            vm = vm,
                            onNuevoPedido = { nav.navigate(Rutas.nuevoPedido()) },
                            onMisPedidos = { irATab(nav, Rutas.MIS_PEDIDOS, Rutas.INICIO) },
                            onTarifas = { irATab(nav, Rutas.TARIFAS, Rutas.INICIO) },
                            onPedido = { nav.navigate(Rutas.detallePedido(it)) }
                        )
                    }
                    composable(
                        Rutas.NUEVO_PEDIDO,
                        arguments = listOf(navArgument("repetir") {
                            type = NavType.IntType
                            defaultValue = 0
                        })
                    ) { entry ->
                        val u = usuario ?: return@composable
                        val repetir = entry.arguments?.getInt("repetir") ?: 0
                        val vm = lavaViewModel { NuevoPedidoViewModel(app.repo, app.preferencias, u.id, repetir) }
                        NuevoPedidoScreen(
                            vm = vm,
                            onAtras = { nav.popBackStack() },
                            onCreado = {
                                nav.navigate(Rutas.MIS_PEDIDOS) {
                                    popUpTo(Rutas.INICIO)
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                    composable(
                        Rutas.DETALLE_PEDIDO,
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { entry ->
                        val id = entry.arguments?.getInt("id") ?: 0
                        val vm = lavaViewModel { DetallePedidoViewModel(app.repo, id) }
                        DetallePedidoScreen(vm = vm, onAtras = { nav.popBackStack() })
                    }
                    composable(Rutas.MIS_PEDIDOS) {
                        val u = usuario ?: return@composable
                        val vm = lavaViewModel { MisPedidosViewModel(app.repo, u.id) }
                        MisPedidosScreen(
                            vm = vm,
                            onAtras = { nav.popBackStack() },
                            onPedido = { nav.navigate(Rutas.detallePedido(it)) },
                            onNuevoPedido = { nav.navigate(Rutas.nuevoPedido()) },
                            onRepetir = { nav.navigate(Rutas.nuevoPedido(it)) }
                        )
                    }
                    composable(Rutas.TARIFAS) {
                        val u = usuario ?: return@composable
                        val vm = lavaViewModel { MisPedidosViewModel(app.repo, u.id) }
                        TarifasScreen(vm = vm, onAtras = { nav.popBackStack() })
                    }
                    composable(Rutas.PERFIL) {
                        val u = usuario ?: return@composable
                        val pedidosVm = lavaViewModel { MisPedidosViewModel(app.repo, u.id) }
                        PerfilScreen(
                            sesion = sesion,
                            pedidosVm = pedidosVm,
                            onAtras = { nav.popBackStack() },
                            onPerfilCambiado = { irAGrafo(it) },
                            onSesionCerrada = { irAGrafo(null) }
                        )
                    }
                }

                // ---------------- Grafo del administrador ----------------
                navigation(startDestination = Rutas.PANEL, route = Rutas.GRAFO_ADMIN) {
                    composable(Rutas.PANEL) {
                        val vm = lavaViewModel { PanelViewModel(app.repo) }
                        PanelScreen(vm = vm, onVerListos = { irATab(nav, Rutas.BANDEJA, Rutas.PANEL) })
                    }
                    composable(Rutas.BANDEJA) {
                        val vm = lavaViewModel { BandejaViewModel(app.repo) }
                        BandejaScreen(vm = vm, onGestionar = { nav.navigate(Rutas.gestionPedido(it)) })
                    }
                    composable(
                        Rutas.GESTION_PEDIDO,
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { entry ->
                        val id = entry.arguments?.getInt("id") ?: 0
                        val vm = lavaViewModel { GestionPedidoViewModel(app.repo, id) }
                        GestionPedidoScreen(vm = vm, onAtras = { nav.popBackStack() })
                    }
                    composable(Rutas.SERVICIOS) {
                        val vm = lavaViewModel { ServiciosViewModel(app.repo) }
                        ServiciosScreen(vm, onEditar = { nav.navigate(Rutas.formularioServicio(it)) })
                    }
                    composable(
                        Rutas.FORMULARIO_SERVICIO,
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { entry ->
                        val id = entry.arguments?.getInt("id") ?: 0
                        val vm = lavaViewModel { ServiciosViewModel(app.repo) }
                        FormularioServicioScreen(vm = vm, servicioId = id, onCerrar = { nav.popBackStack() })
                    }
                    composable(Rutas.CLIENTES) {
                        val vm = lavaViewModel { ClientesViewModel(app.repo) }
                        ClientesScreen(vm = vm)
                    }
                    composable(Rutas.AJUSTES) {
                        AjustesScreen(
                            sesion = sesion,
                            onPerfilCambiado = { irAGrafo(it) },
                            onSesionCerrada = { irAGrafo(null) }
                        )
                    }
                }
            }
        }
    }
}

/** Cambia de pestana de la barra inferior sin apilar pantallas repetidas. */
private fun irATab(nav: NavHostController, ruta: String, inicioGrafo: String) {
    nav.navigate(ruta) {
        popUpTo(inicioGrafo) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** NavigationBar: 4 items para el estudiante y 5 para el administrador. Solo en las pantallas principales. */
@Composable
private fun BarraInferior(rutaActual: String?, nav: NavHostController) {
    val ruta = rutaActual ?: return
    val items = when {
        itemsEstudiante.any { it.ruta == ruta } -> itemsEstudiante
        itemsAdmin.any { it.ruta == ruta } -> itemsAdmin
        else -> return
    }
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        items.forEach { item ->
            NavigationBarItem(
                selected = ruta == item.ruta,
                onClick = { if (ruta != item.ruta) irATab(nav, item.ruta, items.first().ruta) },
                icon = { Icon(item.icono, contentDescription = item.etiqueta) },
                label = { Text(item.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

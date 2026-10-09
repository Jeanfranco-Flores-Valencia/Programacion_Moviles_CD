package com.saludplus.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulOscuro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.BordeSuave
import com.saludplus.citas.ui.theme.Rojo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private data class DrawerItem(val ruta: String, val titulo: String, val icono: ImageVector)

@Composable
fun AppNavigationDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavController,
    currentRoute: String,
    content: @Composable () -> Unit
) {
    val usuario = Repositorio.usuarioActual
    var mostrarDialogoLogout by remember { mutableStateOf(false) }

    val items = listOf(
        DrawerItem(Rutas.SEDES, "Sedes", Icons.Filled.Business),
        DrawerItem(Rutas.DOCTORES_LISTA, "Doctores", Icons.Filled.MedicalServices),
        DrawerItem(Rutas.MIS_CITAS, "Mis Citas (Agenda)", Icons.Filled.CalendarMonth),
        DrawerItem(Rutas.PERFIL, "Mi Perfil", Icons.Filled.Person)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxHeight(),
                drawerContainerColor = Blanco
            ) {
                // Encabezado del Drawer con degradado de marca
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(AzulPrimario, AzulOscuro)
                            )
                        )
                        .padding(24.dp)
                ) {
                    val iniciales = usuario?.nombre
                        ?.split(" ")
                        ?.mapNotNull { it.firstOrNull()?.toString() }
                        ?.take(2)
                        ?.joinToString("") ?: "SP"

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Blanco),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = iniciales,
                            style = MaterialTheme.typography.titleLarge,
                            color = AzulPrimario,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = usuario?.nombre ?: "Paciente",
                        style = MaterialTheme.typography.titleMedium,
                        color = Blanco,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = usuario?.telefono.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Blanco.copy(alpha = 0.85f)
                    )
                    if (!usuario?.correo.isNullOrEmpty()) {
                        Text(
                            text = usuario?.correo.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Blanco.copy(alpha = 0.85f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Ítems de navegación
                items.forEach { item ->
                    val seleccionado = currentRoute == item.ruta
                    NavigationDrawerItem(
                        icon = { Icon(item.icono, contentDescription = item.titulo) },
                        label = { Text(item.titulo, fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal) },
                        selected = seleccionado,
                        onClick = {
                            scope.launch { drawerState.close() }
                            if (currentRoute != item.ruta) {
                                if (item.ruta == Rutas.HOME) {
                                    navController.popBackStack(Rutas.HOME, inclusive = false)
                                } else {
                                    navController.navigate(item.ruta) {
                                        popUpTo(Rutas.HOME)
                                        launchSingleTop = true
                                    }
                                }
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = AzulClaro,
                            selectedTextColor = AzulPrimario,
                            selectedIconColor = AzulPrimario
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = BordeSuave, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))

                // Cerrar sesión
                NavigationDrawerItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión", tint = Rojo) },
                    label = { Text("Cerrar sesión", color = Rojo, fontWeight = FontWeight.SemiBold) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        mostrarDialogoLogout = true
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        content()
    }

    if (mostrarDialogoLogout) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoLogout = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoLogout = false
                        Repositorio.cerrarSesion()
                        navController.navigate(Rutas.SPLASH) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                ) {
                    Text("Sí, salir", color = Rojo, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoLogout = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

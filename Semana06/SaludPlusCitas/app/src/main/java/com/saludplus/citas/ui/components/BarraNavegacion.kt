package com.saludplus.citas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.TextoSecundario

private data class ItemBarra(val ruta: String, val etiqueta: String, val icono: ImageVector)

private val itemsBarra = listOf(
    ItemBarra(Rutas.HOME, "Inicio", Icons.Filled.Home),
    ItemBarra(Rutas.MIS_CITAS, "Citas", Icons.Filled.CalendarMonth),
    ItemBarra(Rutas.RESULTADOS, "Resultados", Icons.Filled.Description),
    ItemBarra(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
)

/**
 * Menú principal (NavigationBar) con 4 destinos: Inicio, Citas, Resultados y Perfil.
 * Siempre deja Inicio como base de la pila para que "Atrás" vuelva a Inicio.
 */
@Composable
fun BarraNavegacionInferior(navController: NavController, rutaActual: String) {
    NavigationBar(containerColor = Blanco) {
        itemsBarra.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.ruta,
                onClick = {
                    if (rutaActual == item.ruta) return@NavigationBarItem
                    if (item.ruta == Rutas.HOME) {
                        navController.popBackStack(Rutas.HOME, inclusive = false)
                    } else {
                        navController.navigate(item.ruta) {
                            popUpTo(Rutas.HOME)
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(item.icono, contentDescription = item.etiqueta) },
                label = { Text(item.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AzulPrimario,
                    selectedTextColor = AzulPrimario,
                    indicatorColor = AzulClaro,
                    unselectedIconColor = TextoSecundario,
                    unselectedTextColor = TextoSecundario
                )
            )
        }
    }
}

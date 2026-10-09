package com.saludplus.citas.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.theme.Primario
import com.saludplus.citas.ui.theme.PrimarioClaro
import com.saludplus.citas.ui.theme.Superficie
import com.saludplus.citas.ui.theme.TextoSecundario

private data class ItemBarra(
    val ruta: String,
    val etiqueta: String,
    val iconoActivo: ImageVector,
    val iconoInactivo: ImageVector
)

private val itemsBarra = listOf(
    ItemBarra(Rutas.HOME, "Inicio", Icons.Filled.Home, Icons.Outlined.Home),
    ItemBarra(Rutas.SEDES, "Sedes", Icons.Filled.Business, Icons.Outlined.Business),
    ItemBarra(Rutas.MIS_CITAS, "Citas", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth),
    ItemBarra(Rutas.PERFIL, "Perfil", Icons.Filled.Person, Icons.Outlined.Person)
)

@Composable
fun BarraNavegacionInferior(navController: NavController, rutaActual: String) {
    val citasProxCount = Repositorio.citasDelUsuario().size

    NavigationBar(
        containerColor = Superficie,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        itemsBarra.forEach { item ->
            val seleccionado = rutaActual == item.ruta
            NavigationBarItem(
                selected = seleccionado,
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
                icon = {
                    val icono = if (seleccionado) item.iconoActivo else item.iconoInactivo
                    if (item.ruta == Rutas.MIS_CITAS && citasProxCount > 0) {
                        BadgedBox(
                            badge = { Badge { Text(citasProxCount.toString()) } }
                        ) {
                            Icon(icono, contentDescription = item.etiqueta)
                        }
                    } else {
                        Icon(icono, contentDescription = item.etiqueta)
                    }
                },
                label = { Text(item.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primario,
                    selectedTextColor = Primario,
                    indicatorColor = PrimarioClaro,
                    unselectedIconColor = TextoSecundario,
                    unselectedTextColor = TextoSecundario
                )
            )
        }
    }
}

package com.floresvalencia.lab06tecsupstore

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class ItemDrawer(val ruta: String, val titulo: String, val icono: ImageVector)

val itemsDrawer = listOf(
    ItemDrawer(Rutas.INICIO, "Inicio", Icons.Default.Home),
    ItemDrawer(Rutas.PEDIDOS, "Mis pedidos", Icons.Default.ShoppingCart),
    ItemDrawer(Rutas.FAVORITOS, "Favoritos", Icons.Default.Favorite),
    ItemDrawer(Rutas.PERFIL, "Perfil", Icons.Default.Person)
)

@Composable
fun AppDrawer(rutaActual: String, onNavegar: (String) -> Unit) {
    ModalDrawerSheet {
        Text(
            text = "TECSUP Store",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        itemsDrawer.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.titulo) },
                selected = item.ruta == rutaActual,
                onClick = { onNavegar(item.ruta) },
                icon = { Icon(item.icono, contentDescription = null) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}
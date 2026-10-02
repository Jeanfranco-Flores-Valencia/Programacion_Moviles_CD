package com.floresvalencia.lab06tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class ItemDrawer(val ruta: String, val titulo: String, val icono: ImageVector)

val itemsDrawer = listOf(
    ItemDrawer(Rutas.INICIO, "Inicio", Icons.Default.Home),
    ItemDrawer(Rutas.PEDIDOS, "Mis pedidos", Icons.Default.ShoppingCart),
    ItemDrawer(Rutas.FAVORITOS, "Favoritos", Icons.Default.Favorite),
    ItemDrawer(Rutas.PERFIL, "Perfil", Icons.Default.Person)
)

@Composable
fun AppDrawer(
    rutaActual: String,
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    ModalDrawerSheet {
        // Encabezado de usuario
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "JF",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Jeanfranco Flores Valencia",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Diseño y Desarrollo de Software · 4to ciclo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Destinos principales
        itemsDrawer.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.titulo) },
                selected = item.ruta == rutaActual,
                onClick = { onNavegar(item.ruta) },
                icon = { Icon(item.icono, contentDescription = null) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            selected = false,
            onClick = onCerrarSesion,
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}
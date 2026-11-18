package com.floresv.campusgps.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Mejora IA: chips de categoria que flotan sobre el mapa, como "Restaurantes" o "Gasolineras"
 * en Google Maps. Cada chip lleva el icono y el color de su categoria.
 */
@Composable
fun ChipsCategorias(
    categorias: List<String>,
    elegida: String?,
    onElegir: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        items(categorias) { cat ->
            val color = colorDeCategoria(cat)
            FilterChip(
                selected = cat == elegida,
                onClick = { onElegir(cat) },
                label = { Text(cat) },
                leadingIcon = { Icon(iconoDeCategoria(cat), contentDescription = null, modifier = Modifier.size(18.dp)) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    iconColor = color,
                    selectedContainerColor = color,
                    selectedLabelColor = MaterialTheme.colorScheme.surface,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.surface
                ),
                elevation = FilterChipDefaults.filterChipElevation(elevation = 3.dp)
            )
        }
    }
}

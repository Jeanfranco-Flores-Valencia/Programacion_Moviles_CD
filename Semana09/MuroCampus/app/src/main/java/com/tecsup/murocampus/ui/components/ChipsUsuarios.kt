package com.tecsup.murocampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Fila de chips: Todos y Usuario 1 a Usuario 10, cada uno con su avatar. El elegido se ve relleno. */
@Composable
fun ChipsUsuarios(elegido: Int?, onElegir: (Int?) -> Unit, modifier: Modifier = Modifier) {
    val opciones: List<Int?> = listOf(null) + (1..10)
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(opciones) { userId ->
            FilterChip(
                selected = userId == elegido,
                onClick = { onElegir(userId) },
                label = { Text(if (userId == null) "Todos" else "Usuario $userId") },
                leadingIcon = userId?.let { { AvatarUsuario(it, tamano = 20.dp) } },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

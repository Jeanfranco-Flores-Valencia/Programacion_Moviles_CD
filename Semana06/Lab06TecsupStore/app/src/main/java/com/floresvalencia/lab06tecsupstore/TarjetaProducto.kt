package com.floresvalencia.lab06tecsupstore

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TarjetaProducto(
    producto: Producto,
    onFavorito: () -> Unit,
    onCompartir: () -> Unit,
    onReportar: () -> Unit,
    onEliminar: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${formatoSoles(producto.precio)} x ${producto.cantidad}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = formatoSoles(producto.precio * producto.cantidad),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            // El menú vive en el mismo Box que su ícono para aparecer junto a él
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Más opciones"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Agregar a favoritos") },
                        onClick = { expanded = false; onFavorito() }
                    )
                    DropdownMenuItem(
                        text = { Text("Compartir") },
                        onClick = { expanded = false; onCompartir() }
                    )
                    DropdownMenuItem(
                        text = { Text("Reportar") },
                        onClick = { expanded = false; onReportar() }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = { expanded = false; onEliminar() }
                    )
                }
            }
        }
    }
}
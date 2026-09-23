package com.floresvalencia.tecsupfit.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.tecsupfit.data.ClaseGym
import com.floresvalencia.tecsupfit.data.DatosGym
import com.floresvalencia.tecsupfit.ui.components.IconoClase

@Composable
fun InicioScreen(onClaseClick: (ClaseGym) -> Unit) {
    // Estado local: filtro elegido en la fila de chips
    var filtroSeleccionado by remember { mutableStateOf(DatosGym.FILTRO_HOY) }

    val clasesFiltradas =
        if (filtroSeleccionado == DatosGym.FILTRO_HOY) DatosGym.clases.filter { it.esHoy }
        else DatosGym.clases

    Column(modifier = Modifier.fillMaxSize()) {

        // LazyRow: chips de filtro (seleccionado = verde relleno)
        LazyRow(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(DatosGym.filtros) { filtro ->
                FilterChip(
                    selected = filtro == filtroSeleccionado,
                    onClick = { filtroSeleccionado = filtro },
                    label = { Text(filtro) },
                    shape = RoundedCornerShape(50),
                    border = null,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        Text(
            text = "Clases disponibles",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 10.dp)
        )

        // LazyColumn: lista de clases
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(clasesFiltradas, key = { it.id }) { clase ->
                ClaseCard(clase = clase, onClick = { onClaseClick(clase) })
            }
        }
    }
}

@Composable
private fun ClaseCard(clase: ClaseGym, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoClase()
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = clase.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = clase.horarioTexto,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
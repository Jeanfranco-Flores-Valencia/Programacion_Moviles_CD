package com.floresv.campusgps.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import com.floresv.campusgps.util.formatearDistancia

/**
 * Mejora IA: selector del radio de cercania con botones segmentados de Material 3.
 * Deshabilitado hasta conocer la ubicacion, porque sin ella no hay distancias.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorRadio(
    opciones: List<Int>,
    elegido: Int?,
    dentro: Int,
    habilitado: Boolean,
    onElegir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = modifier) {
        Text(
            when {
                !habilitado -> "Radio de cercanía (necesita tu ubicación)"
                elegido == null -> "Radio de cercanía"
                else -> "$dentro de los lugares están a menos de ${formatearDistancia(elegido.toFloat())}"
            },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            opciones.forEachIndexed { i, metros ->
                SegmentedButton(
                    selected = metros == elegido,
                    onClick = { onElegir(metros) },
                    enabled = habilitado,
                    shape = SegmentedButtonDefaults.itemShape(index = i, count = opciones.size)
                ) { Text(formatearDistancia(metros.toFloat())) }
            }
        }
    }
}

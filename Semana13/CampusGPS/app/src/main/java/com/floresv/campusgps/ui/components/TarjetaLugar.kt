package com.floresv.campusgps.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresv.campusgps.model.LugarConDistancia
import com.floresv.campusgps.util.formatearDistancia

/** Fila de la lista: icono de categoria, nombre, categoria y distancia. La seleccionada se resalta. */
@Composable
fun TarjetaLugar(
    item: LugarConDistancia,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    detalle: (@Composable () -> Unit)? = null
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow
        ),
        border = if (seleccionado) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp)) {
            IconoCategoria(item.lugar.categoria)
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(item.lugar.nombre, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(item.lugar.categoria, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                detalle?.invoke()
            }
            Text(
                item.metros?.let { formatearDistancia(it) } ?: "Sin calcular",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = if (item.metros != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

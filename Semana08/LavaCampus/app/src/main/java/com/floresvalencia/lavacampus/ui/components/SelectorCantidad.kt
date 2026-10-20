package com.floresvalencia.lavacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Control de cantidad con botones menos y mas.
 * Recibe el valor, el paso, el minimo y el maximo. onCambiar recibe el delta (+paso o -paso);
 * la regla final (por ejemplo, minimo 1 kg al activar) la aplica el ViewModel.
 */
@Composable
fun SelectorCantidad(
    valor: Double,
    paso: Double,
    minimo: Double,
    maximo: Double,
    onCambiar: (Double) -> Unit,
    modifier: Modifier = Modifier,
    sufijo: String = ""
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        BotonRedondo(Icons.Filled.Remove, "Menos", habilitado = valor > 0.0) { onCambiar(-paso) }
        Text(
            text = numeroTexto(valor) + if (sufijo.isNotEmpty() && valor > 0) " $sufijo" else "",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (valor >= minimo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(min = 56.dp)
        )
        BotonRedondo(Icons.Filled.Add, "Mas", habilitado = valor < maximo) { onCambiar(paso) }
    }
}

@Composable
private fun BotonRedondo(icono: ImageVector, descripcion: String, habilitado: Boolean, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(if (habilitado) colores.primary else colores.surfaceVariant)
            .clickable(enabled = habilitado, onClick = onClick)
    ) {
        Icon(
            icono,
            contentDescription = descripcion,
            tint = if (habilitado) colores.onPrimary else colores.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
    }
}

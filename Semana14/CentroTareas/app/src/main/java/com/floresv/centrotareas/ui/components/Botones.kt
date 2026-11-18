package com.floresv.centrotareas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/** Mejora IA: botones reutilizables con el mismo alto, forma e icono en toda la app. */
private val Forma = RoundedCornerShape(14.dp)

@Composable
fun RowScope.BotonPrincipal(texto: String, icono: ImageVector, habilitado: Boolean = true, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = habilitado, shape = Forma, modifier = Modifier.weight(1f).height(48.dp)) {
        Icon(icono, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(texto)
    }
}

@Composable
fun RowScope.BotonSecundario(texto: String, icono: ImageVector, habilitado: Boolean = true, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, enabled = habilitado, shape = Forma, modifier = Modifier.weight(1f).height(48.dp)) {
        Icon(icono, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(texto)
    }
}

/** Fila de acciones que reparte el ancho entre sus botones. */
@Composable
fun FilaAcciones(contenido: @Composable RowScope.() -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth(), content = contenido)
}

package com.tecsup.murocampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/** Mejora IA: acciones del autor sobre su publicacion. Sin onEditar, solo se muestra Eliminar. */
@Composable
fun AccionesPublicacion(
    habilitado: Boolean,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier,
    onEditar: (() -> Unit)? = null
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        onEditar?.let { BotonAccion(Icons.Outlined.Edit, "Editar título", habilitado, peligro = false, onClick = it) }
        BotonAccion(Icons.Outlined.DeleteOutline, "Eliminar", habilitado, peligro = true, onClick = onEliminar)
    }
}

@Composable
private fun BotonAccion(icono: ImageVector, texto: String, habilitado: Boolean, peligro: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        enabled = habilitado,
        colors = if (peligro) ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        else ButtonDefaults.outlinedButtonColors()
    ) {
        Icon(icono, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(texto)
    }
}

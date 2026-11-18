package com.floresv.centrotareas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Recuadro de resultado: verde con check si salio bien, rojo con alerta si fallo. Titulo y detalle. */
@Composable
fun Resultado(titulo: String, detalle: String, exito: Boolean, modifier: Modifier = Modifier) {
    val fondo = if (exito) Color(0xFFE6F4EA) else MaterialTheme.colorScheme.errorContainer
    val tinta = if (exito) Color(0xFF1B5E20) else MaterialTheme.colorScheme.onErrorContainer
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(fondo).padding(14.dp)
    ) {
        Icon(if (exito) Icons.Outlined.CheckCircle else Icons.Outlined.ErrorOutline, contentDescription = null, tint = tinta)
        Column {
            Text(titulo, fontWeight = FontWeight.Bold, color = tinta)
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = tinta)
        }
    }
}

package com.floresvalencia.lavacampus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class EstiloBoton { LLENO, CONTORNO, PELIGRO }

/** Boton principal de la app. Toma el color del rol desde MaterialTheme. */
@Composable
fun LavaButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    estilo: EstiloBoton = EstiloBoton.LLENO,
    icono: ImageVector? = null
) {
    val forma = RoundedCornerShape(12.dp)
    val contenido: @Composable () -> Unit = {
        if (icono != null) {
            Icon(icono, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(texto, style = MaterialTheme.typography.labelLarge)
    }
    when (estilo) {
        EstiloBoton.LLENO -> Button(
            onClick = onClick,
            enabled = enabled,
            shape = forma,
            modifier = modifier.fillMaxWidth().height(50.dp)
        ) { contenido() }

        EstiloBoton.CONTORNO -> OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            shape = forma,
            border = BorderStroke(1.dp, if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
            modifier = modifier.fillMaxWidth().height(50.dp)
        ) { contenido() }

        EstiloBoton.PELIGRO -> OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            shape = forma,
            border = BorderStroke(1.dp, if (enabled) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            modifier = modifier.fillMaxWidth().height(50.dp)
        ) { contenido() }
    }
}

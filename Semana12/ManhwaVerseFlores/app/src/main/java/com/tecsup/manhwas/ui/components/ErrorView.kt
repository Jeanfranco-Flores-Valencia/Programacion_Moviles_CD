package com.tecsup.manhwas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.manhwas.ui.theme.Colores

/**
 * Pantalla de error o de estado vacio: icono en un circulo morado, mensaje y un boton opcional
 * (Reintentar por defecto). Mejora IA: usa BotonPrimario para que se vea igual que el resto.
 */
@Composable
fun ErrorView(
    mensaje: String,
    onReintentar: (() -> Unit)? = null,
    icono: ImageVector = Icons.Outlined.CloudOff,
    textoBoton: String = "Reintentar",
    iconoBoton: ImageVector = Icons.Outlined.Refresh
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(88.dp).clip(CircleShape).background(Colores.Morado.copy(alpha = 0.15f))
        ) {
            Icon(icono, contentDescription = null, tint = Colores.MoradoClaro, modifier = Modifier.size(44.dp))
        }
        Text(mensaje, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = Colores.Texto)
        if (onReintentar != null) {
            BotonPrimario(
                texto = textoBoton,
                onClick = onReintentar,
                icono = iconoBoton,
                modifier = Modifier.widthIn(max = 280.dp).fillMaxWidth()
            )
        }
    }
}

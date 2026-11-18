package com.floresv.centrotareas.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Mejora IA: "pulso" de la interfaz. El punto verde late con una animacion y el boton cuenta toques.
 * Si el hilo principal se bloquea, el punto deja de latir y el contador no suma: se ve al instante.
 */
@Composable
fun PulsoInterfaz(toques: Int, onTocar: () -> Unit, modifier: Modifier = Modifier) {
    val latido by rememberInfiniteTransition(label = "pulso").animateFloat(
        initialValue = 0.7f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "latido"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(24.dp)) {
            Box(modifier = Modifier.size(12.dp).scale(latido).clip(CircleShape).background(Color(0xFF2E7D32)))
        }
        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text("La pantalla responde", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(
                "Si el punto deja de latir o el contador no suma, está congelada",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        FilledTonalButton(onClick = onTocar) {
            Icon(Icons.Outlined.TouchApp, contentDescription = null, modifier = Modifier.size(18.dp))
            Text(" Tócame: $toques")
        }
    }
}

package com.floresv.centrotareas.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Mejora IA: anillo de progreso con el porcentaje en el centro (como el avance de una descarga). */
@Composable
fun AnilloProgreso(progreso: Float, modifier: Modifier = Modifier) {
    val suave by animateFloatAsState(progreso, label = "progreso")
    Box(contentAlignment = Alignment.Center, modifier = modifier.size(88.dp)) {
        CircularProgressIndicator(
            progress = { suave },
            strokeWidth = 8.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(88.dp)
        )
        Text("${(progreso * 100).toInt()} %", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

package com.floresv.centrotareas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Mejora IA: estado de cada tarjeta, como las etiquetas de un gestor de tareas. */
enum class EstadoTarea(val texto: String, val color: Color) {
    LISTA("Lista", Color(0xFF607D8B)),
    EN_CURSO("En curso", Color(0xFF1565C0)),
    COMPLETADA("Completada", Color(0xFF2E7D32)),
    CANCELADA("Cancelada", Color(0xFFEF6C00)),
    ERROR("Error", Color(0xFFC62828))
}

@Composable
fun InsigniaEstado(estado: EstadoTarea, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(estado.color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(estado.color))
        Text(estado.texto, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = estado.color)
    }
}

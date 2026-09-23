package com.floresvalencia.clinicasalud.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.floresvalencia.clinicasalud.data.EstadoCita

val ColorConfirmada = Color(0xFF1B7F3B)
val ColorConfirmadaFondo = Color(0xFFDDF4E4)
val ColorCompletada = Color(0xFF5F6368)
val ColorCompletadaFondo = Color(0xFFE8EAED)

/** Círculo con las iniciales del nombre. Ej: "Dra. Ana Torres" -> "AT" */
@Composable
fun AvatarIniciales(nombre: String, tamano: Dp = 56.dp) {
    val iniciales = nombre.split(" ")
        .filter { it.isNotBlank() && !it.endsWith(".") }   // ignora "Dr." / "Dra."
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Box(
        modifier = Modifier
            .size(tamano)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold,
            fontSize = (tamano.value / 2.8f).sp
        )
    }
}

@Composable
fun Calificacion(valor: Double) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color(0xFFFFB300),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "$valor", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun TituloSeccion(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

/**
 * Chip de SELECCIÓN ÚNICA: se ve como chip pero se comporta como RadioButton.
 * El estado (qué opción está elegida) NO vive aquí: vive en la pantalla padre en UNA
 * sola variable, por eso solo un chip puede tener seleccionado = true a la vez.
 */
@Composable
fun OpcionUnicaChip(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto) },
        leadingIcon = if (seleccionado) {
            { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
        } else null,
        modifier = Modifier.semantics { role = Role.RadioButton }
    )
}

@Composable
fun EstadoBadge(estado: EstadoCita) {
    val esConfirmada = estado == EstadoCita.CONFIRMADA
    val fondo = if (esConfirmada) ColorConfirmadaFondo else ColorCompletadaFondo
    val colorTexto = if (esConfirmada) ColorConfirmada else ColorCompletada

    Surface(color = fondo, shape = RoundedCornerShape(50)) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (esConfirmada) Icons.Filled.Schedule else Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = colorTexto,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = estado.texto,
                color = colorTexto,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun FilaResumen(icono: ImageVector, etiqueta: String, valor: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(etiqueta, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(valor, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}
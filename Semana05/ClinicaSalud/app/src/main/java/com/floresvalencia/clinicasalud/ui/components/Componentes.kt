package com.floresvalencia.clinicasalud.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.floresvalencia.clinicasalud.data.EstadoCita

val VerdeConfirmada = Color(0xFF1B8A55)
val VerdeConfirmadaFondo = Color(0xFFDDF4E8)
val GrisCompletada = Color(0xFF5F5B62)
val GrisCompletadaFondo = Color(0xFFE6E1E8)

/** Círculo lavanda con una cruz médica (+), como en el prototipo. */
@Composable
fun AvatarMedico(tamano: Dp = 44.dp) {
    Box(
        modifier = Modifier
            .size(tamano)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(tamano * 0.75f)
        )
    }
}

/** Círculo con las iniciales (se usará para "JP" en el menú lateral). */
@Composable
fun AvatarIniciales(nombre: String, tamano: Dp = 48.dp) {
    val iniciales = nombre.split(" ")
        .filter { it.isNotBlank() && !it.endsWith(".") }
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
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = (tamano.value / 2.8f).sp
        )
    }
}

/** Estrella dorada + puntaje. Si se pasan reseñas: "4.9 (128 reseñas)". */
@Composable
fun Calificacion(valor: Double, resenas: Int? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color(0xFFC9A000),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = if (resenas != null) "$valor ($resenas reseñas)" else "$valor",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Opción de SELECCIÓN ÚNICA con forma de "cajita" (fecha u hora).
 * - Seleccionada: fondo morado y texto blanco. No seleccionada: fondo lavanda.
 * - Modifier.selectable(role = Role.RadioButton) la hace comportarse como un RadioButton.
 * - El estado NO vive aquí: la pantalla padre guarda UNA sola variable con la opción elegida.
 */
@Composable
fun OpcionUnicaChip(
    textoPrincipal: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    textoSuperior: String? = null
) {
    val fondo = if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val colorTexto = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(fondo)
            .selectable(selected = seleccionado, onClick = onClick, role = Role.RadioButton)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (textoSuperior != null) {
            Text(textoSuperior, style = MaterialTheme.typography.labelSmall, color = colorTexto)
        }
        Text(
            text = textoPrincipal,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = if (textoSuperior != null) FontWeight.Bold else FontWeight.Medium,
            color = colorTexto
        )
    }
}

/** Etiqueta de estado: verde para Confirmada, gris para Completada. */
@Composable
fun EstadoBadge(estado: EstadoCita) {
    val esConfirmada = estado == EstadoCita.CONFIRMADA
    Surface(
        color = if (esConfirmada) VerdeConfirmadaFondo else GrisCompletadaFondo,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = estado.texto,
            color = if (esConfirmada) VerdeConfirmada else GrisCompletada,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
        )
    }
}
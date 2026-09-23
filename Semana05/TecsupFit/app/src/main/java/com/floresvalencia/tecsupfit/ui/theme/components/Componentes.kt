package com.floresvalencia.tecsupfit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.floresvalencia.tecsupfit.data.EstadoReserva

val GrisCompletada = Color(0xFF5F6368)
val GrisCompletadaFondo = Color(0xFFE2E2E2)

/**
 * Cuadro verde claro con una pesa. Se usa pequeño en las tarjetas
 * y grande (ancho completo) en el Detalle de clase.
 */
@Composable
fun IconoClase(
    modifier: Modifier = Modifier.size(44.dp),
    tamanoIcono: Dp = 26.dp
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.FitnessCenter,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(tamanoIcono)
        )
    }
}

/** Círculo verde claro con las iniciales. Ej: "Diego Ramos" -> "DR" */
@Composable
fun AvatarIniciales(nombre: String, tamano: Dp = 80.dp) {
    val iniciales = nombre.split(" ")
        .filter { it.isNotBlank() }
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
            fontSize = (tamano.value / 3f).sp
        )
    }
}

/**
 * Opción de SELECCIÓN ÚNICA (horario/cupo) con forma de píldora.
 * - Seleccionada: fondo verde y texto blanco. No seleccionada: fondo gris.
 * - Si no hay cupos se deshabilita (se ve más transparente y no responde).
 * - Modifier.selectable(role = Role.RadioButton) la hace comportarse como RadioButton.
 * - El estado NO vive aquí: la pantalla padre guarda UNA sola variable con la opción elegida.
 */
@Composable
fun OpcionUnicaChip(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    habilitado: Boolean = true
) {
    val fondo = if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val colorTexto = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Text(
        text = texto,
        color = colorTexto,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .alpha(if (habilitado) 1f else 0.4f)
            .clip(RoundedCornerShape(50))
            .background(fondo)
            .selectable(
                selected = seleccionado,
                enabled = habilitado,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
}

/** Etiqueta de estado: verde para Confirmada, gris para Completada. */
@Composable
fun EstadoBadge(estado: EstadoReserva) {
    val esConfirmada = estado == EstadoReserva.CONFIRMADA
    Surface(
        color = if (esConfirmada) MaterialTheme.colorScheme.primaryContainer else GrisCompletadaFondo,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = estado.texto,
            color = if (esConfirmada) MaterialTheme.colorScheme.primary else GrisCompletada,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
        )
    }
}
package com.tecsup.murocampus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/** Recuadro de resultado (verde si salio bien, rojo si fallo). */
@Composable
fun Aviso(texto: String, color: Color, fondo: Color, modifier: Modifier = Modifier) {
    Surface(
        color = fondo,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(texto, color = color, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(12.dp))
    }
}

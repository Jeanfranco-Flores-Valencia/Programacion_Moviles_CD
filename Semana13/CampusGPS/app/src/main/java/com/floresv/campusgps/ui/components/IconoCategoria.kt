package com.floresv.campusgps.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Circulo de color con el icono de la categoria (se usa en la lista y en la tarjeta del mas cercano). */
@Composable
fun IconoCategoria(categoria: String, tamano: Dp = 40.dp) {
    val color = colorDeCategoria(categoria)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(tamano).clip(CircleShape).background(color.copy(alpha = 0.15f))
    ) {
        Icon(iconoDeCategoria(categoria), contentDescription = categoria, tint = color, modifier = Modifier.size(tamano * 0.55f))
    }
}

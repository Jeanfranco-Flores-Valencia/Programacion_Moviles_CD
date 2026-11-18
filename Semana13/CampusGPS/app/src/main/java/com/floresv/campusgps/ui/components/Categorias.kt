package com.floresv.campusgps.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LocalParking
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/** Mejora IA: cada categoria tiene su color e icono, como en las apps de mapas. */
fun colorDeCategoria(categoria: String): Color = when (categoria) {
    "Estudio" -> Color(0xFF1E88E5)
    "Comida" -> Color(0xFFF4511E)
    "Eventos" -> Color(0xFF8E24AA)
    "Servicios" -> Color(0xFF00897B)
    else -> Color(0xFF546E7A)
}

fun iconoDeCategoria(categoria: String): ImageVector = when (categoria) {
    "Estudio" -> Icons.AutoMirrored.Outlined.MenuBook
    "Comida" -> Icons.Outlined.Restaurant
    "Eventos" -> Icons.Outlined.Event
    "Servicios" -> Icons.Outlined.LocalParking
    else -> Icons.Outlined.Place
}

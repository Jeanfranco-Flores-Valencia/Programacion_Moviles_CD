package com.saludplus.citas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.saludplus.citas.ui.theme.AcentoClaro
import com.saludplus.citas.ui.theme.Exito
import com.saludplus.citas.ui.theme.ExitoClaro
import com.saludplus.citas.ui.theme.Primario
import com.saludplus.citas.ui.theme.SecundarioClaro

/** Ícono de cada especialidad. */
fun iconoEspecialidad(especialidadId: Int): ImageVector = when (especialidadId) {
    1 -> Icons.Filled.Person
    2 -> Icons.Filled.ChildCare
    3 -> Icons.Filled.Female
    4 -> Icons.Filled.Favorite
    5 -> Icons.Filled.Face
    6 -> Icons.Filled.Healing
    7 -> Icons.Filled.Visibility
    8 -> Icons.Filled.Psychology
    else -> Icons.Filled.MedicalServices
}

/** Par (color de fondo, color del ícono) de cada especialidad en armonía con Verde Salvia. */
fun coloresEspecialidad(especialidadId: Int): Pair<Color, Color> = when (especialidadId) {
    1 -> Pair(SecundarioClaro, Primario)
    2 -> Pair(AcentoClaro, Color(0xFFC27C12))
    3 -> Pair(Color(0xFFF6E3EA), Color(0xFFB4577A))
    4 -> Pair(Color(0xFFF9DCD9), Color(0xFFB8433A))
    5 -> Pair(Color(0xFFF5E6DA), Color(0xFFB26B45))
    6 -> Pair(Color(0xFFE3EDF3), Color(0xFF4D7C99))
    7 -> Pair(Color(0xFFE6F0EC), Color(0xFF2F7B6B))
    8 -> Pair(Color(0xFFECE8F4), Color(0xFF6F619E))
    else -> Pair(ExitoClaro, Exito)
}

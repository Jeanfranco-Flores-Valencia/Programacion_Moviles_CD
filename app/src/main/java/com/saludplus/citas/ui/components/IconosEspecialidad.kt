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
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Celeste
import com.saludplus.citas.ui.theme.Morado
import com.saludplus.citas.ui.theme.MoradoClaro
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.NaranjaClaro
import com.saludplus.citas.ui.theme.Rojo
import com.saludplus.citas.ui.theme.RojoClaro
import com.saludplus.citas.ui.theme.Rosa
import com.saludplus.citas.ui.theme.RosaClaro
import com.saludplus.citas.ui.theme.VerdeClaro
import com.saludplus.citas.ui.theme.VerdeExito

/** Ícono de cada especialidad (el modelo no depende de Compose, por eso se asigna aquí). */
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

/** Par (color de fondo, color del ícono) de cada especialidad. */
fun coloresEspecialidad(especialidadId: Int): Pair<Color, Color> = when (especialidadId) {
    1 -> AzulClaro to AzulPrimario
    2 -> NaranjaClaro to Naranja
    3 -> RosaClaro to Rosa
    4 -> RojoClaro to Rojo
    5 -> NaranjaClaro to Naranja
    6 -> AzulClaro to Celeste
    7 -> AzulClaro to AzulPrimario
    8 -> MoradoClaro to Morado
    else -> VerdeClaro to VerdeExito
}

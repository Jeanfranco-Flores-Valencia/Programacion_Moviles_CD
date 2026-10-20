package com.floresvalencia.lavacampus.ui.components.graficos

import androidx.compose.ui.graphics.Color

/** Un valor para los graficos. Si color es Unspecified, el grafico usa el color primario del tema. */
data class DatoGrafico(
    val etiqueta: String,
    val valor: Float,
    val color: Color = Color.Unspecified
)

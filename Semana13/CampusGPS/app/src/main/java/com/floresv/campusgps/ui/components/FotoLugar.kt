package com.floresv.campusgps.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.floresv.campusgps.R
import com.floresv.campusgps.model.Lugar

/** Mejora IA: foto de cada lugar. La foto es un recurso de la vista, por eso el modelo solo guarda el id. */
@DrawableRes
fun fotoDeLugar(id: Int): Int? = when (id) {
    1 -> R.drawable.lugar_biblioteca
    2 -> R.drawable.lugar_cafeteria
    3 -> R.drawable.lugar_laboratorio
    4 -> R.drawable.lugar_auditorio
    5 -> R.drawable.lugar_estacionamiento
    else -> null
}

/** Foto recortada del lugar; si no tiene foto, muestra el color y el icono de su categoria. */
@Composable
fun FotoLugar(lugar: Lugar, modifier: Modifier = Modifier, esquinas: Dp = 12.dp) {
    val forma = RoundedCornerShape(esquinas)
    val foto = fotoDeLugar(lugar.id)
    if (foto != null) {
        Image(
            painter = painterResource(foto),
            contentDescription = lugar.nombre,
            contentScale = ContentScale.Crop,
            modifier = modifier.clip(forma)
        )
    } else {
        val color = colorDeCategoria(lugar.categoria)
        Box(contentAlignment = Alignment.Center, modifier = modifier.clip(forma).background(color.copy(alpha = 0.15f))) {
            Icon(iconoDeCategoria(lugar.categoria), contentDescription = lugar.nombre, tint = color, modifier = Modifier.size(32.dp))
        }
    }
}

/** Version cuadrada para la lista y la tarjeta del mas cercano. */
@Composable
fun MiniaturaLugar(lugar: Lugar, tamano: Dp = 64.dp) {
    FotoLugar(lugar, Modifier.size(tamano), esquinas = 12.dp)
}

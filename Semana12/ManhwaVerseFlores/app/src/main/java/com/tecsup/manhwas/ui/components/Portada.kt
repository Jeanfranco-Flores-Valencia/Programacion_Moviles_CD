package com.tecsup.manhwas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.manhwas.ui.theme.Colores

/**
 * Mejora IA: portada desde la URL de la API. Mientras carga o si no hay imagen
 * (por ejemplo sin internet) se ve un fondo morado con un libro, nunca un hueco vacio.
 */
@Composable
fun Portada(url: String?, descripcion: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(Brush.linearGradient(listOf(Colores.MoradoOscuro, Colores.Superficie)))
    ) {
        Icon(Icons.Outlined.AutoStories, contentDescription = null, tint = Colores.Morado.copy(alpha = 0.5f), modifier = Modifier.size(36.dp))
        AsyncImage(
            model = url,
            contentDescription = descripcion,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

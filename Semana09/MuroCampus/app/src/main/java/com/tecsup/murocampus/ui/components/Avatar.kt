package com.tecsup.murocampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.murocampus.ui.colorDeUsuario

/** Mejora IA: circulo con el numero del usuario y su color, como la foto de perfil de una red social. */
@Composable
fun AvatarUsuario(userId: Int, tamano: Dp = 40.dp) {
    Avatar(texto = "U$userId", color = colorDeUsuario(userId), tamano = tamano)
}

/** Circulo de color con un texto corto (iniciales). Lo usan los usuarios y los comentarios. */
@Composable
fun Avatar(texto: String, color: Color, tamano: Dp = 40.dp) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(tamano).clip(CircleShape).background(color)
    ) {
        Text(texto, color = Color.White, fontWeight = FontWeight.Bold, fontSize = (tamano.value * 0.36f).sp)
    }
}

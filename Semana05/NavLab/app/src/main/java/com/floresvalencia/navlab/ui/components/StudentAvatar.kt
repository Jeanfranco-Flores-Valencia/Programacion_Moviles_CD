package com.floresvalencia.navlab.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.floresvalencia.navlab.ui.theme.LavenderContainer
import com.floresvalencia.navlab.ui.theme.PurplePrimary

@Composable
fun StudentAvatar(
    name: String,
    modifier: Modifier = Modifier,
    photoRes: Int? = null,
    size: Dp = 48.dp,
    border: BorderStroke? = null
) {
    val initials = name.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "A" }

    var baseModifier = modifier
        .size(size)
        .clip(CircleShape)

    if (border != null) {
        baseModifier = baseModifier.border(border, CircleShape)
    }

    if (photoRes != null) {
        Image(
            painter = painterResource(id = photoRes),
            contentDescription = "Foto de $name",
            contentScale = ContentScale.Crop,
            modifier = baseModifier
        )
    } else {
        Box(
            modifier = baseModifier.background(LavenderContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = PurplePrimary,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.38f).sp
            )
        }
    }
}

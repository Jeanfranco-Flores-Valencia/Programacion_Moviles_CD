package com.floresvalencia.navlab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.floresvalencia.navlab.ui.theme.PurplePrimary
import com.floresvalencia.navlab.ui.theme.PurpleSecondary

@Composable
fun GradientHeader(
    modifier: Modifier = Modifier,
    height: Dp = 160.dp,
    horizontalGradient: Boolean = false,
    bottomCornerRadius: Dp = 24.dp,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val gradient = if (horizontalGradient) {
        Brush.horizontalGradient(
            colors = listOf(PurplePrimary, PurpleSecondary)
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(PurplePrimary, PurpleSecondary)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(bottomStart = bottomCornerRadius, bottomEnd = bottomCornerRadius))
            .background(gradient),
        contentAlignment = Alignment.Center,
        content = content
    )
}

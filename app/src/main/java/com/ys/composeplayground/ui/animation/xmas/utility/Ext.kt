package com.ys.composeplayground.ui.animation.xmas.utility

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawSnowflake(x: Float, y: Float, size: Float, alpha: Float) {
    drawCircle(
        color = Color.White.copy(alpha = alpha),
        radius = size,
        center = Offset(x, y),
    )
}
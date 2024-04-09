package com.ys.composeplayground.ui.sample.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSpacer(
    space: Dp = 8.dp,
    color: Color = Color.Transparent,
) {
    Spacer(
        modifier = Modifier
            .height(space)
            .background(color)
    )
}
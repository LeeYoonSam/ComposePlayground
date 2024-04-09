package com.ys.composeplayground.ui.sample.product.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSpaceColumn(
    topSpace: Dp = 8.dp,
    bottomSpace: Dp = 8.dp,
    content: @Composable () ->  Unit
) {
    Column {
        VerticalSpacer(topSpace)
        content()
        VerticalSpacer(bottomSpace)
    }
}
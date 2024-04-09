package com.ys.composeplayground.ui.sample.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MembershipBadge(content: @Composable () -> Unit) {
    Column(modifier = Modifier
        .wrapContentSize()
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFF55898),
                    Color(0xFF5352E7),
                    Color(0xFF11A1E5),
                )
            ),
            shape = RoundedCornerShape(100.dp)
        )
        .padding(
            vertical = 3.dp,
            horizontal = 8.dp
        ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
    }
}
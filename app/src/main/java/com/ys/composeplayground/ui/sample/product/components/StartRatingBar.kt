package com.ys.composeplayground.ui.sample.product.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlin.math.roundToInt

@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    startSize: Float = 10f,
    rating: Float,
    spacing: Float = 0f
) {
    val starSize = startSize.dp
    val starSpacing = spacing.dp

    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating.roundToInt()
            val icon = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star
            val iconTintColor = if (isSelected) Color(0xFFFFAF00) else Color(0xFFF5F5F5)
            Icon(
                imageVector = icon,
                contentDescription = "Start Rating",
                tint = iconTintColor,
                modifier = Modifier
                    .size(starSize)
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}

@Composable
@Preview
fun PreviewStarRatingBar() {
    ComposePlaygroundTheme {
        Column {
            StarRatingBar(rating = 4.8f)
            StarRatingBar(rating = 4.4f)
            StarRatingBar(rating = 3.4f)
            StarRatingBar(rating = 2.1f)
            StarRatingBar(rating = 1.4f)
            StarRatingBar(rating = 0f)
        }
    }
}
package com.ys.composeplayground.ui.sample.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

@Composable
fun Badge(badgeModel: BadgeModel) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(2.dp))
            .background(badgeModel.backgroundColor)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        badgeModel.iconVector?.let {
            Icon(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(10.dp),
                imageVector = it,
                tint = badgeModel.badgeTextColor,
                contentDescription = "badge icon"
            )
        }
        Text(
            text = badgeModel.badgeText,
            fontSize = badgeModel.badgeTextSize,
            color = badgeModel.badgeTextColor,
        )
    }
}

@Composable
@Preview
fun PreviewBadge() {
    ComposePlaygroundTheme {
        LazyRow(Modifier.fillMaxWidth()) {
            item {
                Badge(badges[0])
                Spacer(modifier = Modifier.width(4.dp))
                Badge(badges[1])
                Spacer(modifier = Modifier.width(4.dp))
                Badge(badges[3])
            }
        }
    }
}
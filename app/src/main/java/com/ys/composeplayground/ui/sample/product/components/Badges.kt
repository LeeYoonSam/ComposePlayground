package com.ys.composeplayground.ui.sample.product.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

@Composable
fun Badges(
    modifier: Modifier = Modifier,
    badges: List<BadgeModel>,
    badgeSpace: Dp = 0.dp,
) {
    LazyRow(modifier.fillMaxWidth()) {
        item {
            badges.forEach {
                Badge(badgeModel = it)
                Spacer(modifier = Modifier.size(badgeSpace))
            }
        }
    }
}

@Composable
@Preview
fun PreviewBadges() {
    ComposePlaygroundTheme {
        Badges(badges = badges)
    }
}
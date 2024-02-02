package com.ys.composeplayground.ui.sample.photoapp.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FeedHeader() {
    Surface(
        color = MaterialTheme.colorScheme.onSurface,
        contentColor = MaterialTheme.colorScheme.surface
    ) {
        Column {
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                text = "Hello",
                style = MaterialTheme.typography.titleSmall.copy(color = Color.White),
                modifier = Modifier.padding(start = 16.dp),
            )
            Text(
                text = "Alice",
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                modifier = Modifier.padding(start = 16.dp),
            )
            Spacer(modifier = Modifier.size(16.dp))
            RoundedHeader(title = "Your feed")
        }
    }
}
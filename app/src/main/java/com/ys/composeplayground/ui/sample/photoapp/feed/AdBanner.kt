package com.ys.composeplayground.ui.sample.photoapp.feed

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AdBanner() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(4.dp)),
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = "Banner Sample",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
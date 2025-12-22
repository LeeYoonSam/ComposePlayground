package com.ys.composeplayground.ui.sample.photoapp.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.sample.photoapp.Photographer

@Composable
fun ProfileHeader(photographer: Photographer, tutorialHighlightModifier: Modifier) {
    val padding = 16.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = padding,
                end = padding,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = photographer.avatar),
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.size(padding))
        Column(Modifier.weight(1f)) {
            Text(
                text = photographer.name,
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FollowerInfo(text = "followers", number = photographer.numOfFollowers)
                FollowerInfo(text = "following", number = photographer.numOfFollowing)
                Button(
                    onClick = {},
                    shape = CircleShape,
                    modifier = tutorialHighlightModifier
                ) {
                    Text(text = "Follow")
                }
            }
        }
    }
}

@Composable
private fun FollowerInfo(text: String, number: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = number, style = MaterialTheme.typography.titleMedium)
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }
}
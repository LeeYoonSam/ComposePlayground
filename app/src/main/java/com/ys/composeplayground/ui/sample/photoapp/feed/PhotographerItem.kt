package com.ys.composeplayground.ui.sample.photoapp.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.R
import com.ys.composeplayground.ui.sample.photoapp.Photographer
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

@Composable
fun PhotographerCard(
    photographer: Photographer,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val padding = 16.dp

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(
                vertical = padding / 2,
                horizontal = padding,
            )
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = photographer.avatar),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.size(padding))
            Column {
                Text(
                    text = photographer.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Text(text = photographer.lastSeenOnline, style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(modifier = Modifier.size(padding))
        Image(
            painter = painterResource(id = photographer.mainImage),
            contentDescription = "mainImage",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .shadow(elevation = 4.dp, clip = true, shape = RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview
@Composable
fun PhotographerItemPreview() {
    val demoPhotographer = Photographer(
        "id",
        "Patricia Stevenson",
        "3 minutes ago",
        R.drawable.ava1,
        R.drawable.image1,
        "0",
        "0",
        emptyList(),
        emptyMap()
    )
    ComposePlaygroundTheme {
        Surface {
            PhotographerCard(demoPhotographer, {})
        }
    }
}
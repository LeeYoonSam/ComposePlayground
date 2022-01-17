package com.ys.composeplayground.ui.album

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.R
import com.ys.composeplayground.ui.album.data.Album
import com.ys.composeplayground.ui.album.data.getAlbumSample

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumScreen() {
    val albumList = getAlbumSample()

    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        contentPadding = PaddingValues(6.dp),
        content = {
            items(
                items = albumList,
                itemContent = { item ->
                    CardImageView(album = item)
                }
            )
        }
    )
}

@Composable
fun CardImageView(album: Album) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(4.dp),
        elevation = 4.dp
    ) {
        CardImageContent(album = album)
    }
}

@Composable
private fun CardImageContent(album: Album) {
    val image: Painter = painterResource(id = R.drawable.composelogo)

    Image(
        painter = image,
        contentDescription = ""
    )
}

@Preview
@Composable
fun PreviewCardImageView() {
    CardImageView(album = Album(0, "testPath"))
}

@Preview
@Composable
fun PreviewAlbumScreen() {
    AlbumScreen()
}
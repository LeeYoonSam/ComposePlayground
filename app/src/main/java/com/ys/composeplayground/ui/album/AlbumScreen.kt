package com.ys.composeplayground.ui.album

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.ys.composeplayground.R
import com.ys.composeplayground.ui.album.data.MediaStoreImage
import com.ys.composeplayground.ui.album.data.ProviderMediaStoreImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumScreen(albumList: List<MediaStoreImage>) {

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
fun CardImageView(album: MediaStoreImage) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(4.dp),
        elevation = 4.dp
    ) {
        CardImageContent(album = album)
    }
}

@Composable
private fun CardImageContent(album: MediaStoreImage) {

    val painter = rememberImagePainter(
        data = album.contentUri,
        onExecute = ImagePainter.ExecuteCallback { _, _ -> true },
        builder = {
            scale(Scale.FILL)
        }
    )

    Box(modifier = Modifier.size(100.dp)) {
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier.align(Alignment.Center)
        )

        when (painter.state) {
            is ImagePainter.State.Loading -> {
                // Display a circular progress indicator whilst loading
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            is ImagePainter.State.Error -> {
                // If you wish to display some content if the request fails
            }
        }
    }
}

@Preview
@Composable
fun PreviewCardImageView() {
    CardImageView(album = MediaStoreImage(0, "testPath"))
}

@Preview
@Composable
fun PreviewAlbumScreen() {
    AlbumScreen(ProviderMediaStoreImage.getAlbumList())
}
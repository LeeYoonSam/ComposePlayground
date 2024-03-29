package com.ys.composeplayground.ui.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ys.composeplayground.R
import com.ys.composeplayground.ui.album.data.MediaStoreImage
import com.ys.composeplayground.ui.album.data.ProviderMediaStoreImage

const val GRID_FIXED_COUNT = 3
val GRID_CONTENT_PADDING = 4.dp
val GRID_PADDING = 4.dp

@Composable
fun AlbumScreen(albumList: List<MediaStoreImage>) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_FIXED_COUNT),
        contentPadding = PaddingValues(GRID_CONTENT_PADDING)
    ) {
        items(
            count = albumList.size,
            itemContent = { position ->
                val item = albumList[position]
                CardImageView(album = item)
            }
        )
    }
}

@Composable
fun CardImageView(album: MediaStoreImage) {
    Column(
        Modifier.padding(GRID_PADDING)
    ) {
        CardImageContent(album = album)
    }
}

@Composable
private fun CardImageContent(album: MediaStoreImage) {

    val painter = rememberImagePainter(
        data = album.contentUri
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val totalPadding = (GRID_FIXED_COUNT + 1) * (GRID_CONTENT_PADDING.value + GRID_PADDING.value)
    val size = (screenWidth - totalPadding.dp) / GRID_FIXED_COUNT

    Box(modifier = Modifier.size(size)) {
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .clip(RoundedCornerShape(5.dp)),
            contentScale = ContentScale.Crop
        )
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
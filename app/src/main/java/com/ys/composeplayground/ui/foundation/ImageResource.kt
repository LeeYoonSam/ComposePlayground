package com.ys.composeplayground.ui.foundation

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.ys.composeplayground.R

@Preview
@Composable
fun ImageResourceDemo() {
    val image: Painter = painterResource(id = R.drawable.composelogo)
    Image(
        painter = image,
        contentScale = ContentScale.Crop,
        contentDescription = ""
    )
}
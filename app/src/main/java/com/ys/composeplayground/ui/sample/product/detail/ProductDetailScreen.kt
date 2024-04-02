package com.ys.composeplayground.ui.sample.product.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.R
import com.ys.composeplayground.ui.sample.product.components.StarRatingBar
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

@Composable
fun ProductDetailScreen() {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .background(Color.White)
        .verticalScroll(scrollState)
    ) {
        ImageUnit()
        ProductBaseInfo()
        UnitPadding()
    }
}

@Composable
fun UnitPadding() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .background(Color.Gray)
    )
}

@Composable
fun ImageUnit() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(50.dp),
            text = "Image Unit"
        )
    }
}

@Composable
fun ProductBaseInfo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
    ) {
        ProductInfoArtist()
    }
}

@Composable
fun ProductInfoArtist() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(24.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.ava1),
                contentDescription = "ArtistName"
            )
            Text(
                text = "캬옹이",
                fontSize = 12.sp,
                color = Color.DarkGray,
            )
            Image(
                modifier = Modifier.size(12.dp),
                colorFilter = ColorFilter.tint(Color.Gray),
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "arrow right"
            )
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            StarRatingBar(
                rating = 4.3f,
                startSize = 12f,
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "4.8(521)",
                fontSize = 12.sp,
                color = Color.DarkGray,
            )
            Image(
                modifier = Modifier.size(12.dp),
                colorFilter = ColorFilter.tint(Color.Gray),
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "arrow right"
            )
        }
    }
}

@Preview
@Composable
fun PreviewProductDetailScreen() {
    ComposePlaygroundTheme {
        ProductDetailScreen()
    }
}

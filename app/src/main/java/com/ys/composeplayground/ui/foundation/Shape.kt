package com.ys.composeplayground.ui.foundation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * RectangleShape
 */
@Composable
fun RectangleShapeDemo() {
    ExampleBox(shape = RectangleShape)
}

@Preview
@Composable
fun previewRectangleShapeDemo() {
    RectangleShapeDemo()
}

/**
 * CircleShape
 */
@Composable
fun CircleShapeDemo() {
    ExampleBox(shape = CircleShape)
}

@Preview
@Composable
fun previewCircleShapeDemo() {
    CircleShapeDemo()
}

/**
 * RoundedCornerShape
 */
@Composable
fun RoundedCornerShapeDemo() {
    ExampleBox(shape = RoundedCornerShape(10.dp))
}

@Preview
@Composable
fun previewRoundedCornerShapeDemo() {
    RoundedCornerShapeDemo()
}

/**
 * CutCornerShape
 */
@Composable
fun CutCornerShapeDemo() {
    ExampleBox(shape = CutCornerShape(10.dp))
}

@Preview
@Composable
fun previewCutCornerShapeDemo() {
    CutCornerShapeDemo()
}

@Composable
fun ExampleBox(shape: Shape) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.Center)) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(shape)
        ) {
            ImageResourceDemo()
        }
    }
}
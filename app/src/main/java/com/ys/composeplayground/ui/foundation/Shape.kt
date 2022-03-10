package com.ys.composeplayground.ui.foundation

import android.widget.Scroller
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ShapeDemo() {

    val scrollState = rememberScrollState() // ScrollState 에 대한 remember

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .padding(16.dp)
        .verticalScroll(scrollState) // Column 에 ScrollState 설정
    ) {
        RectangleShapeDemo()
        AddSpace()
        CircleShapeDemo()
        AddSpace()
        RoundedCornerShapeDemo()
        AddSpace()
        CutCornerShapeDemo()
    }
}

@Composable
fun AddSpace() {
    Spacer(modifier = Modifier.height(20.dp))
}

@Preview
@Composable
fun PreviewShapeDemo() {
    ShapeDemo()
}

/**
 * RectangleShape
 */
@Composable
fun RectangleShapeDemo() {
    ExampleBox(shape = RectangleShape)
}

@Preview
@Composable
fun PreviewRectangleShapeDemo() {
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
fun PreviewCircleShapeDemo() {
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
fun PreviewRoundedCornerShapeDemo() {
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
fun PreviewCutCornerShapeDemo() {
    CutCornerShapeDemo()
}

@Composable
fun ExampleBox(shape: Shape) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(shape)
        ) {
            ImageResourceDemo()
        }
    }
}
package com.ys.composeplayground.ui.material

import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

/**
 * Slider
 *
 * 슬라이더를 사용하면 값 범위에서 선택할 수 있습니다.
 */
@Composable
fun SliderDemo() {
    var sliderPosition by remember { mutableStateOf(0f) }
    Text(text = sliderPosition.toString())
    Slider(value = sliderPosition, onValueChange = { sliderPosition = it } )
}

@Preview
@Composable
fun PreviewSlider() {
    SliderDemo()
}
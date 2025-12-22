package com.ys.composeplayground.ui.material

import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

/**
 * Slider
 *
 * 슬라이더를 사용하면 값 범위에서 선택할 수 있습니다.
 */
@Composable
fun SliderDemo() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Text(text = sliderPosition.toString())
    Slider(value = sliderPosition, onValueChange = { sliderPosition = it } )
}

@Preview
@Composable
fun PreviewSlider() {
    SliderDemo()
}
package com.ys.composeplayground.ui.material

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 머티리얼 디자인 표면.
 * 머티리얼 표면은 머티리얼 디자인의 중요한 메타포입니다.
 * 각 표면은 지정된 고도에 존재하며,
 * 이는 해당 표면이 다른 표면과 시각적으로 관련되는 방식 및 해당 표면이 그림자를 그리는 방식에 영향을 줍니다.
 * 클릭을 처리하는 Surface를 사용하려면 다른 오버로드를 사용하는 것이 좋습니다.
 */

@Composable
fun SurfaceDemo() {
    Surface(
        modifier = Modifier.padding(8.dp),
        border = BorderStroke(2.dp, Color.Red),
        contentColor = Color.Blue,
        shadowElevation = 8.dp,
        shape = CircleShape
    ) {
        Text("Hello World", modifier = Modifier.padding(8.dp))
    }
}

@Preview
@Composable
fun PreviewSurface() {
    SurfaceDemo()
}
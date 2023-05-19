package com.ys.composeplayground.ui.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

/**
 * @Composable
 * fun Popup(
 *     alignment: Alignment = Alignment.TopStart,
 *     offset: IntOffset = IntOffset(0, 0),
 *     onDismissRequest: (() -> Unit)? = null,
 *     properties: PopupProperties = PopupProperties(),
 *     content: @Composable () -> Unit
 * )
 *
 * Parameters
 *
 * alignment
 * 부모를 기준으로 한 정렬입니다.
 *
 * offset
 * 팝업의 원래 정렬된 위치로부터의 오프셋입니다. 오프셋은 Ltr/Rtl 컨텍스트를 존중하므로 Ltr에서는 원래 정렬된 위치에 더하고 Rtl에서는 원래 정렬된 위치에서 뺍니다.
 *
 * onDismissRequest
 * 사용자가 팝업 외부를 클릭할 때 실행됩니다.
 *
 * properties
 * 이 팝업의 동작을 추가로 사용자 정의할 수 있는 PopupProperties.
 *
 * content
 * 팝업 안에 표시할 콘텐츠
 */

@Composable
@Preview
fun PopupSample() {
    Box(modifier = Modifier.fillMaxSize()) {
        val popupWidth = 200.dp
        val popupHeight = 50.dp
        val cornerSize = 16.dp

        Popup(alignment = Alignment.Center) {
            // Draw a rectangle shape with rounded corners inside the popup
            Box(
                Modifier
                    .size(popupWidth, popupHeight)
                    .background(Color.White, RoundedCornerShape(cornerSize))
            ) {
                Text(text = "PopupTest")
            }
        }
    }
}

/**
 * @Composable
 * fun Popup(
 *     popupPositionProvider: PopupPositionProvider,
 *     onDismissRequest: (() -> Unit)? = null,
 *     properties: PopupProperties = PopupProperties(),
 *     content: @Composable () -> Unit
 * )
 *
 * Parameters
 *
 * popupPositionProvider
 * 팝업의 화면 위치를 제공합니다.
 *
 * onDismissRequest
 * 사용자가 팝업 외부를 클릭할 때 실행됩니다.
 *
 * properties
 * 이 팝업의 동작을 추가로 사용자 정의할 수 있는 PopupProperties.
 *
 * content
 * 팝업 안에 표시할 콘텐츠
 */

@Composable
@Preview
fun PopupSample2() {
    Box(modifier = Modifier.fillMaxSize()) {
        val popupWidth = 200.dp
        val popupHeight = 50.dp
        val cornerSize = 16.dp

        Popup(alignment = Alignment.Center) {
            // Draw a rectangle shape with rounded corners inside the popup
            Box(
                Modifier
                    .size(popupWidth, popupHeight)
                    .background(Color.White, RoundedCornerShape(cornerSize))
            ) {
                Text(text = "PopupTest2")
            }
        }
    }
}

package com.ys.composeplayground.ui.foundation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * SelectionContainer
 *
 * 직접 또는 간접 자식에 대한 텍스트 선택을 활성화합니다.
 *
 * @Composable
 * fun SelectionContainer(modifier: Modifier = Modifier, content: @Composable () -> Unit)
 */

@Composable
fun SelectionSample() {
    SelectionContainer {
        Column {
            Text(text = "Selection Text 1")
            Text(text = "Selection Text 2")
            Text(text = "Selection Text 3")
        }
    }
}

@Preview
@Composable
fun SelectionSamplePreview() {
    Column {
        // SelectionContainer 의 Text 는 길게 눌러서 텍스트 선택이 가능
        SelectionSample()

        // 일반 Text 는 선택 불가능
        Text(text = "General Text 1")
        Text(text = "General Text 2")
        Text(text = "General Text 3")
    }

}

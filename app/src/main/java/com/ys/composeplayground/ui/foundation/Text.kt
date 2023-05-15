package com.ys.composeplayground.ui.foundation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 텍스트를 사용하여 텍스트를 표시할 수 있습니다.
 * style 인수를 사용하여 textdecoration 또는 fontfamily와 같은 것을 정의할 수 있습니다.
 */
@Composable
fun TextDemo() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(
            items = listOf(0, 1, 2, 3, 4),
            itemContent =  { item ->

                when(item) {
                    0 -> {
                        NormalTextExample()
                    }

                    1 -> {
                        CursiveTextExample()
                    }

                    2 -> {
                        TextWithLineThroughExample()
                    }

                    3 -> {
                        TextWithUnderlineStrikeThroughAndBold()
                    }

                    else -> {

                    }
                }
            }
        )
    }
}

@Composable
fun NormalTextExample(){
    // 일반 텍스트
    Text("Just Text")
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun CursiveTextExample() {
    // 필기체 텍스트
    Text(
        text = "Text with cursive font",
        style = TextStyle(fontFamily = FontFamily.Cursive)
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun TextWithLineThroughExample() {
    // 밑줄 텍스트
    Text(
        text = "Text with underline",
        style = TextStyle(textDecoration = TextDecoration.Underline)
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun TextWithUnderlineStrikeThroughAndBold() {
    // 밑줄, 취소, 볼드체
    Text(
        text = "Text with underline, linethrough and bold",
        style = TextStyle(
            textDecoration = TextDecoration.combine(
                listOf(
                    TextDecoration.Underline,
                    TextDecoration.LineThrough
                )
            ),
            fontWeight = FontWeight.Bold
        )
    )
}

@Preview
@Composable
fun PreviewTextDemo() {
    TextDemo()
}




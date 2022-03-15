package com.ys.composeplayground.ui.material

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.extensions.toast

/**
 * Button
 *
 * 버튼에는 onClick 기능이 있습니다. Text-Composable 또는 다른 Composable을 Button의 자식 요소로 추가할 수 있습니다.
 */
@Composable
fun ButtonExample(
    onClick: (String) -> Unit = {}
) {
    val text = "Button"

    Button(
        onClick = { onClick(text) },
        colors = ButtonDefaults
            .textButtonColors(
                backgroundColor = Color.Red
            )
    ) {
        Text(text)
    }
}

/**
 * OutlinedButton
 *
 * 라인으로만 되어있는 버튼
 */
@Composable
fun OutlinedButtonExample(
    onClick: (String) -> Unit = {}
) {
    val text = "I'm an outlined Button"

    OutlinedButton(onClick = { onClick(text) }) {
        Text(text)
    }
}

/**
 * TextButton
 *
 * 배경이 없는 글자만 있는 버튼
 */
@Composable
fun TextButtonExample(
    onClick: (String) -> Unit = {}
) {
    val text = "I'm a Text Button"

    TextButton(onClick = { onClick(text) }) {
        Text(text)
    }
}

@Composable
fun SpaceDefault() {
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun ButtonDemos() {

    // Composable 안에서 context 를 가져오는 방법
    val context = LocalContext.current
    val onClick: (String) -> Unit = { context.toast(it) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            ButtonExample(onClick)
            SpaceDefault()
            OutlinedButtonExample(onClick)
            SpaceDefault()
            TextButtonExample(onClick)
        }
    }
}

@Preview
@Composable
fun PreviewButtonDemos() {
    ButtonDemos()
}
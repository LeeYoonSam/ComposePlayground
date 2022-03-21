package com.ys.composeplayground.ui.keyboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.R
import kotlin.math.pow

/**
 * Showing and hiding the soft keyboard
 *
 * 사용자가 숫자를 입력하고 계산 버튼이나 소프트 키보드의 특수 완료 키를 누른 후 제곱을 계산할 수 있도록 합니다.
 * 데이터가 입력된 후 전체 사용자 인터페이스를 다시 표시하는 것이 바람직할 수 있습니다.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShowingHidingKeyboardDemo() {
    val localSoftwareKeyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val callback = {
        result = try {
            val num = text.toFloat()
            num.pow(2.0f).toString()
        } catch (e: NumberFormatException) {
            ""
        }
        // callback 실행 후 키보드 숨김
        localSoftwareKeyboardController?.hide()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            TextField(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .alignByBaseline(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        callback()
                    }
                ),
                value = text,
                onValueChange = {
                    text = it
                }
            )
            Button(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .alignByBaseline(),
                onClick = { callback() }
            ) {
                Text(stringResource(id = R.string.calc))
            }
        }
        Text(
            text = result,
            style = MaterialTheme.typography.h4
        )
    }
}

@Preview
@Composable
fun PreviewShowingHidingKeyboard() {
    ShowingHidingKeyboardDemo()
}
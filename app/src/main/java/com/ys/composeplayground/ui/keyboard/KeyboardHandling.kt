package com.ys.composeplayground.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.R

/**
 * manifest 에서 windowSoftInputMode 모드를 추가해서 화면 크기에 맞춰서 resize 되도록 처리
 * android:windowSoftInputMode="adjustResize"
 */
@Composable
fun KeyboardHandlingDemo1() {

    var text by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .background(color = MaterialTheme.colors.primary)
                .weight(1.0f),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h5
            )
        }

        TextField(
            modifier = Modifier.padding(bottom = 16.dp),
            value = text,
            onValueChange = {
                text = it
            }
        )
    }
}

@Preview
@Composable
fun PreviewKeyboardHandlingDemo1() {
    KeyboardHandlingDemo1()
}
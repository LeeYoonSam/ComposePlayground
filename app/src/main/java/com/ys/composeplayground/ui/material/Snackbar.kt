package com.ys.composeplayground.ui.material

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Snackbar
 *
 * 스낵바는 화면 하단에 앱 프로세스에 대한 간단한 메시지를 제공합니다.
 */
@Composable
fun SnackbarDemo() {
    Column {
        val (snackbarVisibleState, setSnackBarState) = remember { mutableStateOf(false) }
        
        Button(onClick = { setSnackBarState(!snackbarVisibleState) }) {
            if (snackbarVisibleState) {
                Text("Hide Snackbar")
            } else {
                Text("Show Snackbar")
            }
        }

        if (snackbarVisibleState) {
            Snackbar(
                action = {
                    Button(onClick = {}) {
                        Text("MyAction")
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Column {
                    Text("This is a snackbar!")
                    Text("The second line")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSnackbar() {
    SnackbarDemo()
}

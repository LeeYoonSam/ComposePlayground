package com.ys.composeplayground.ui.material

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

// AlertDialog 는 긴급한 정보, 세부 정보 또는 작업의 사용자 대화 상자입니다.

@Composable
fun AlertDialogSample() {
    MaterialTheme {
        Column {
            val openDialog = remember { mutableStateOf(false) }

            Button(onClick = {
                openDialog.value = true
            }) {
                Text("Click me")
            }

            if (openDialog.value) {

                AlertDialog(onDismissRequest = {
                    // 사용자가 대화 상자 밖이나 뒤로 버튼을 클릭하면 대화 상자를 닫습니다.
                    // 해당 기능을 비활성화하려면 빈 onCloseRequest를 사용하기만 하면 됩니다.
                    openDialog.value = false
                },
                    title = {
                        Text(text = "Dialog Title")
                    },
                    text = {
                        Text(text = "Here is a text")
                    },
                    confirmButton = {
                        Button(
                            onClick = { openDialog.value = false }
                        ) {
                            Text(text = "This is the confirm Button")
                        }
                    }, dismissButton =  {
                        Button(

                            onClick = {
                                openDialog.value = false
                            }) {
                            Text("This is the dismiss Button")
                        }
                    })
            }
        }
    }
}

@Preview
@Composable
fun PreviewAlertDialogSample() {
    AlertDialogSample()
}
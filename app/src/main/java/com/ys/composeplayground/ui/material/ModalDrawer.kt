package com.ys.composeplayground.ui.material

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

/**
 * ModalDrawer
 *
 * ModalDrawer를 사용하면 탐색 창을 만들 수 있습니다.
 */

@Composable
fun ModalDrawerDemo() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column {
                Text("Text in Drawer")
                Button(onClick = {
                    scope.launch {
                        drawerState.close()
                    }
                }) {
                    Text("Close Drawer")
                }
            }
        },
        content = {
            Column {
                Text("Text in Bodycontext")
                Button(onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }) {
                    Text("Click to open")
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewModalDrawer() {
    ModalDrawerDemo()
}
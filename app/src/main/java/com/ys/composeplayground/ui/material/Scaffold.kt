package com.ys.composeplayground.ui.material

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

/**
 * Scaffold
 *
 * Scaffold는 기본 머티리얼 디자인 레이아웃 구조를 구현하는 레이아웃입니다. TopBar, BottomBar, FAB 또는 서랍과 같은 것을 추가할 수 있습니다.
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldDemo() {
    val materialBlue700 = Color(0xFF1976D2)

    /**
     * rememberScaffoldState
     *  - 기본 애니메이션 시계로 ScaffoldState를 만들고 기억
     *
     * rememberDrawerState
     *  - DrawerState를 만들고 기억
     */
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("TopAppBar") },
                backgroundColor = materialBlue700
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        if (scaffoldState.drawerState.isOpen) {
                            scaffoldState.drawerState.close()
                        } else {
                            scaffoldState.drawerState.open()
                        }
                    }
                }
            ) {
                Text("X")
            }
        },
        drawerContent = { Text("drawerContent") },
        content = { Text("BodyContent") },
        bottomBar = {
            BottomAppBar(backgroundColor = materialBlue700) {
                Text("BottomAppBar")
            }
        }
    )
}

@Preview
@Composable
fun PreviewScaffold() {
    ScaffoldDemo()
}
package com.ys.composeplayground.ui.material

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

/**
 * Scaffold
 *
 * Scaffold는 기본 머티리얼 디자인 레이아웃 구조를 구현하는 레이아웃입니다. TopBar, BottomBar, FAB 또는 서랍과 같은 것을 추가할 수 있습니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldDemo() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Scaffold는 Material3에서 TopAppBar, BottomAppBar, FloatingActionButton 등을 포함하는 레이아웃을 제공합니다.
    // Material3의 Scaffold는 DrawerState를 직접 받지 않고, ModalNavigationDrawer를 통해 Drawer를 구현합니다.
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Drawer content")
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TopAppBar") }
                )
            },
            floatingActionButtonPosition = FabPosition.End, // FloatingActionButton의 위치를 지정합니다.
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            if (drawerState.isOpen) {
                                drawerState.close()
                            } else {
                                drawerState.open()
                            }
                        }
                    }
                ) {
                    Text("X")
                }
            }, // FloatingActionButton을 정의합니다.
            content = { innerPadding ->
                Text(
                    "BodyContent",
                    modifier = Modifier.padding(innerPadding)
                )
            }, // Scaffold의 메인 콘텐츠를 정의합니다.
            bottomBar = {
                BottomAppBar {
                    Text("BottomAppBar")
                }
            }
        )
    }
}

@Preview
@Composable
fun PreviewScaffold() {
    ScaffoldDemo()
}
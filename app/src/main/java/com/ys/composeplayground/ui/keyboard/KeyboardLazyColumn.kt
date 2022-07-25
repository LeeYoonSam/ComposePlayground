package com.ys.composeplayground.ui.keyboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * LazyColumn 내부에 있는 TextField 의 커서가 IME 키보드 밑으로 내려가서 안보이는 이슈
 *
 * bringIntoViewRequester 를 이용해서 해결
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeyboardLazyColumnDemo() {
    val scaffoldState = rememberScaffoldState()
    var content by remember { mutableStateOf(TextFieldValue()) }

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.Center,
    ) {
        val lazyListState = rememberLazyListState()

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                BasicTextField(
                    value = content,
                    onValueChange = { content = it },
                    onTextLayout = { textLayoutResult ->
                        val cursorRect = textLayoutResult.getCursorRect(content.selection.end)
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView(cursorRect)
                        }
                    },
                    modifier = Modifier
                        .background(color = Color.Green)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .bringIntoViewRequester(bringIntoViewRequester)
                )
            }
        }
    }
}
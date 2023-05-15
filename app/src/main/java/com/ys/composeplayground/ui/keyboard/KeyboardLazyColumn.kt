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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.launch

/**
 * LazyColumn 내부에 있는 TextField 의 커서가 IME 키보드 밑으로 내려가서 안보이는 이슈
 *
 * bringIntoViewRequester 를 이용해서 해결
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun KeyboardLazyColumnDemo() {
    var textContent by remember { mutableStateOf(TextFieldValue()) }

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
    ) { content ->
        val lazyListState = rememberLazyListState()

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(content),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                BasicTextField(
                    value = textContent,
                    onValueChange = { textContent = it },
                    onTextLayout = { textLayoutResult ->
                        val cursorRect = textLayoutResult.getCursorRect(textContent.selection.end)
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
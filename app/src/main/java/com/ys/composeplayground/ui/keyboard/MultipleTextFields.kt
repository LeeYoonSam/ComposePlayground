package com.ys.composeplayground.ui.keyboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun KeyboardHandlingMultipleTextFieldsDemo() {
    val states = remember {
        mutableStateListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    }

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(states) { i, _ ->
            OutlinedTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = states[i],
                onValueChange = {
                    states[i] = it
                },
                label = {
                    Text("Text field ${i + 1}")
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)

                        coroutineScope.launch {
                            listState.animateScrollToItem(i)
                        }
                    }
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewKeyboardHandlingMultipleTextFields() {
    KeyboardHandlingMultipleTextFieldsDemo()
}
package com.ys.composeplayground.ui.keyboard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
package com.ys.composeplayground.ui.foundation

import android.util.Log
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.ui.snackbar.SnackbarAction
import com.ys.composeplayground.ui.snackbar.SnackbarController
import kotlinx.coroutines.launch

@Composable
fun LazyColumnDemo() {
    val list = listOf(
        "A", "B", "C", "D"
    ) + ((0..100).map { it.toString() })

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state =  listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = list, itemContent =  { item ->
            Log.d("COMPOSE", "This get rendered $item")

            when(item) {
                "A" -> {
                    Text(text = item, style = TextStyle(fontSize = 80.sp))
                }

                "B" -> {
                    Button(onClick = { SnackbarController.showMessage(
                        message = item,
                        action = SnackbarAction(
                            title = "Scroll Up",
                            onActionPress = {
                                coroutineScope.launch {
                                    listState.scrollToItem(listState.firstVisibleItemIndex + 10)
                                }
                            }
                        )
                    ) }) {
                        Text(text = item, style = TextStyle(fontSize = 80.sp))
                    }
                }

                "C" -> {
                    // Do Nothing
                }

                "D" -> {
                    Text(text = item, style = LocalTextStyle.current)
                }

                else -> {
                    Text(text = item, style = TextStyle(fontSize = 80.sp))
                }
            }
        })
    }
}

@Preview
@Composable
fun PreviewLazyColumnDemo() {
    LazyColumnDemo()
}
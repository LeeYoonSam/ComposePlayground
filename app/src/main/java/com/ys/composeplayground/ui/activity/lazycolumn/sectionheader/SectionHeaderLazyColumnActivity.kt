package com.ys.composeplayground.ui.activity.lazycolumn.sectionheader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class SectionHeaderLazyColumnActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val list = mutableListOf<ListItem>()
                (1..10).forEach { headerIndex ->
                    (1..(3..10).random()).forEach { itemIndex ->
                        list.add(
                            ListItem(
                                header = "Header #$headerIndex",
                                text = "Item #$itemIndex"
                            )
                        )
                    }
                }
                LazyColumnDemo(list)
            }
        }
    }

    @Composable
    fun LazyColumnDemo(items: List<ListItem>) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            val listState = rememberLazyListState()
            LazyColumn(state = listState) {
                itemsIndexed(items = items) { index, item ->
                    if ((index == 0) ||
                        items[index - 1].header != item.header) {
                        Header(text = item.header)
                    }
                    Item(text = item.text)
                }
            }

            val currentHeader by remember {
                derivedStateOf {
                    items[listState.firstVisibleItemIndex].header
                }
            }

            var lastHeader by remember {
                mutableStateOf(currentHeader)
            }

            val headerVisible by remember(
                currentHeader, lastHeader
            ) {
                mutableStateOf(lastHeader != currentHeader)
            }

            AnimatedVisibility(visible = headerVisible) {
                Header(text = currentHeader)
                LaunchedEffect(key1 = currentHeader) {
                    delay(3000)
                    lastHeader = currentHeader
                }
            }
        }
    }

    @Composable
    fun Header(text: String) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun Item(text: String) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }

    data class ListItem(
        val header: String,
        val text: String,
    )
}

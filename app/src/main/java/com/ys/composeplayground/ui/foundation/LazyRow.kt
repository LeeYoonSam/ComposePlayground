package com.ys.composeplayground.ui.foundation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun LazyRowDemo() {
    val list = listOf(
        "A", "B", "C", "D"
    ) + ((0..100).map { it.toString() })
    
    LazyRow(modifier = Modifier.fillMaxHeight()) {
        items(items = list, itemContent = { item ->
            Log.d("COMPOSE", "This get rendered $item")

            when (item) {
                "A" -> {
                    Text(text = item, style = TextStyle(fontSize = 80.sp))
                }
                
                "B" -> {
                    Button(onClick = {}) {
                        Text(text = item, style = TextStyle(fontSize = 80.sp))
                    }
                }

                "C" -> {
                    // Nothing
                }

                "D" -> {
                    Text(text = item)
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
fun previewLazyRowDemo() {
    LazyRowDemo()
}
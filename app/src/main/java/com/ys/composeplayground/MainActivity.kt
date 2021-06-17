package com.ys.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ComposeList()
                }
            }
        }
    }
}

@Composable
fun ComposeList() {

    val sampleList = getComposeSampleList()

    LazyColumn(
        modifier = Modifier.fillMaxHeight()
    ) {
        items(
            items = sampleList,
            itemContent = { item ->
                Card(
                    elevation = 4.dp,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = item,
                        style = TextStyle(fontSize = 24.sp)
                    )
                }
            }
        )
    }
}

fun getComposeSampleList() = listOf(
    "Crossfade",
    "BaseTextField",
    "Canvas",
    "Image",
    "LazyColumn"
)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposePlaygroundTheme {
        ComposeList()
    }
}
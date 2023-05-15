package com.ys.composeplayground.ui.material

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.extensions.toast

@Composable
fun FloatingActionButtonDemo(onClick: (String) -> Unit) {
    FloatingActionButton(
        onClick = { onClick("Clicked FloatingActionButton!") }
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "FloatingActionButton"
        )
    }
}

@Preview
@Composable
fun PreviewFloatingActionButton() {
    FloatingActionButtonDemo({})
}

@Composable
fun ExtendedFloatingActionButtonDemo(onClick: (String) -> Unit) {
    val context = LocalContext.current

    /**
     * FloatingActionButton 베이스에 content Composable 에 Box, Row 를 활용해서 뷰를 구성
     */
    ExtendedFloatingActionButton(
        icon = { Icon(Icons.Filled.Favorite, "") },
        text = { Text("ExtendedFloatingActionButton") },
        onClick = { onClick("Clicked ExtendedFloatingActionButtonDemo!") },
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    )
}

@Preview
@Composable
fun PreviewExtendedFloatingActionButtonDemo() {
    ExtendedFloatingActionButtonDemo({})
}

@Composable
fun FloatingActionButtonDemos() {
    val context = LocalContext.current

    val onClick: (String) -> Unit = { message -> context.toast(message) }

    Column(modifier = Modifier.padding(16.dp)) {
        FloatingActionButtonDemo(onClick)
        Spacer(modifier = Modifier.height(20.dp))
        ExtendedFloatingActionButtonDemo(onClick)
    }
}

@Preview
@Composable
fun PreviewFloatingActionButtons() {
    FloatingActionButtonDemos()
}
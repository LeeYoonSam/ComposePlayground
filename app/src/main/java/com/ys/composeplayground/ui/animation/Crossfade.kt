package com.ys.composeplayground.ui.animation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class MyColors(val color: Color) {
    Red(Color.Red),
    Green(Color.Green),
    Blue(Color.Blue),
}

@Preview
@Composable
fun CrossfadeDemo() {
    var currentColor by remember { mutableStateOf(MyColors.Red) }

    Column {
        Row {
           MyColors.values().forEach { myColor ->
               Button(
                   onClick = { currentColor = myColor },
                   modifier = Modifier
                       .weight(1f, true)
                       .height(48.dp)
                       .background(myColor.color),
                   colors = ButtonDefaults.buttonColors(containerColor = myColor.color)
               ) {
                   Text(text = myColor.name)
               }
           }
        }
        
        Crossfade(targetState = currentColor, animationSpec = tween(3000),
            label = ""
        ) { selectedColor ->
            Box(modifier = Modifier.fillMaxSize().background(selectedColor.color))
        }
    }
}
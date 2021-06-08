package com.ys.composeplayground.ui.animation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
                   colors = ButtonDefaults.buttonColors(backgroundColor = myColor.color)
               ) {
                   Text(text = myColor.name)
               }
           }
        }
        
        Crossfade(targetState = currentColor, animationSpec = tween(3000)) { selectedColor ->
            Box(modifier = Modifier.fillMaxSize().background(selectedColor.color))
        }
    }
}
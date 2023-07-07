package com.ys.composeplayground.ui.stat

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayerCountScreen(player: Player) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        Text(text = player.name, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CountCell(
                count = player.rebounds.value,
                onIncrement = { player.rebounds.value++ },
                onDecrement = { player.rebounds.value-- }
            )

            CountCell(
                count = player.assists.value,
                onIncrement = { player.assists.value++ },
                onDecrement = { player.assists.value-- }
            )

            CountCell(
                count = player.steals.value,
                onIncrement = { player.steals.value++ },
                onDecrement = { player.steals.value-- }
            )

            CountCell(
                count = player.blocks.value,
                onIncrement = { player.blocks.value++ },
                onDecrement = { player.assists.value-- }
            )
        }
    }
}

@Composable
fun CountCell(
    modifier: Modifier = Modifier,
    count: Int,
    fontSize: TextUnit = 24.sp,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val offsetY = remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, dragAmount ->
                        offsetY.value += dragAmount
                        if (change.positionChange() != Offset.Zero) change.consume()
                    },
                    onDragEnd = {
                        if (offsetY.value > 0) {
                            onDecrement()
                        } else if (offsetY.value < 0) {
                            onIncrement()
                        }
                        offsetY.value = 0f
                    }
                )
            }
            .background(Color.LightGray)
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Text(
            text = count.toString(),
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            modifier = modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
fun CountCellPreview() {
    var countState by remember {
        mutableStateOf(5)
    }

    CountCell(
        count = countState,
        onIncrement = { countState++ },
        onDecrement = { countState-- }
    )
}

@Preview
@Composable
fun PlayerCountScreenPreview() {
    val player = Player("Albert")
    PlayerCountScreen(player)
}
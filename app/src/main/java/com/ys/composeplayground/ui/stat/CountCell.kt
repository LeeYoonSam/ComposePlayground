package com.ys.composeplayground.ui.stat

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun CountCell(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .draggable(
                state = DraggableState {  },
                orientation = Orientation.Vertical,
                onDragStarted = { /* Drag started callback */ },
                onDragStopped = { /* Drag stopped callback */ },
                startDragImmediately = true
            )
            .background(Color.LightGray)
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        val offsetY = remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                text = count.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { change, dragAmount ->
                            offsetY.value += dragAmount
                            if (change.positionChange() != Offset.Zero) change.consume()
                        },
                        onDragEnd = {
                            if (offsetY.value > 0) {
                                onIncrement()
                            } else if (offsetY.value < 0) {
                                onDecrement()
                            }
                            offsetY.value = 0f
                        }
                    )
                }
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
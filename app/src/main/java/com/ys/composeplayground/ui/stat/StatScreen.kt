package com.ys.composeplayground.ui.stat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatScreen(players: List<Player>) {
    LazyColumn {
        stickyHeader {
            StatHeader()
        }

        items(players) { player ->
            PlayerCountItem(player)
        }
    }
}

@Composable
fun StatHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "선수(번호)",
                style = TextStyle(fontSize = 20.sp)
            )
        }

        val resource = LocalContext.current.resources

        Row(
            modifier = Modifier.weight(3f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CountHeaderText(
                modifier = Modifier.weight(1f),
                headerText = resource.getString(R.string.rebound)
            )
            CountHeaderText(
                modifier = Modifier.weight(1f),
                headerText = resource.getString(R.string.assist)
            )
            CountHeaderText(
                modifier = Modifier.weight(1f),
                headerText = resource.getString(R.string.steal)
            )
            CountHeaderText(
                modifier = Modifier.weight(1f),
                headerText = resource.getString(R.string.block)
            )
        }
    }
}

@Composable
fun CountHeaderText(
    modifier: Modifier = Modifier,
    headerText: String
) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        text = headerText,
        style = TextStyle(fontSize = 24.sp)
    )
}

@Composable
fun PlayerCountItem(player: Player) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = player.name, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
            Text(text = player.backNumber, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
        }

        Row(
            modifier = Modifier.weight(3f),
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
fun PlayerCountItemPreview() {
    val player = Player("Albert", "22")
    PlayerCountItem(player)
}

@Preview(device = Devices.TABLET)
@Composable
fun StatScreenPreview() {
    val players = listOf(
        Player("YS", "22"),
        Player("DK", "30"),
        Player("YN", "5"),
        Player("KH", "21"),
        Player("DonK", "4"),
        Player("YS", "22"),
        Player("DK", "30"),
        Player("YN", "5"),
        Player("KH", "21"),
        Player("DonK", "4"),
        Player("YS", "22"),
        Player("DK", "30"),
        Player("YN", "5"),
        Player("KH", "21"),
        Player("DonK", "4"),
    )

    StatScreen(players)
}
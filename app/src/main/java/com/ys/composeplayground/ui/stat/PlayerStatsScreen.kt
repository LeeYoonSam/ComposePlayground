package com.ys.composeplayground.ui.stat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayerStatsScreen(
    player: Player,
    onReboundIncrement: () -> Unit = {},
    onReboundDecrement: () -> Unit = {},
    onAssistIncrement: () -> Unit = {},
    onAssistDecrement: () -> Unit = {},
    onStealIncrement: () -> Unit = {},
    onStealDecrement: () -> Unit = {},
    onBlockIncrement: () -> Unit = {},
    onBlockDecrement: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        Text(text = player.name, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(16.dp))

        StatItem(
            title = "Rebounds",
            value = player.rebounds,
            onIncrement = onReboundIncrement,
            onDecrement = onReboundDecrement
        )

        StatItem(
            title = "Assists",
            value = player.assists,
            onIncrement = onAssistIncrement,
            onDecrement = onAssistDecrement
        )

        StatItem(
            title = "Steals",
            value = player.steals,
            onIncrement = onStealIncrement,
            onDecrement = onStealDecrement
        )

        StatItem(
            title = "Blocks",
            value = player.blocks,
            onIncrement = onBlockIncrement,
            onDecrement = onBlockDecrement
        )
    }
}

@Composable
fun StatItem(
    title: String, value: Int, onIncrement: () -> Unit, onDecrement: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = TextStyle(fontSize = 18.sp))
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onDecrement) {
            Icon(Icons.Default.Remove, contentDescription = "Decrement")
        }
        Text(text = value.toString(), style = TextStyle(fontSize = 18.sp))
        IconButton(onClick = onIncrement) {
            Icon(Icons.Default.Add, contentDescription = "Increment")
        }
    }
}

@Preview
@Composable
fun PlayerStateScreenPreview() {
    val rebounds = remember {
        mutableStateOf(0)
    }

    val player = Player(
        name = "Albert",
        rebounds = rebounds.value,
        assists = 0,
        steals = 0,
        blocks = 0
    )

    PlayerStatsScreen(
        player = player,
        onReboundIncrement = {
            rebounds.value = rebounds.value + 1
        },
        onReboundDecrement = {
            rebounds.value = rebounds.value - 1
        }
    )
}
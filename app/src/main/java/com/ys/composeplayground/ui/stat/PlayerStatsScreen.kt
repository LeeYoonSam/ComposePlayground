package com.ys.composeplayground.ui.stat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayerStatsScreen(
    player: Player
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
            value = player.rebounds.value,
            onIncrement = { player.rebounds.value++ },
            onDecrement = { player.rebounds.value-- }
        )

        StatItem(
            title = "Assists",
            value = player.assists.value,
            onIncrement = { player.assists.value++ },
            onDecrement = { player.assists.value-- }
        )

        StatItem(
            title = "Steals",
            value = player.steals.value,
            onIncrement = { player.steals.value++ },
            onDecrement = { player.steals.value-- }
        )

        StatItem(
            title = "Blocks",
            value = player.blocks.value,
            onIncrement = { player.blocks.value++ },
            onDecrement = { player.blocks.value-- }
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
    val player = Player("Albert", "22")
    PlayerStatsScreen(player)
}
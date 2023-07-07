package com.ys.composeplayground.ui.stat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Player(
    val name: String,
    var rebounds: MutableState<Int> = mutableStateOf(0),
    var assists: MutableState<Int> = mutableStateOf(0),
    var steals: MutableState<Int> = mutableStateOf(0),
    var blocks: MutableState<Int> = mutableStateOf(0)
)
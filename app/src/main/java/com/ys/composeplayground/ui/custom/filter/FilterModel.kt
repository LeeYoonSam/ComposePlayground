package com.ys.composeplayground.ui.custom.filter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class FilterModel(
    val type: FilterType,
    val title: String,
    val options: List<String>,
    val selectedOptions: MutableState<Set<String>> = mutableStateOf(emptySet())
)
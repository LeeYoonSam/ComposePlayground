package com.ys.composeplayground.ui.grid.chip

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class ChipItemViewModel(
    val chipId: Int,
    val chipTitle: String,
    val chipAction: (chipId: Int) -> Unit
) {
    var isSelected by mutableStateOf(false)

    fun clickChip() {
        chipAction(chipId)
    }
}

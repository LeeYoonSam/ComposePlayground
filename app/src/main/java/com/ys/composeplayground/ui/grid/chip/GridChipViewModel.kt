package com.ys.composeplayground.ui.grid.chip

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class GridChipViewModel : ViewModel() {
    private val _chipItems = mutableStateListOf<ChipItemViewModel>()
    val chipItems: List<ChipItemViewModel> = _chipItems

    private val sampleChipItems = listOf(
        ChipItemViewModel(
            chipId = 1,
            chipTitle = "키워드 1",
            chipAction = this::setSelectedChipId
        ),
        ChipItemViewModel(
            chipId = 2,
            chipTitle = "키워드 2",
            chipAction = this::setSelectedChipId
        ),
        ChipItemViewModel(
            chipId = 3,
            chipTitle = "키워드 3",
            chipAction = this::setSelectedChipId
        ),
        ChipItemViewModel(
            chipId = 4,
            chipTitle = "키워드 4",
            chipAction = this::setSelectedChipId
        ),
        ChipItemViewModel(
            chipId = 5,
            chipTitle = "키워드 5",
            chipAction = this::setSelectedChipId
        ),
        ChipItemViewModel(
            chipId = 6,
            chipTitle = "키워드 6",
            chipAction = this::setSelectedChipId
        ),
    )

    init {
        _chipItems.clear()
        _chipItems.addAll(sampleChipItems)
        _chipItems.firstOrNull()?.let {
            setSelectedChipId(it.chipId)
        }
    }

    fun setSelectedChipId(chipId: Int) {
        _chipItems.forEach {
            it.isSelected = it.chipId == chipId
        }
    }
}
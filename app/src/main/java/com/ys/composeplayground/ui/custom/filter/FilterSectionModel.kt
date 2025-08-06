package com.ys.composeplayground.ui.custom.filter

data class FilterSectionModel(
    val title: String,
    val filters: List<FilterModel>,
    var isSectionOpen: Boolean = true,
)
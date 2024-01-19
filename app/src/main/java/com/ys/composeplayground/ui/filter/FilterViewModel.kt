package com.ys.composeplayground.ui.filter

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {

    private val _filterSections = mutableStateListOf<FilterSectionModel>()
    val filterSections: List<FilterSectionModel> get() = _filterSections

    init {
        generateFilters()
    }

    private fun generateFilters() {
        val toggleFilter = FilterModel(
            type = FilterType.TOGGLE,
            title = "Toggle Filter",
            options = listOf("Enabled", "Disabled")
        )

        val radioFilter = FilterModel(
            type = FilterType.RADIO,
            title = "Radio Filter",
            options = listOf("Option 1", "Option 2", "Option 3")
        )

        val singleSelectFilter = FilterModel(
            type = FilterType.SINGLE_SELECT,
            title = "Single Select Filter",
            options = listOf("Option A", "Option B", "Option C")
        )

        val multiSelectFilter = FilterModel(
            type = FilterType.MULTI_SELECT,
            title = "Multi Select Filter",
            options = listOf("Option X", "Option Y", "Option Z")
        )

        val filterSection1 = FilterSectionModel(
            title = "Section 1",
            filters = listOf(toggleFilter, radioFilter)
        )

        val filterSection2 = FilterSectionModel(
            title = "Section 2",
            filters = listOf(singleSelectFilter, multiSelectFilter)
        )

        addFilterSection(filterSection1)
        addFilterSection(filterSection2)
    }

    private fun addFilterSection(filterSection: FilterSectionModel) {
        _filterSections.add(filterSection)
    }

    fun applyFilters(): String {
        val result = StringBuilder()

        for (section in filterSections) {
            if (section.isSectionOpen) {
                result.append("${section.title}:\n")
                for (filter in section.filters) {
                    result.append("  ${filter.title}: ")

                    when (filter.type) {
                        FilterType.TOGGLE -> {
                            result.append(if (filter.selectedOptions.value.contains("Enabled")) "Enabled" else "Disabled")
                        }
                        FilterType.RADIO,
                        FilterType.SINGLE_SELECT -> {
                            result.append(filter.selectedOptions.value.firstOrNull() ?: "None")
                        }
                        FilterType.MULTI_SELECT -> {
                            result.append(filter.selectedOptions.value.joinToString())
                        }
                    }

                    result.append("\n")
                }
            }
        }

        return result.toString()
    }

    fun removeFilterSection(filterSection: FilterSectionModel) {
        _filterSections.remove(filterSection)
    }
}

package com.ys.composeplayground.ui.custom.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ys.composeplayground.ui.snackbar.SnackbarController
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

@Composable
fun FilterScreen(filterViewModel: FilterViewModel = viewModel()) {
    val snackbar = SnackbarController.current

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Selected Filters:", fontWeight = FontWeight.Bold)

        // 각 필터의 선택된 옵션들을 표시
        filterViewModel.apply {
            val filterResult = applyFilters()
            Text(filterResult)
            snackbar.showMessage(filterResult)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 각 필터의 세션을 표시
        filterViewModel.filterSections.forEach { filterSection ->
            FilterSection(filterSection)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun FilterSection(filterSection: FilterSectionModel) {
    var isSectionOpen by remember {
        mutableStateOf(filterSection.isSectionOpen)
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = filterSection.title, fontWeight = FontWeight.Bold)
            IconButton(
                onClick = {
                    isSectionOpen = !isSectionOpen
                }
            ) {
                Icon(
                    imageVector = if (isSectionOpen) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Toggle Section"
                )
            }
        }

        if (isSectionOpen) {
            filterSection.filters.forEach { filter ->
                when (filter.type) {
                    FilterType.TOGGLE -> {
                        ToggleFilter(filter)
                    }
                    FilterType.RADIO,
                    FilterType.SINGLE_SELECT -> {
                        SingleSelectFilter(filter)
                    }
                    FilterType.MULTI_SELECT -> {
                        MultiSelectFilter(filter)
                    }
                }
            }
        }
    }
}

@Composable
fun ToggleFilter(filter: FilterModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = filter.title)
        Switch(
            checked = filter.selectedOptions.value.contains("Enabled"),
            onCheckedChange = {
                filter.selectedOptions.value =
                    if (it) setOf("Enabled") else setOf("Disabled")
            }
        )
    }
}

@Composable
fun SingleSelectFilter(filter: FilterModel) {
    Column {
        Text(text = filter.title)
        RadioGroup(
            options = filter.options,
            selectedOption = filter.selectedOptions.value.firstOrNull(),
            onOptionSelected = {
                filter.selectedOptions.value = setOf(it)
            }
        )
    }
}

@Composable
fun RadioGroup(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    val selectedOptionState by rememberUpdatedState(newValue = selectedOption)

    Column {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .selectable(
                        selected = (option == selectedOptionState),
                        onClick = {
                            onOptionSelected(option)
                        },
                    )
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = (option == selectedOptionState))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option)
            }
        }
    }
}

@Composable
fun RadioButton(selected: Boolean) {
    val modifier = Modifier
        .size(24.dp)
        .background(MaterialTheme.colorScheme.primary, CircleShape)
        .padding(2.dp)
        .shadow(2.dp, CircleShape)
        .clip(CircleShape)

    if (selected) {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            // Check mark icon or any indicator when selected
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.White
            )
        }
    } else {
        Box(modifier = modifier)
    }
}

@Composable
fun MultiSelectFilter(filter: FilterModel) {
    Column {
        Text(text = filter.title)
        CheckboxGroup(
            options = filter.options,
            selectedOptions = filter.selectedOptions.value.toSet(),
            onOptionsSelected = {
                filter.selectedOptions.value = it
            }
        )
    }
}

@Composable
fun CheckboxGroup(
    options: List<String>,
    selectedOptions: Set<String>,
    onOptionsSelected: (Set<String>) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        val updatedSet = if (selectedOptions.contains(option)) {
                            selectedOptions - option
                        } else {
                            selectedOptions + option
                        }
                        onOptionsSelected(updatedSet)
                    }
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedOptions.contains(option),
                    onCheckedChange = null // null to disable default behavior
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option)
            }
        }
    }
}


@Preview
@Composable
fun FilterScreenPreview() {
    ComposePlaygroundTheme {
        FilterScreen()
    }
}
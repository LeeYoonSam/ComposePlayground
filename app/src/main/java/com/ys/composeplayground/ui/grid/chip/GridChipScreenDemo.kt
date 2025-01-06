package com.ys.composeplayground.ui.grid.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ys.composeplayground.ui.snackbar.SnackbarController
import com.ys.composeplayground.ui.theme.CustomTypography

@Composable
fun GridChipScreenDemo(viewModel: GridChipViewModel = viewModel()) {
    val chipItems by remember { derivedStateOf { viewModel.chipItems } }
    RoundChipContainer(chipItems)
}

@Composable
fun RoundChipContainer(chipItemViewModels: List<ChipItemViewModel>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(chipItemViewModels) {
            RoundChip(it)
        }
    }
}

@Composable
fun RoundChip(chipItemViewModel: ChipItemViewModel) {
    Button(
        onClick = { chipItemViewModel.clickChip() },
        modifier = Modifier.height(36.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = if (chipItemViewModel.isSelected) Color.Blue else Color.White
        )
    ) {
        Text(
            chipItemViewModel.chipTitle,
            style = CustomTypography.body1BoldSmall.copy(color = if (chipItemViewModel.isSelected) Color.White else Color.Blue)
        )
    }
}

@Preview
@Composable
fun PreviewRoundChip() {
    val chipItemViewModel = ChipItemViewModel(
        chipId = 1,
        chipTitle = "키워드 1"
    ) { chipId ->
        SnackbarController.showMessage("칩 이동: $chipId")
    }

    chipItemViewModel.isSelected = true

    RoundChip(chipItemViewModel)
}

@Preview
@Composable
fun PreviewRoundChipContainer() {
    val chipItemViewModel = ChipItemViewModel(
        chipId = 1,
        chipTitle = "키워드 1"
    ) { chipId ->
        SnackbarController.showMessage("칩 이동: $chipId")
    }

    chipItemViewModel.isSelected = true

    val chipItemViewModel2 = ChipItemViewModel(
        chipId = 1,
        chipTitle = "키워드 2"
    ) { chipId ->
        SnackbarController.showMessage("칩 이동: $chipId")
    }

    chipItemViewModel2.isSelected = false

    val chips = listOf(chipItemViewModel, chipItemViewModel2)

    RoundChipContainer(chips)
}
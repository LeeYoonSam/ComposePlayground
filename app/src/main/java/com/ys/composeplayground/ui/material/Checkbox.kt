package com.ys.composeplayground.ui.material

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CheckboxDemo() {
    val checkedState = remember { mutableStateOf(true) }

    Checkbox(
        modifier = Modifier.padding(10.dp),
        checked = checkedState.value,
        onCheckedChange = { checkedState.value = it }
    )
}

@Preview
@Composable
fun PreviewCheckbox() {
    CheckboxDemo()
}
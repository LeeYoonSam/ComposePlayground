package com.ys.composeplayground.ui.sample.photoapp.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

@Composable
fun PhotosTab(groups: List<String>, selectedGroup: String, onSelected: (String) -> Unit) {
    val selectedIndex = groups.indexOf(selectedGroup)
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.surface,
        indicator = { positions ->
            TabIndicatorContainer(positions, groups.indexOf(selectedGroup)) {
                // circle indicator
                val color = MaterialTheme.colorScheme.primary
                Canvas(modifier = Modifier.size(4.dp)) {
                    drawCircle(color)
                }
            }
        },
        divider = {}
    ) {
        groups.forEachIndexed { index, group ->
            val color = animateColorAsState(
                targetValue = if (selectedGroup == group) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled),
                label = ""
            )
            Tab(
                selected = index == selectedIndex,
                text = { Text(text = group, color = color.value)},
                onClick = { onSelected(group) },
                selectedContentColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
private fun TabIndicatorContainer(
    tabPosition: List<TabPosition>,
    selectedIndex: Int,
    content: @Composable () -> Unit
) {
    val transition = updateTransition(targetState = selectedIndex, label = "")

    val offset = transition.animateDp(transitionSpec = {
        spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
    }, label = "") {
        val position = tabPosition[it]
        (position.left + position.right) / 2
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomStart)
            .offset { IntOffset(x = offset.value.roundToPx(), y = (-2).dp.roundToPx()) }
    ) {
        content()
    }
}

@Preview
@Composable
fun TabPreview() {
    ComposePlaygroundTheme {
        var selectedGroup by remember { mutableStateOf("b/w") }
        PhotosTab(
            groups = listOf("sports", "portrait", "b/w", "neon city"),
            selectedGroup = selectedGroup,
            onSelected = { selectedGroup = it }
        )
    }
}
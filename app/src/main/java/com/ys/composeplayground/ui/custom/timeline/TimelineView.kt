package com.ys.composeplayground.ui.custom.timeline

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.R

/**
 * Provides synchronized scrolling between past and future content
 */
@Composable
fun TimelineView(
    modifier: Modifier = Modifier,
    height: Dp = 120.dp,
    pastContent: Int = R.drawable.soundwave_first_default_0,
    futureContent: Int = R.drawable.soundwave_second_default_0
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthDp = configuration.screenWidthDp.dp
    val halfScreenWidthDp = screenWidthDp / 2
    val offsetDp = screenWidthDp / 12 // Same offset as the original implementation

    val leftScrollState = rememberScrollState()
    val rightScrollState = rememberScrollState()

    var isSynchronizing by remember { mutableStateOf(false) }

    // Synchronize scroll positions with offset
    LaunchedEffect(leftScrollState.value) {
        if (!isSynchronizing) {
            isSynchronizing = true
            try {
                val targetPosition = (leftScrollState.value - with(density) { offsetDp.roundToPx() }).coerceAtLeast(0)
                rightScrollState.scrollTo(targetPosition)
            } finally {
                isSynchronizing = false
            }
        }
    }

    LaunchedEffect(rightScrollState.value) {
        if (!isSynchronizing) {
            isSynchronizing = true
            try {
                val targetPosition = (rightScrollState.value - with(density) { offsetDp.roundToPx() }).coerceAtLeast(0)
                leftScrollState.scrollTo(targetPosition)
            } finally {
                isSynchronizing = false
            }
        }
    }

    // Initialize scroll positions
    LaunchedEffect(Unit) {
        isSynchronizing = true

        try {
            leftScrollState.scrollTo(with(density) { offsetDp.roundToPx() })
            rightScrollState.scrollTo(0)
        } finally {
            isSynchronizing = false
        }
    }

    Box(modifier = modifier.height(height)) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Left scroll area - past content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .horizontalScroll(leftScrollState)
            ) {
                Image(
                    painter = painterResource(pastContent),
                    contentDescription = "Past timeline content",
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = halfScreenWidthDp + 2.dp),
                    contentScale = ContentScale.FillHeight
                )
            }

            // Center divider
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(Color.Transparent)
            )

            // Right scroll area - future content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .horizontalScroll(rightScrollState)
            ) {
                Image(
                    painter = painterResource(futureContent),
                    contentDescription = "Future timeline content",
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = halfScreenWidthDp + 2.dp),
                    contentScale = ContentScale.FillHeight
                )
            }
        }
    }
}

/**
 * Extended Compose TimelineView with additional configuration options
 */
@Composable
fun TimelineViewAdvanced(
    modifier: Modifier = Modifier,
    height: Dp = 120.dp,
    pastContent: Int = R.drawable.soundwave_first_default_0,
    futureContent: Int = R.drawable.soundwave_second_default_0,
    offsetFraction: Float = 1f/12f, // Configurable offset as fraction of screen width
    dividerWidth: Dp = 2.dp,
    dividerColor: Color = Color.Transparent,
    paddingExtra: Dp = 2.dp
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // Screen width calculations
    val screenWidthDp = configuration.screenWidthDp.dp
    val halfScreenWidthDp = screenWidthDp / 2
    val offsetDp = screenWidthDp * offsetFraction

    // Scroll states
    val leftScrollState = rememberScrollState()
    val rightScrollState = rememberScrollState()

    // Synchronization
    var isSynchronizing by remember { mutableStateOf(false) }

    // Synchronize scroll positions
    LaunchedEffect(leftScrollState.value) {
        if (!isSynchronizing) {
            isSynchronizing = true
            try {
                val targetPosition = (leftScrollState.value - with(density) { offsetDp.roundToPx() }).coerceAtLeast(0)
                rightScrollState.scrollTo(targetPosition)
            } finally {
                isSynchronizing = false
            }
        }
    }

    LaunchedEffect(rightScrollState.value) {
        if (!isSynchronizing) {
            isSynchronizing = true
            try {
                val targetPosition = rightScrollState.value + with(density) { offsetDp.roundToPx() }
                leftScrollState.scrollTo(targetPosition)
            } finally {
                isSynchronizing = false
            }
        }
    }

    // Initialize positions
    LaunchedEffect(Unit) {
        isSynchronizing = true
        try {
            leftScrollState.scrollTo(with(density) { offsetDp.roundToPx() })
            rightScrollState.scrollTo(0)
        } finally {
            isSynchronizing = false
        }
    }

    Box(
        modifier = modifier.height(height)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Left scroll area - past content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .horizontalScroll(leftScrollState)
            ) {
                Image(
                    painter = painterResource(id = pastContent),
                    contentDescription = "Past timeline content",
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = halfScreenWidthDp + paddingExtra),
                    contentScale = ContentScale.FillHeight
                )
            }

            // Center divider
            Box(
                modifier = Modifier
                    .width(dividerWidth)
                    .fillMaxHeight()
                    .background(dividerColor)
            )

            // Right scroll area - future content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .horizontalScroll(rightScrollState)
            ) {
                Image(
                    painter = painterResource(id = futureContent),
                    contentDescription = "Future timeline content",
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = halfScreenWidthDp + paddingExtra),
                    contentScale = ContentScale.FillHeight
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewTimelineView() {
    TimelineView()
}

@Preview
@Composable
fun PreviewTimelineViewAdvanced() {
    TimelineViewAdvanced()
}
package com.ys.composeplayground.ui.lazycolumn

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlin.math.roundToInt

/**
 * https://proandroiddev.com/netflix-toolbar-scroll-effect-c7ab3e435fd6
 * https://github.com/BILLyTheLiTTle/LazyColumns
 *
 * 콘텐츠용 툴바를 투명하게 만들면서 동시에 가로 스크롤 보기용 툴바를 불투명하게 만들기
 *
 * Primary App Bar
 * - 최대 스크롤을 스크롤바 높이로 제한
 */

private val DEFAULT_TOOLBAR_HEIGHT = 48.dp

data class NetflixLazyScreenSettings(
    val appBarHeight: Dp = DEFAULT_TOOLBAR_HEIGHT
)

private var toolbarHeight: Dp = DEFAULT_TOOLBAR_HEIGHT

@Composable
fun NetflixLazyScreen(
    modifier: Modifier = Modifier,
    settings: NetflixLazyScreenSettings = NetflixLazyScreenSettings(),
    primaryAppBar: @Composable () -> Unit,
    secondaryAppBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    toolbarHeight = settings.appBarHeight

    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }

    val toolbarOffsetHeightPx = remember { mutableFloatStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                // delta 의미: 두 값 또는 상태간의 차이를 나타냄
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.floatValue + delta
                // coerceIn: 범위 내에 있으면 이 값을, 최소값보다 작으면 최소값을, 최대값보다 크면 최대값을 반환합니다.
                toolbarOffsetHeightPx.floatValue = newOffset.coerceIn(-toolbarHeightPx, 0f)

                return Offset.Zero
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        content()

        Box(
            modifier = Modifier
                .padding(top = toolbarHeight)
        ) {
            Box(
                modifier = Modifier
                    .alpha(0.8f)
                    .offset {
                        IntOffset(x = 0, y = toolbarOffsetHeightPx.floatValue.roundToInt())
                    }
            ) {
                secondaryAppBar()
            }
        }
        primaryAppBar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
) {
    TopAppBar(
        title = title,
        modifier = modifier
            .alpha(0.8f)
            .height(toolbarHeight),
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
        ),
    )
}

@Composable
fun SecondaryAppBar(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal = if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        flingBehavior = flingBehavior,
        content = content,
    )
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPaddingStart: Dp = 0.dp,
    contentPaddingEnd: Dp = 0.dp,
    contentPaddingBottom: Dp = 0.dp,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = state,
        contentPadding = PaddingValues(
            top = toolbarHeight + toolbarHeight,
            start = contentPaddingStart,
            end = contentPaddingEnd,
            bottom = contentPaddingBottom
        ),
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content
    )
}

@Composable
fun NetflixToolbarScreen() {
    NetflixLazyScreen(
        primaryAppBar = {
            PrimaryAppBar(title = { Text(text = "Primary App Bar") })
        },
        secondaryAppBar = {
            SecondaryAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                items(5) {
                    Button(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 5.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(
                            fontSize = 14.sp,
                            text = "Button ${it + 1}"
                        )
                    }
                }
            }
        }
    ) {
        MainContent {
            items(30) {
                Button(onClick = { /*TODO*/ }) {
                    Text(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(5.dp),
                        text = "Vertical button ${it + 1}"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPrimaryAppBar() {
    ComposePlaygroundTheme {
        PrimaryAppBar(title = { Text(text = "Title") })
    }
}

@Preview
@Composable
fun PreviewSecondaryAppBar() {
    ComposePlaygroundTheme {
        SecondaryAppBar {
            items(5) {
                Button(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(horizontal = 5.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Text(
                        fontSize = 14.sp,
                        text = "Button ${it + 1}"
                    )
                }
            }
        }
    }
}
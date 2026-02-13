package com.ys.composeplayground.ui.commerce

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme
import com.ys.composeplayground.ui.theme.Grey500
import com.ys.composeplayground.ui.theme.Grey900

/**
 * 세일 탭 바
 * - ScrollableTabRow 사용
 * - 선택된 탭: Grey900 + Bold
 * - 미선택 탭: Grey500
 * - 인디케이터: SaleRed, 2dp
 */
@Composable
fun SaleTabBar(
    tabs: List<SaleTab>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier
            .height(48.dp)
            .background(Color.White),
        containerColor = Color.White,
        contentColor = Grey900,
        edgePadding = 16.dp,
        indicator = { tabPositions ->
            if (selectedIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    height = 2.dp,
                    color = SaleRed
                )
            }
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = tab.title,
                        fontSize = 15.sp,
                        fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedIndex == index) Grey900 else Grey500
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewSaleTabBar() {
    ComposePlaygroundTheme {
        SaleTabBar(
            tabs = saleTabs,
            selectedIndex = 2,
            onTabSelected = {}
        )
    }
}

package com.ys.composeplayground.ui.commerce

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme
import com.ys.composeplayground.ui.theme.Grey666
import com.ys.composeplayground.ui.theme.Grey900
import java.text.NumberFormat
import java.util.Locale

/**
 * 세일 탭 메인 화면
 * - NestedScrollConnection으로 스크롤 오프셋 추적
 * - collapseProgress (0~1)로 카테고리 아이콘 확장/축소 애니메이션
 * - LazyVerticalGrid 2열 상품 그리드
 */
@Composable
fun SaleTabScreen() {
    // 최대 축소 오프셋 (44dp = 92dp - 48dp, 카테고리 높이 변화량)
    val maxCollapseOffsetPx = with(LocalDensity.current) { 44.dp.toPx() }
    val scrollOffset = remember { mutableFloatStateOf(0f) }

    // collapseProgress: 0f (확장) ~ 1f (축소)
    val collapseProgress by remember {
        derivedStateOf {
            (scrollOffset.floatValue / maxCollapseOffsetPx).coerceIn(0f, 1f)
        }
    }

    // NestedScrollConnection: 스크롤 이벤트를 가로채서 오프셋 계산
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = scrollOffset.floatValue - delta
                scrollOffset.floatValue = newOffset.coerceIn(0f, maxCollapseOffsetPx)
                return Offset.Zero
            }
        }
    }

    // 상태 관리
    var selectedTabIndex by remember { mutableIntStateOf(2) } // 기본 "세일" 탭
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var selectedFilterChipIds by remember { mutableStateOf(setOf(1)) } // 기본 "필터" 선택

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .nestedScroll(nestedScrollConnection)
    ) {
        // 탭 바
        SaleTabBar(
            tabs = saleTabs,
            selectedIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        // 카테고리 아이콘 행 (확장/축소 애니메이션)
        CategoryIconRow(
            collapseProgress = collapseProgress,
            categories = saleCategories,
            selectedIndex = selectedCategoryIndex,
            onCategorySelected = { selectedCategoryIndex = it }
        )

        // 필터 칩 행
        FilterChipRow(
            chips = saleFilterChips,
            selectedChipIds = selectedFilterChipIds,
            onChipToggle = { chipId ->
                selectedFilterChipIds = if (chipId in selectedFilterChipIds) {
                    selectedFilterChipIds - chipId
                } else {
                    selectedFilterChipIds + chipId
                }
            }
        )

        // 상품 개수 + 정렬
        ProductCountAndSortRow(
            productCount = saleProducts.size
        )

        // 상품 그리드
        ProductGrid(
            products = saleProducts,
            onProductClick = { product ->
                // TODO: 상품 상세 화면 이동
            },
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 상품 개수 + 정렬 행
 */
@Composable
private fun ProductCountAndSortRow(
    productCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "총 ${NumberFormat.getNumberInstance(Locale.KOREA).format(productCount)}개",
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Grey666
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "추천순",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Grey900
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "정렬",
                tint = Grey666,
                modifier = androidx.compose.ui.Modifier.padding(0.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSaleTabScreen() {
    ComposePlaygroundTheme {
        SaleTabScreen()
    }
}

package com.ys.composeplayground.ui.commerce

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme
import com.ys.composeplayground.ui.theme.Grey900
import kotlin.math.roundToInt

/**
 * 애니메이션 상수
 */
private object CategoryAnimationSpec {
    val ExpandedIconSize = 56.dp
    val CollapsedIconSize = 24.dp
    val ExpandedRowHeight = 92.dp
    val CollapsedRowHeight = 48.dp
    val ExpandedInnerIconSize = 24.dp
    val CollapsedInnerIconSize = 16.dp
    val IconTextSpacing = 8.dp
    val ChipPaddingHorizontal = 12.dp
    val ChipPaddingVertical = 6.dp
    val ChipCornerRadius = 20.dp
}

/**
 * 카테고리 아이콘 행
 * - collapseProgress: 0f (확장) ~ 1f (축소)
 * - 확장: 원형 아이콘 56dp + 하단 텍스트, 높이 ~92dp
 * - 축소: 칩 형태, 좌측 아이콘 24dp + 우측 텍스트, 높이 ~48dp
 * - Custom Layout으로 아이콘/텍스트 위치를 부드럽게 보간
 */
@Composable
fun CategoryIconRow(
    collapseProgress: Float,
    categories: List<CategoryItem>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val rowHeight = lerp(
        CategoryAnimationSpec.ExpandedRowHeight,
        CategoryAnimationSpec.CollapsedRowHeight,
        collapseProgress
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(rowHeight)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                lerp(12.dp, 8.dp, collapseProgress)
            )
        ) {
            itemsIndexed(
                items = categories,
                key = { _, item -> item.id }
            ) { index, category ->
                MorphingCategoryItem(
                    category = category,
                    isSelected = selectedIndex == index,
                    collapseProgress = collapseProgress,
                    onClick = { onCategorySelected(index) }
                )
            }
        }
    }
}

/**
 * Custom Layout 기반 카테고리 아이템
 * - 하나의 아이콘과 하나의 텍스트만 렌더링
 * - collapseProgress에 따라 위치(x, y)와 크기를 보간
 * - 확장: 세로 배치 (아이콘 상단 중앙, 텍스트 하단 중앙)
 * - 축소: 가로 배치 (아이콘 좌측, 텍스트 우측) + 칩 배경
 */
@Composable
private fun MorphingCategoryItem(
    category: CategoryItem,
    isSelected: Boolean,
    collapseProgress: Float,
    onClick: () -> Unit
) {
    val density = LocalDensity.current

    val iconSize = lerp(
        CategoryAnimationSpec.ExpandedIconSize,
        CategoryAnimationSpec.CollapsedIconSize,
        collapseProgress
    )
    val innerIconSize = lerp(
        CategoryAnimationSpec.ExpandedInnerIconSize,
        CategoryAnimationSpec.CollapsedInnerIconSize,
        collapseProgress
    )

    val spacingPx = with(density) { CategoryAnimationSpec.IconTextSpacing.roundToPx() }
    val chipPaddingHPx = with(density) { CategoryAnimationSpec.ChipPaddingHorizontal.roundToPx() }
    val chipPaddingVPx = with(density) { CategoryAnimationSpec.ChipPaddingVertical.roundToPx() }

    Layout(
        content = {
            // [0] 칩 배경 (축소 시에만 보이도록 alpha 적용)
            Box(
                modifier = Modifier
                    .alpha(collapseProgress)
                    .background(Color.White, RoundedCornerShape(CategoryAnimationSpec.ChipCornerRadius))
                    .border(
                        width = 1.dp,
                        color = if (isSelected) SaleRed else SaleBorder,
                        shape = RoundedCornerShape(CategoryAnimationSpec.ChipCornerRadius)
                    )
            )

            // [1] 아이콘 원형 - 하나만 존재, 크기는 lerp
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .background(category.iconBackgroundColor)
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) SaleRed else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = category.iconTintColor,
                    modifier = Modifier.size(innerIconSize)
                )
            }

            // [2] 텍스트 - 하나만 존재
            Text(
                text = category.name,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Grey900 else SaleGrey,
                maxLines = 1
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    ) { measurables: List<Measurable>, constraints: Constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val iconPlaceable: Placeable = measurables[1].measure(looseConstraints)
        val textPlaceable: Placeable = measurables[2].measure(looseConstraints)

        // === 확장 상태 (세로 배치): 아이콘 위, 텍스트 아래 ===
        val expandedWidth = maxOf(iconPlaceable.width, textPlaceable.width)
        val expandedHeight = iconPlaceable.height + spacingPx + textPlaceable.height

        // === 축소 상태 (가로 배치 칩): [패딩][아이콘][간격][텍스트][패딩] ===
        val collapsedContentWidth = iconPlaceable.width + spacingPx + textPlaceable.width
        val collapsedWidth = chipPaddingHPx * 2 + collapsedContentWidth
        val collapsedContentHeight = maxOf(iconPlaceable.height, textPlaceable.height)
        val collapsedHeight = chipPaddingVPx * 2 + collapsedContentHeight

        // === 현재 치수 (보간) ===
        val currentWidth = lerpInt(expandedWidth, collapsedWidth, collapseProgress)
        val currentHeight = lerpInt(expandedHeight, collapsedHeight, collapseProgress)

        // 칩 배경 측정 (현재 크기에 맞춤)
        val bgPlaceable: Placeable = measurables[0].measure(
            Constraints.fixed(currentWidth, currentHeight)
        )

        layout(currentWidth, currentHeight) {
            // 칩 배경 배치
            bgPlaceable.place(0, 0)

            // --- 아이콘 위치 보간 ---
            // 확장: 상단 중앙
            val expIconX = (currentWidth - iconPlaceable.width) / 2
            val expIconY = 0
            // 축소: 좌측 패딩, 수직 중앙
            val colIconX = chipPaddingHPx
            val colIconY = (currentHeight - iconPlaceable.height) / 2

            iconPlaceable.place(
                x = lerpInt(expIconX, colIconX, collapseProgress),
                y = lerpInt(expIconY, colIconY, collapseProgress)
            )

            // --- 텍스트 위치 보간 ---
            // 확장: 아이콘 아래 중앙
            val expTextX = (currentWidth - textPlaceable.width) / 2
            val expTextY = iconPlaceable.height + spacingPx
            // 축소: 아이콘 오른쪽, 수직 중앙
            val colTextX = chipPaddingHPx + iconPlaceable.width + spacingPx
            val colTextY = (currentHeight - textPlaceable.height) / 2

            textPlaceable.place(
                x = lerpInt(expTextX, colTextX, collapseProgress),
                y = lerpInt(expTextY, colTextY, collapseProgress)
            )
        }
    }
}

/**
 * Int 보간 유틸리티
 */
private fun lerpInt(start: Int, stop: Int, fraction: Float): Int =
    (start + (stop - start) * fraction).roundToInt()

// === Preview ===

@Preview(showBackground = true)
@Composable
fun PreviewCategoryIconRowExpanded() {
    ComposePlaygroundTheme {
        var selectedIndex by remember { mutableIntStateOf(0) }
        CategoryIconRow(
            collapseProgress = 0f,
            categories = saleCategories,
            selectedIndex = selectedIndex,
            onCategorySelected = { selectedIndex = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryIconRowCollapsed() {
    ComposePlaygroundTheme {
        var selectedIndex by remember { mutableIntStateOf(0) }
        CategoryIconRow(
            collapseProgress = 1f,
            categories = saleCategories,
            selectedIndex = selectedIndex,
            onCategorySelected = { selectedIndex = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryIconRowMid() {
    ComposePlaygroundTheme {
        var selectedIndex by remember { mutableIntStateOf(0) }
        CategoryIconRow(
            collapseProgress = 0.5f,
            categories = saleCategories,
            selectedIndex = selectedIndex,
            onCategorySelected = { selectedIndex = it }
        )
    }
}

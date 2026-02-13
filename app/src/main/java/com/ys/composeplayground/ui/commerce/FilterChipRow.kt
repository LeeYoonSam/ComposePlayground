package com.ys.composeplayground.ui.commerce

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme
import com.ys.composeplayground.ui.theme.Grey900

/**
 * 필터 칩 행
 * - 칩 높이 32dp, RoundedCornerShape 16dp
 * - 토글 선택/해제
 * - leadingIcon: 좌측 아이콘 (필터 칩)
 * - hasDropdown: 우측 드롭다운 화살표
 */
@Composable
fun FilterChipRow(
    chips: List<SaleFilterChip>,
    selectedChipIds: Set<Int>,
    onChipToggle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = chips,
            key = { _, chip -> chip.id }
        ) { _, chip ->
            FilterChip(
                chip = chip,
                isSelected = chip.id in selectedChipIds,
                onClick = { onChipToggle(chip.id) }
            )
        }
    }
}

@Composable
private fun FilterChip(
    chip: SaleFilterChip,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) SaleRed.copy(alpha = 0.1f) else Color.White)
            .border(
                width = 1.dp,
                color = if (isSelected) SaleRed else SaleBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // 좌측 아이콘
        chip.leadingIcon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) SaleRed else SaleGrey,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        // 텍스트
        Text(
            text = chip.label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) SaleRed else Grey900
        )

        // 우측 드롭다운 화살표
        if (chip.hasDropdown) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = if (isSelected) SaleRed else SaleGrey,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewFilterChipRow() {
    ComposePlaygroundTheme {
        var selectedChipIds by remember { mutableStateOf(setOf(1)) }
        FilterChipRow(
            chips = saleFilterChips,
            selectedChipIds = selectedChipIds,
            onChipToggle = { chipId ->
                selectedChipIds = if (chipId in selectedChipIds) {
                    selectedChipIds - chipId
                } else {
                    selectedChipIds + chipId
                }
            }
        )
    }
}

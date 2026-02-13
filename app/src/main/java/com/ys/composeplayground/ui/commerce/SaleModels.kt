package com.ys.composeplayground.ui.commerce

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 세일 탭 데이터
 */
data class SaleTab(val title: String)

/**
 * 카테고리 아이템
 */
data class CategoryItem(
    val id: Int,
    val name: String,
    val icon: ImageVector,
    val iconBackgroundColor: Color,
    val iconTintColor: Color = Color(0xFF666666)
)

/**
 * 필터 칩 데이터
 */
data class SaleFilterChip(
    val id: Int,
    val label: String,
    val leadingIcon: ImageVector? = null,
    val hasDropdown: Boolean = false
)

/**
 * 상품 배지 타입
 */
enum class ProductBadge {
    NEW,
    COUPON,
    FREE_SHIPPING
}

/**
 * 세일 상품 데이터
 */
data class SaleProduct(
    val id: Int,
    val name: String,
    val sellerName: String,
    val originalPrice: Int,
    val discountPercent: Int,
    val salePrice: Int,
    val membershipPrice: Int?,
    val badges: List<ProductBadge>,
    val rating: Float,
    val reviewCount: Int,
    val reviewPreview: String?,
    val imageUrl: String,
    val imageColor: Color
)

package com.ys.composeplayground.ui.sample.product.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.ui.theme.Blue100
import com.ys.composeplayground.ui.theme.Blue500
import com.ys.composeplayground.ui.theme.Grey100
import com.ys.composeplayground.ui.theme.Grey666
import com.ys.composeplayground.ui.theme.Navy50
import com.ys.composeplayground.ui.theme.Navy500
import com.ys.composeplayground.ui.theme.Orange50
import com.ys.composeplayground.ui.theme.Orange500

data class BadgeModel(
    val badgeText: String,
    val badgeTextColor: Color = Grey666,
    val badgeTextSize: TextUnit = 10.sp,
    val backgroundColor: Color = Grey100,
    val iconVector: ImageVector? = null
)

val badges = listOf(
    BadgeModel(
        badgeText = "NEW",
        badgeTextColor = Grey666,
        backgroundColor = Grey100,
    ),
    BadgeModel(
        badgeText = "빠른배송",
        badgeTextColor = Navy500,
        backgroundColor = Navy50,
        iconVector = Icons.Filled.FastForward,
    ),
    BadgeModel(
        badgeText = "무배",
        badgeTextColor = Blue500,
        backgroundColor = Blue100,
    ),
    BadgeModel(
        badgeText = "Special",
        badgeTextColor = Orange500,
        backgroundColor = Orange50,
        iconVector = Icons.Outlined.AcUnit,
    ),
    BadgeModel(
        badgeText = "일반 뱃지",
        badgeTextColor = Color.Black,
        backgroundColor = Grey100,
    ),
)
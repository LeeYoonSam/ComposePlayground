package com.ys.composeplayground.ui.custom.orderstatus

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.ui.theme.Grey300
import com.ys.composeplayground.ui.theme.GreyD9
import com.ys.composeplayground.ui.theme.MyTypographyTokens.Caption2BoldMedium
import com.ys.composeplayground.ui.theme.Orange50
import com.ys.composeplayground.ui.theme.Orange500

/**
 * 주문 상태 배지
 *
 * 스펙:
 * - 크기: 52x20dp
 * - Text: 12sp, Bold
 * - 둥근 모서리: 10dp
 * - 활성화 색상: Orange500 (#EF7014)
 * - 비활성화 색상: Grey300 (#E0E0E0)
 * - Text 색상: 항상 White
 *
 * @param text 배지에 표시될 텍스트
 * @param isEnabled 배지가 활성화되었는지 여부
 * @param modifier Modifier
 * @param enableAnimation 애니메이션 활성화 여부 (기본값: true)
 */
@Composable
fun OrderStatusBadge(
    text: String,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true
) {
    // 배경 색상 (애니메이션 활성화 여부에 따라)
    val backgroundColor = if (isEnabled) Orange50 else Color.Transparent
    val textColor = if (isEnabled) Orange500 else GreyD9

    Box(
        modifier = modifier
            .width(52.dp)
            .height(20.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = Caption2BoldMedium,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 애니메이션과 함께 표시되는 주문 상태 배지
 *
 * @param text 배지에 표시될 텍스트
 * @param isVisible 배지가 보이는지 여부
 * @param isEnabled 배지가 활성화되었는지 여부
 * @param modifier Modifier
 */
@Composable
fun AnimatedOrderStatusBadge(
    text: String,
    isVisible: Boolean,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        ) + scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        ),
        exit = fadeOut(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        ) + scaleOut(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    ) {
        OrderStatusBadge(
            text = text,
            isEnabled = isEnabled,
            modifier = modifier
        )
    }
}

/**
 * 프리뷰: 활성화된 배지
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewOrderStatusBadgeEnabled() {
    OrderStatusBadge(
        text = "결제완료",
        isEnabled = true
    )
}

/**
 * 프리뷰: 비활성화된 배지
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewOrderStatusBadgeDisabled() {
    OrderStatusBadge(
        text = "배송완료",
        isEnabled = false
    )
}

/**
 * 프리뷰: 여러 상태의 배지들
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewOrderStatusBadgeVariants() {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.width(100.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        OrderStatusBadge(text = "결제완료", isEnabled = true)
        OrderStatusBadge(text = "작품준비", isEnabled = true)
        OrderStatusBadge(text = "배송중", isEnabled = false)
        OrderStatusBadge(text = "배송완료", isEnabled = false)
    }
}

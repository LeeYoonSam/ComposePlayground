package com.ys.composeplayground.ui.custom.orderstatus

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.theme.Grey300
import com.ys.composeplayground.ui.theme.Orange500

/**
 * 주문 프로그레스 노드 (원형 인디케이터)
 *
 * 스펙:
 * - 전체 크기: 12x12dp
 * - 배경: 흰색 원형 (프로그레스 바를 가리기 위함)
 * - 테두리: 2dp stroke
 * - 활성화: Orange500 (#EF7014)
 * - 비활성화: Grey300 (#E0E0E0)
 *
 * @param isEnabled 노드가 활성화되었는지 여부 (완료 또는 진행 중)
 * @param modifier Modifier
 * @param isActive 현재 진행 중인 노드인지 여부
 * @param enableAnimation 애니메이션 활성화 여부 (기본값: true)
 */
@Composable
fun OrderProgressNode(
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    enableAnimation: Boolean = true
) {
    // 색상 (애니메이션 활성화 여부에 따라)
    val nodeColor = if (enableAnimation) {
        val animatedColor by animateColorAsState(
            targetValue = if (isEnabled) Orange500 else Grey300,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, // overshoot 방지
                stiffness = Spring.StiffnessLow
            ),
            label = "nodeColorAnimation"
        )
        animatedColor
    } else {
        if (isEnabled) Orange500 else Grey300
    }

    Canvas(
        modifier = modifier.size(12.dp)
    ) {
        val canvasSize = size.minDimension
        val center = Offset(canvasSize / 2f, canvasSize / 2f)
        val radius = canvasSize / 2f

        // 1. 먼저 내부를 배경색(흰색)으로 채워서 프로그레스 바를 가림
        drawCircle(
            color = Color.White,
            radius = radius,
            center = center
        )

        // 2. 그 위에 테두리를 그림 (2dp stroke)
        drawCircle(
            color = nodeColor,
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

/**
 * 프리뷰: 활성화된 프로그레스 노드
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewOrderProgressNodeEnabled() {
    OrderProgressNode(
        isEnabled = true,
        isActive = true
    )
}

/**
 * 프리뷰: 비활성화된 프로그레스 노드
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewOrderProgressNodeDisabled() {
    OrderProgressNode(
        isEnabled = false,
        isActive = false
    )
}

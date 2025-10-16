package com.ys.composeplayground.ui.custom.orderstatus

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.theme.Grey300
import com.ys.composeplayground.ui.theme.Orange500

/**
 * 주문 프로그레스 바 (노드 간 연결선)
 *
 * 스펙:
 * - 높이: 2dp
 * - 완료 색상: Orange500 (#EF7014)
 * - 미완료 색상: Grey300 (#E0E0E0)
 * - 진행률에 따라 부드럽게 애니메이션
 *
 * @param progress 진행률 (0.0 ~ 1.0)
 * @param modifier Modifier
 * @param enableAnimation 애니메이션 활성화 여부 (기본값: true)
 */
@Composable
fun OrderProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true
) {
    // 진행률 (애니메이션 활성화 여부에 따라)
    val displayProgress = if (enableAnimation) {
        val animatedProgress by animateFloatAsState(
            targetValue = progress.coerceIn(0f, 1f),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, // overshoot 방지
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "progressAnimation"
        )
        animatedProgress
    } else {
        progress.coerceIn(0f, 1f)
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val strokeWidth = canvasHeight

        val startOffset = Offset(0f, canvasHeight / 2f)
        val endOffset = Offset(canvasWidth, canvasHeight / 2f)

        // 배경 라인 (미완료 부분)
        drawLine(
            color = Grey300,
            start = startOffset,
            end = endOffset,
            strokeWidth = strokeWidth
        )

        // 진행된 라인 (완료 부분)
        // Spring 애니메이션의 overshoot를 방지하기 위해 coerceIn 적용
        val clampedProgress = displayProgress.coerceIn(0f, 1f)
        if (clampedProgress > 0f) {
            val progressEndOffset = Offset(
                x = canvasWidth * clampedProgress,
                y = canvasHeight / 2f
            )

            drawLine(
                color = Orange500,
                start = startOffset,
                end = progressEndOffset,
                strokeWidth = strokeWidth
            )
        }
    }
}

/**
 * 프리뷰: 0% 진행
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewOrderProgressBar0() {
    OrderProgressBar(progress = 0f)
}

/**
 * 프리뷰: 50% 진행
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewOrderProgressBar50() {
    OrderProgressBar(progress = 0.5f)
}

/**
 * 프리뷰: 100% 진행
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewOrderProgressBar100() {
    OrderProgressBar(progress = 1f)
}

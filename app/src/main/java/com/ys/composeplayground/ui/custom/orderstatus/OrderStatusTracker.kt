package com.ys.composeplayground.ui.custom.orderstatus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 주문 상태 트래커 (메인 컴포넌트)
 *
 * 스펙:
 * - 전체 높이: 40dp (배지 20dp + 간격 8dp + 프로그레스 12dp)
 * - 배지 크기: 52x20dp
 * - 프로그레스 노드 크기: 12x12dp
 * - 프로그레스 바 높이: 2dp
 * - 색상: Orange500 (#EF7014), Grey300 (#E0E0E0)
 *
 * @param progressState 주문 프로그레스 상태
 * @param modifier Modifier
 * @param enableAnimation 애니메이션 활성화 여부 (기본값: true, 초기 진입시에만 사용)
 */
@Composable
fun OrderStatusTracker(
    progressState: OrderProgressState,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true
) {
    val steps = progressState.steps

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .semantics {
                stateDescription = "주문 상태: ${progressState.currentStep?.status?.displayName ?: "알 수 없음"}"
            }
    ) {
        // 배경 레이어: 프로그레스 바 (노드 중앙 높이에 위치)
        // 배지 중앙 = 26dp, 양쪽 패딩으로 노드 중앙에 맞춤
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(12.dp)
                .padding(horizontal = 26.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, step ->
                // 마지막 노드가 아니면 연결선 추가
                if (index < steps.size - 1) {
                    val nextStep = steps[index + 1]
                    val progress = when {
                        step.isCompleted && nextStep.isEnabled -> 1f
                        step.isCompleted -> 1f
                        else -> 0f
                    }

                    OrderProgressBar(
                        progress = progress,
                        enableAnimation = enableAnimation,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 전경 레이어: 배지와 노드
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            steps.forEach { step ->
                Column(
                    modifier = Modifier.width(52.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 배지
                    OrderStatusBadge(
                        text = step.status.displayName,
                        isEnabled = step.isEnabled,
                        enableAnimation = enableAnimation
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 프로그레스 노드
                    OrderProgressNode(
                        isEnabled = step.isEnabled,
                        isActive = step.isActive,
                        enableAnimation = enableAnimation
                    )
                }
            }
        }
    }
}

/**
 * 간편한 사용을 위한 OrderStatusTracker 오버로드
 *
 * @param currentStatus 현재 주문 상태
 * @param modifier Modifier
 * @param enableAnimation 애니메이션 활성화 여부 (기본값: true)
 */
@Composable
fun OrderStatusTracker(
    currentStatus: OrderStatus,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true
) {
    val progressState = remember(currentStatus) {
        OrderProgressState.fromCurrentStatus(currentStatus)
    }

    OrderStatusTracker(
        progressState = progressState,
        modifier = modifier,
        enableAnimation = enableAnimation
    )
}

/**
 * 프리뷰: 결제완료 상태
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewOrderStatusTrackerPaymentCompleted() {
    Box(modifier = Modifier.padding(16.dp)) {
        OrderStatusTracker(currentStatus = OrderStatus.PAYMENT_COMPLETED)
    }
}

/**
 * 프리뷰: 착품준비 상태
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewOrderStatusTrackerPreparing() {
    Box(modifier = Modifier.padding(16.dp)) {
        OrderStatusTracker(currentStatus = OrderStatus.PREPARING)
    }
}

/**
 * 프리뷰: 배송중 상태
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewOrderStatusTrackerShipping() {
    Box(modifier = Modifier.padding(16.dp)) {
        OrderStatusTracker(currentStatus = OrderStatus.SHIPPING)
    }
}

/**
 * 프리뷰: 배송완료 상태
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewOrderStatusTrackerDelivered() {
    Box(modifier = Modifier.padding(16.dp)) {
        OrderStatusTracker(currentStatus = OrderStatus.DELIVERED)
    }
}

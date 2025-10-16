package com.ys.composeplayground.ui.custom.orderstatus

import androidx.compose.runtime.Stable

/**
 * 주문 상태를 나타내는 Enum
 *
 * @property displayName 화면에 표시될 한글 이름
 */
enum class OrderStatus(val displayName: String) {
    PAYMENT_COMPLETED("결제완료"),
    PREPARING("작품준비"),
    SHIPPING("배송중"),
    DELIVERED("배송완료");

    companion object {
        /**
         * 전체 주문 상태 목록을 순서대로 반환
         */
        fun getAllSteps(): List<OrderStatus> = entries
    }
}

/**
 * 개별 주문 단계의 상태를 나타내는 데이터 클래스
 *
 * @property status 현재 단계의 주문 상태
 * @property isCompleted 해당 단계가 완료되었는지 여부
 * @property isActive 해당 단계가 현재 진행 중인지 여부
 */
@Stable
data class OrderStep(
    val status: OrderStatus,
    val isCompleted: Boolean = false,
    val isActive: Boolean = false
) {
    /**
     * 단계가 활성화되어야 하는지 (완료되었거나 현재 진행 중)
     */
    val isEnabled: Boolean
        get() = isCompleted || isActive
}

/**
 * 전체 주문 프로그레스 상태를 관리하는 데이터 클래스
 *
 * @property steps 전체 주문 단계 리스트
 * @property currentStepIndex 현재 진행 중인 단계의 인덱스
 */
@Stable
data class OrderProgressState(
    val steps: List<OrderStep>,
    val currentStepIndex: Int
) {
    /**
     * 현재 진행 중인 단계
     */
    val currentStep: OrderStep?
        get() = steps.getOrNull(currentStepIndex)

    /**
     * 전체 진행률 (0.0 ~ 1.0)
     */
    val overallProgress: Float
        get() = if (steps.isEmpty()) 0f else currentStepIndex.toFloat() / steps.size.toFloat()

    companion object {
        /**
         * 현재 주문 상태를 기반으로 OrderProgressState를 생성
         *
         * @param currentStatus 현재 주문 상태
         * @return OrderProgressState 인스턴스
         */
        fun fromCurrentStatus(currentStatus: OrderStatus): OrderProgressState {
            val allStatuses = OrderStatus.getAllSteps()
            val currentIndex = allStatuses.indexOf(currentStatus)

            val steps = allStatuses.mapIndexed { index, status ->
                OrderStep(
                    status = status,
                    isCompleted = index < currentIndex,
                    isActive = index == currentIndex
                )
            }

            return OrderProgressState(
                steps = steps,
                currentStepIndex = currentIndex
            )
        }
    }
}

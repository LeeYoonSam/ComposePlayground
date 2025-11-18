package com.ys.composeplayground.ui.scroll.timer.model

/**
 * 타이머 상태를 나타내는 데이터 클래스
 */
data class TimerState(
    val remainingSeconds: Int = 5,            // 남은 시간 (초)
    val isActive: Boolean = false,            // 타이머가 활성화되어 있는지
    val isCompleted: Boolean = false          // 타이머가 완료되었는지 (0초 도달)
) {
    /**
     * 타이머가 실제로 카운트다운 중인지 확인
     * 스크롤 중이고, 완료되지 않았을 때만 true
     */
    val isCountingDown: Boolean
        get() = isActive && !isCompleted && remainingSeconds > 0
}

package com.ys.composeplayground.ui.scroll.timer.model

/**
 * 타이머 완료 이벤트
 * 서버로부터 받은 완료 메시지와 이미지 URL을 담는다
 */
data class TimerCompletionEvent(
    val message: String,     // 완료 메시지 (포인트 정보 포함된 텍스트)
    val imageUrl: String     // 완료 이미지 URL
)

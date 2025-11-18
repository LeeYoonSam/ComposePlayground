package com.ys.composeplayground.ui.scroll.timer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ys.composeplayground.ui.scroll.timer.model.TimerCompletionEvent
import com.ys.composeplayground.ui.scroll.timer.model.TimerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 스크롤 기반 타이머 ViewModel
 *
 * 스크롤 시작 시 타이머가 시작/재개되고, 스크롤 정지 시 일시정지됩니다.
 * Activity 스택 간 상태를 유지하기 위해 SavedStateHandle을 사용합니다.
 */
class ScrollTimerViewModel(
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
) : ViewModel() {

    companion object {
        private const val KEY_REMAINING_SECONDS = "remaining_seconds"
        private const val KEY_IS_COMPLETED = "is_completed"
        private const val INITIAL_SECONDS = 5
    }

    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _completionEvent = MutableSharedFlow<TimerCompletionEvent>()
    val completionEvent: SharedFlow<TimerCompletionEvent> = _completionEvent.asSharedFlow()

    private var accumulatedMillis = 0L // 누적된 스크롤 시간 (밀리초)
    private var scrollStartTime = 0L // 스크롤 시작 시간
    private var isScrolling = false // 현재 스크롤 중인지
    private var isActivityPaused = false // Activity가 일시정지 상태인지
    private var timerJob: Job? = null // 실시간 카운트다운 코루틴

    init {
        // SavedStateHandle에서 상태 복원
        restoreState()
    }

    /**
     * 스크롤 시작 - 시작 시간 기록 및 실시간 카운트다운 시작
     */
    fun onScrollStarted() {
        if (!isScrolling && !_timerState.value.isCompleted && !isActivityPaused) {
            isScrolling = true
            scrollStartTime = System.currentTimeMillis()
            _timerState.update { it.copy(isActive = true) }

            // 실시간 카운트다운 시작
            timerJob?.cancel()
            timerJob = viewModelScope.launch {
                while (isScrolling && _timerState.value.remainingSeconds > 0) {
                    delay(100) // 100ms마다 업데이트 (매끄러운 애니메이션)

                    // 현재까지 경과한 총 시간 계산
                    val currentElapsed = System.currentTimeMillis() - scrollStartTime + accumulatedMillis
                    val elapsedSeconds = (currentElapsed / 1000).toInt()
                    val newRemaining = (INITIAL_SECONDS - elapsedSeconds).coerceAtLeast(0)

                    _timerState.update { it.copy(remainingSeconds = newRemaining) }

                    // 0초 도달 시 완료 처리
                    if (newRemaining == 0) {
                        onTimerComplete()
                        isScrolling = false
                        break
                    }
                }
            }
        }
    }

    /**
     * 스크롤 정지 - 타이머 코루틴 중지 및 누적 시간 저장
     */
    fun onScrollStopped() {
        if (isScrolling) {
            // 실시간 카운트다운 중지
            timerJob?.cancel()

            // 스크롤한 시간을 누적
            val scrollDuration = System.currentTimeMillis() - scrollStartTime
            accumulatedMillis += scrollDuration

            _timerState.update { it.copy(isActive = false) }

            saveState()
            isScrolling = false
        }
    }

    /**
     * 스크롤 이벤트 (호환성 유지)
     */
    fun onScrollEvent() {
        onScrollStarted()
    }

    /**
     * Activity가 일시정지 상태로 전환될 때 호출
     * (onPause)
     */
    fun onActivityPaused() {
        if (isScrolling) {
            onScrollStopped()
        }
        isActivityPaused = true
    }

    /**
     * Activity가 재개 상태로 전환될 때 호출
     * (onResume)
     */
    fun onActivityResumed() {
        isActivityPaused = false
        // 타이머는 사용자가 스크롤할 때 자동으로 재개됨
    }

    /**
     * 타이머 완료 - 서버에서 완료 보상 받아오기
     */
    private fun onTimerComplete() {
        _timerState.update {
            it.copy(
                isCompleted = true,
                isActive = false
            )
        }
        saveState()

        // 서버에서 완료 보상 받아오기 (현재는 Mock)
        viewModelScope.launch {
            try {
                val event = fetchCompletionReward()
                _completionEvent.emit(event)
            } catch (e: Exception) {
                // 에러 처리 (나중에 실제 서버 연동 시 구현)
            }
        }
    }

    /**
     * 서버에서 완료 보상 받아오기 (Mock 함수)
     * TODO: 실제 서버 API 호출로 교체
     */
    private suspend fun fetchCompletionReward(): TimerCompletionEvent {
        delay(500) // 네트워크 지연 시뮬레이션
        return TimerCompletionEvent(
            message = "축하합니다! 20P 적립!",
            imageUrl = "https://picsum.photos/200"
        )
    }

    /**
     * 상태를 SavedStateHandle에 저장
     */
    private fun saveState() {
        savedStateHandle[KEY_REMAINING_SECONDS] = _timerState.value.remainingSeconds
        savedStateHandle[KEY_IS_COMPLETED] = _timerState.value.isCompleted
        savedStateHandle["accumulated_millis"] = accumulatedMillis
    }

    /**
     * SavedStateHandle에서 상태 복원
     */
    private fun restoreState() {
        val remainingSeconds = savedStateHandle.get<Int>(KEY_REMAINING_SECONDS) ?: INITIAL_SECONDS
        val isCompleted = savedStateHandle.get<Boolean>(KEY_IS_COMPLETED) ?: false
        accumulatedMillis = savedStateHandle.get<Long>("accumulated_millis") ?: 0L

        _timerState.value = TimerState(
            remainingSeconds = remainingSeconds,
            isCompleted = isCompleted
        )
    }

    /**
     * 타이머 리셋 (테스트용)
     */
    fun resetTimer() {
        timerJob?.cancel()
        isScrolling = false
        isActivityPaused = false
        accumulatedMillis = 0L
        scrollStartTime = 0L
        _timerState.value = TimerState(remainingSeconds = INITIAL_SECONDS)
        savedStateHandle.remove<Int>(KEY_REMAINING_SECONDS)
        savedStateHandle.remove<Boolean>(KEY_IS_COMPLETED)
        savedStateHandle.remove<Long>("accumulated_millis")
    }

    override fun onCleared() {
        super.onCleared()
        // 타이머 코루틴 취소
        timerJob?.cancel()
        // 스크롤 중이었다면 정지 처리
        if (isScrolling) {
            onScrollStopped()
        }
    }
}

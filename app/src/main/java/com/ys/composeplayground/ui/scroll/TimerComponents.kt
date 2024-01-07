package com.ys.composeplayground.ui.scroll

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 타이머 컴포넌트
 * - 타이머 영역이 상단에 위치하고, 하단에는 리스트가 있으며 리스트의 스크롤 시작시 타이머가 시작되고 지정된 시간동안 타이머가 흘러간 후 종료되는 컴포넌트
 */
@Composable
fun TimerComponent() {
    // 타이머 진행 상태를 기억
    var timerState by remember { mutableStateOf(TimerState.NotStarted) }

    // 스크롤 상태
    val lazyListState = rememberLazyListState()

    // 타이머가 완료되면 실행할 액션
    val onCompletedTimer = {
        timerState = TimerState.Completed
    }

    // 스크롤 위치 변경 감지
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
            .collect { offset ->
                // 스크롤 위치가 변경될 때마다 호출되는 블록
                // index를 이용하여 스크롤 위치에 대한 작업 수행
                println("현재 스크롤 offset: $offset")

                if (offset > 0 && timerState == TimerState.NotStarted) {
                    // 뷰가 스크롤되고 타이머가 시작되지 않았을 때 타이머를 시작합니다.
                    timerState = TimerState.Running
                }
            }
    }

    val list = listOf(
        "A", "B", "C", "D"
    ) + ((0..100).map { it.toString() })

    Column {
        // 타이머 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
        ) {
            when (timerState) {
                TimerState.NotStarted -> {
                    // 시작하지 않았을경우 안내
                    BeforeStartTimer()
                }
                TimerState.Running -> {
                    // 실행 중인 타이머 표시
                    RunningTimer(
                        timerDuration = 10,
                        timerCompleted = onCompletedTimer
                    )
                }
                TimerState.Completed -> {
                    CompletedTimer()
                }
            }
        }
        // 리스트 영역
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = lazyListState,
        ) {
            items(items = list, itemContent =  { item ->
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = item,
                    style = TextStyle(fontSize = 80.sp)
                )
            })
        }
    }
}

@Composable
fun BeforeStartTimer() {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.WatchLater,
            contentDescription = "Timer Running",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = "스크롤시 타이머가 시작됩니다.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
        )
    }
}

@Composable
fun CompletedTimer() {
    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CardGiftcard,
            contentDescription = "Timer Completed",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = "미션이 완료 되었습니다.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
        )
    }
}

@Composable
fun RunningTimer(timerDuration: Int = 30, timerCompleted: () -> Unit) {

    /**
     * getContext가 제공하는 선택적 CoroutineContext를 사용해 컴포지션의 이 지점에 바인딩된 CoroutineScope를 반환합니다.
     * getContext는 한 번만 호출되며 재구성에 걸쳐 동일한 CoroutineScope 인스턴스가 반환됩니다.
     * 이 호출이 컴포지션에서 벗어나면 이 coroutineScope 는 취소됩니다.
     */
    val coroutineScope = rememberCoroutineScope()

    // 타이머로 변경되는 숫자 값을 저장
    var remainingTime by remember {
        mutableStateOf(timerDuration)
    }

    /**
     * 비동기적인 작업을 수행하고 그 결과에 따라 UI를 업데이트할 때 사용.
     * 주로 Android의 생명주기와 관련이 없는 비동기 작업을 처리할 때 사용됩니다.
     */
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            while (remainingTime > 0) {
                delay(1000)
                remainingTime--
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val tintColor = if (remainingTime < 1) Color.White else Color.White
        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = "Timer Running",
            tint = tintColor,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        if (remainingTime == 0) {
            timerCompleted()
        } else {
            Text(
                text = "Timer Running: $remainingTime seconds",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}

/**
 * 타이머가 가질수 있는 상태
 */
enum class TimerState {
    NotStarted,
    Running,
    Completed
}

@Preview
@Composable
fun PreviewBeforeScrollText() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
    ) {
        BeforeStartTimer()
    }
}

@Preview
@Composable
fun PreviewCompletedScrollText() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
    ) {
        CompletedTimer()
    }
}

@Preview
@Composable
fun PreviewTimerComponent() {
    TimerComponent()
}
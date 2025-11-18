package com.ys.composeplayground.ui.scroll.timer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.ui.scroll.timer.model.TimerState
import com.ys.composeplayground.ui.theme.LocalTypography
import kotlinx.coroutines.delay

/**
 * 스크롤 기반 타이머 배너
 *
 * - 높이: 30dp
 * - 너비: match_parent
 * - 텍스트 롤링 애니메이션: 1초 간격으로 위로 슬라이드
 */
@Composable
fun TimerBannerView(
    timerState: TimerState,
    modifier: Modifier = Modifier
) {
    // 타이머가 완료되면 배너를 표시하지 않음
    if (timerState.isCompleted) {
        return
    }

    val messages = listOf(
        "이 작품 조금 더 구경해 볼까요?",
        "작품 속 숨은 선물을 찾아보세요!"
    )

    var currentMessageIndex by remember { mutableIntStateOf(0) }

    // 1초마다 메시지 전환
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentMessageIndex = (currentMessageIndex + 1) % messages.size
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFF6B6B).copy(alpha = 0.8f),
                        Color(0xFFFFB347).copy(alpha = 0.8f)
                    )
                )
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 롤링 텍스트 (왼쪽 정렬)
            AnimatedContent(
                targetState = currentMessageIndex,
                transitionSpec = {
                    (slideInVertically(
                        animationSpec = tween(300),
                        initialOffsetY = { it }
                    ) + fadeIn(animationSpec = tween(300)))
                        .togetherWith(
                            slideOutVertically(
                                animationSpec = tween(300),
                                targetOffsetY = { -it }
                            ) + fadeOut(animationSpec = tween(300))
                        )
                },
                label = "rolling_text",
                modifier = Modifier.weight(1f)
            ) { index ->
                Text(
                    text = messages[index],
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            // 타이머 표시 (오른쪽 정렬)
            Text(
                text = "${timerState.remainingSeconds}초",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                style = LocalTypography.current.caption1BoldMedium.copy()
            )
        }
    }
}

package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ys.composeplayground.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 📱 실무 애니메이션 #1: Lottie 통합
 *
 * 📖 핵심 개념
 *
 * Lottie는 After Effects 애니메이션을 JSON으로 변환하여 앱에서 사용하는 라이브러리입니다.
 * 디자이너가 만든 복잡한 애니메이션을 코드 없이 그대로 구현할 수 있습니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * rememberLottieComposition | Lottie 파일 로드 (raw, url, asset)
 * animateLottieCompositionAsState | 자동 재생, 속도, 반복 제어
 * LottieAnimation | 애니메이션 렌더링
 * progress | 수동 진행률 제어
 *
 * 💡 실무 시나리오
 *
 * - 로딩 상태: 브랜드 로딩 애니메이션
 * - 성공/실패: 체크마크, X 표시
 * - 빈 상태: Empty state 일러스트
 * - 좋아요: 터치 시 재생되는 인터랙션
 * - Pull-to-Refresh: 드래그 진행률 연동
 *
 * 학습 목표:
 * 1. Lottie 파일 로드 (raw, url, assets)
 * 2. 재생 제어 (play, pause, speed, iterations)
 * 3. 진행률 제어 (progress)
 * 4. 실무 패턴 (로딩, 성공/실패, 빈 상태)
 */

// ============================================
// 1. 기본 Lottie 애니메이션
// ============================================

/**
 * 가장 기본적인 Lottie 애니메이션 사용법
 *
 * - URL에서 JSON 로드 (개발/테스트용)
 * - 무한 반복 재생
 * - 실무에서는 res/raw/ 사용 권장
 */
@Composable
fun BasicLottieAnimation(modifier: Modifier = Modifier) {
    // URL에서 로드 (예제용)
    // 실무에서는 LottieCompositionSpec.RawRes(R.raw.loading) 사용
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )

    // 애니메이션 진행 상태 (무한 반복)
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(150.dp)
        )
    }
}

// ============================================
// 2. 재생 제어 (Play/Pause/Speed)
// ============================================

/**
 * 재생/정지, 속도 조절이 가능한 Lottie 컴포넌트
 *
 * - isPlaying: 재생/정지 토글
 * - speed: 0.25x ~ 3x 속도 조절
 * - restartOnPlay: false로 설정하면 정지 위치에서 재개
 */
@Composable
fun ControlledLottieAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )

    var isPlaying by remember { mutableStateOf(true) }
    var speed by remember { mutableFloatStateOf(1f) }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false // 일시정지 후 재생 시 처음부터 시작하지 않음
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2D3436))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E272E)),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(120.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 재생/정지 버튼
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = { isPlaying = !isPlaying },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPlaying) Color(0xFFFF6B6B) else Color(0xFF00B894)
            ),
            modifier = Modifier.width(100.dp)
        ) {
            Text(if (isPlaying) "정지" else "재생")
        }

        Button(
            onClick = { speed = 1f },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7))
        ) {
            Text("리셋")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 속도 조절
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "속도",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.width(50.dp)
        )

        Slider(
            value = speed,
            onValueChange = { speed = it },
            valueRange = 0.25f..3f,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF74B9FF),
                activeTrackColor = Color(0xFF74B9FF)
            )
        )

        Text(
            text = "%.1fx".format(speed),
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            modifier = Modifier.width(45.dp),
            textAlign = TextAlign.End
        )
    }
}

// ============================================
// 3. 진행률 직접 제어
// ============================================

/**
 * 진행률을 수동으로 제어하는 예제
 *
 * - 슬라이더로 애니메이션 프레임 직접 제어
 * - 스크롤, 드래그와 연동할 때 유용
 */
@Composable
fun ProgressControlledLottie(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )

    var manualProgress by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F0F23)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Lottie 애니메이션 (진행률 직접 제어)
        LottieAnimation(
            composition = composition,
            progress = { manualProgress }, // 수동 진행률
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 진행률 슬라이더
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "진행률",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.width(60.dp)
            )

            Slider(
                value = manualProgress,
                onValueChange = { manualProgress = it },
                valueRange = 0f..1f,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFFE66D),
                    activeTrackColor = Color(0xFFFFE66D)
                )
            )

            Text(
                text = "${(manualProgress * 100).toInt()}%",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier.width(45.dp),
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "슬라이더를 드래그하여 애니메이션 프레임 제어",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp
        )
    }
}

// ============================================
// 4. 로딩 상태 표시
// ============================================

/**
 * 로딩 상태에서만 Lottie 애니메이션 표시
 *
 * - 데이터 로드 중 표시
 * - 로드 완료 시 콘텐츠로 전환
 */
@Composable
fun LoadingStateWithLottie(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )

    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E272E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF2D3436)),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val progress by animateLottieCompositionAsState(
                        composition = composition,
                        iterations = LottieConstants.IterateForever
                    )

                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "로딩 중...",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            } else {
                Text(
                    text = "콘텐츠 영역",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                scope.launch {
                    delay(3000) // 3초 후 로딩 완료
                    isLoading = false
                }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7))
        ) {
            Text(if (isLoading) "로딩 중..." else "데이터 로드")
        }
    }
}

// ============================================
// 5. 성공/실패 상태 애니메이션
// ============================================

private enum class ResultState {
    Idle,
    Loading,
    Success,
    Error
}

/**
 * 성공/실패 상태에 따른 애니메이션 전환
 *
 * - 상태별 다른 Lottie 애니메이션 표시
 * - 성공/실패는 1회만 재생
 */
@Composable
fun ResultStateAnimation(modifier: Modifier = Modifier) {
    val successComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.success)
    )

    val errorComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.error)
    )

    val loadingComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )

    var state by remember { mutableStateOf(ResultState.Idle) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2C3E50))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    when (state) {
                        ResultState.Idle -> Color(0xFF34495E)
                        ResultState.Loading -> Color(0xFF34495E)
                        ResultState.Success -> Color(0xFF27AE60).copy(alpha = 0.2f)
                        ResultState.Error -> Color(0xFFE74C3C).copy(alpha = 0.2f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                ResultState.Idle -> {
                    Text(
                        text = "대기 중",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                }

                ResultState.Loading -> {
                    val progress by animateLottieCompositionAsState(
                        composition = loadingComposition,
                        iterations = LottieConstants.IterateForever
                    )
                    LottieAnimation(
                        composition = loadingComposition,
                        progress = { progress },
                        modifier = Modifier.size(80.dp)
                    )
                }

                ResultState.Success -> {
                    val progress by animateLottieCompositionAsState(
                        composition = successComposition,
                        iterations = 1 // 한 번만 재생
                    )
                    LottieAnimation(
                        composition = successComposition,
                        progress = { progress },
                        modifier = Modifier.size(100.dp)
                    )
                }

                ResultState.Error -> {
                    val progress by animateLottieCompositionAsState(
                        composition = errorComposition,
                        iterations = 1
                    )
                    LottieAnimation(
                        composition = errorComposition,
                        progress = { progress },
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    state = ResultState.Loading
                    scope.launch {
                        delay(2000)
                        state = ResultState.Success
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27AE60)),
                enabled = state == ResultState.Idle || state == ResultState.Error
            ) {
                Text("성공 시뮬")
            }

            Button(
                onClick = {
                    state = ResultState.Loading
                    scope.launch {
                        delay(2000)
                        state = ResultState.Error
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C)),
                enabled = state == ResultState.Idle || state == ResultState.Success
            ) {
                Text("실패 시뮬")
            }
        }

        if (state == ResultState.Success || state == ResultState.Error) {
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { state = ResultState.Idle },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F8C8D))
            ) {
                Text("리셋")
            }
        }
    }
}

/**
 * 데이터가 없을 때 표시하는 Empty State
 *
 * - 무한 반복 재생
 * - 데이터 추가 시 콘텐츠로 전환
 */
@Composable
fun EmptyStateWithLottie(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.empty)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    var hasData by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F6FA)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (hasData) {
            // 데이터가 있을 때
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { index ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = "아이템 ${index + 1}",
                            modifier = Modifier.padding(16.dp),
                            color = Color(0xFF2D3436)
                        )
                    }
                }
            }
        } else {
            // 빈 상태
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "아직 데이터가 없어요",
                color = Color(0xFF636E72),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "새로운 항목을 추가해보세요",
                color = Color(0xFF95A5A6),
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { hasData = !hasData },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7))
        ) {
            Text(if (hasData) "데이터 삭제" else "데이터 추가")
        }
    }
}

// ============================================
// 7. 클릭 애니메이션 (좋아요 버튼)
// ============================================

/**
 * 클릭 시 재생되는 좋아요 버튼
 *
 * - Animatable로 수동 진행률 제어
 * - 클릭 시 0→1 애니메이션 재생
 */
@Composable
fun LikeButtonWithLottie(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.heart)
    )

    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableIntStateOf(42) }

    // 애니메이션 진행률 (수동 제어)
    val animProgress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E))
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Lottie 하트 버튼
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .clickable {
                    isLiked = !isLiked
                    if (isLiked) {
                        likeCount++
                        scope.launch {
                            animProgress.snapTo(0f)
                            animProgress.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(1000)
                            )
                        }
                    } else {
                        likeCount--
                        scope.launch {
                            animProgress.snapTo(0f)
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = { animProgress.value },
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "$likeCount",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "좋아요",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
        }
    }
}

// ============================================
// 8. 드래그로 진행률 제어 (Pull-to-Refresh 스타일)
// ============================================

/**
 * 드래그 제스처로 진행률 연동
 *
 * - detectVerticalDragGestures로 드래그 감지
 * - 드래그 양에 따라 진행률 변화
 * - 100% 도달 시 새로고침 트리거
 */
@Composable
fun DragControlledLottie(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.refresh)
    )

    var dragProgress by remember { mutableFloatStateOf(0f) }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // 자동 재생 진행률 (새로고침 중일 때)
    val autoProgress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isRefreshing
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2D3436))
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        if (dragProgress >= 1f && !isRefreshing) {
                            // 새로고침 트리거
                            isRefreshing = true
                            scope.launch {
                                delay(2000) // 새로고침 시뮬레이션
                                isRefreshing = false
                                dragProgress = 0f
                            }
                        } else if (!isRefreshing) {
                            // 원래대로 복귀
                            dragProgress = 0f
                        }
                    },
                    onVerticalDrag = { _, dragAmount ->
                        if (!isRefreshing) {
                            dragProgress = (dragProgress + dragAmount / 500f)
                                .coerceIn(0f, 1.2f)
                        }
                    }
                )
            }
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFEFEFE)),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = {
                    if (isRefreshing) autoProgress
                    else dragProgress.coerceIn(0f, 1f)
                },
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = when {
                isRefreshing -> "새로고침 중..."
                dragProgress >= 1f -> "놓으면 새로고침"
                dragProgress > 0f -> "아래로 당기세요"
                else -> "아래로 드래그하여 새로고침"
            },
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 진행률 표시
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFF636E72))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(dragProgress.coerceIn(0f, 1f))
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (dragProgress >= 1f) Color(0xFF00B894)
                        else Color(0xFF74B9FF)
                    )
            )
        }
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun LottieIntegrationDemo() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Lottie 통합",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "1. 기본 Lottie 애니메이션") {
            BasicLottieAnimation()
        }

        DemoSection(title = "2. 재생 제어 (Play/Pause/Speed)") {
            ControlledLottieAnimation()
        }

        DemoSection(title = "3. 진행률 직접 제어") {
            ProgressControlledLottie()
        }

        DemoSection(title = "4. 로딩 상태 표시") {
            LoadingStateWithLottie()
        }

        DemoSection(title = "5. 성공/실패 상태") {
            ResultStateAnimation()
        }

        DemoSection(title = "6. 빈 상태 (Empty State)") {
            EmptyStateWithLottie()
        }

        DemoSection(title = "7. 좋아요 버튼") {
            LikeButtonWithLottie()
        }

        DemoSection(title = "8. 드래그로 진행률 제어") {
            DragControlledLottie()
        }

        LottieGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LottieGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "📚 Lottie 통합 가이드",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = """
                    핵심 API:
                    
                    • rememberLottieComposition
                      - RawRes: res/raw/에서 로드
                      - Url: 네트워크에서 로드
                      - Asset: assets/에서 로드
                    
                    • animateLottieCompositionAsState
                      - iterations: 반복 횟수
                      - isPlaying: 재생/정지
                      - speed: 재생 속도
                      - restartOnPlay: 재시작 여부
                    
                    • LottieAnimation
                      - composition: Lottie 데이터
                      - progress: 진행률 (0f~1f)
                    
                    💡 실무 팁:
                    • 로딩: iterations = IterateForever
                    • 성공/실패: iterations = 1
                    • 인터랙션: progress 수동 제어
                    • 파일 크기: JSON < 50KB 권장
                """.trimIndent(),
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 2800)
@Composable
private fun LottieIntegrationDemoPreview() {
    LottieIntegrationDemo()
}
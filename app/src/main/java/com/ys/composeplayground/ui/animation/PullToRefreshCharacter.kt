package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * 🎯 Bonus #21: Pull-to-Refresh 캐릭터
 *
 * 📖 핵심 개념
 *
 * 당기면 캐릭터가 스트레칭되고, 놓으면 탄성있게 복귀하면서 리프레시를 트리거하는 재미있는 효과입니다. 기본 Pull-to-Refresh에 캐릭터 애니메이션을 추가합니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * Animatable | 당김 위치/캐릭터 변형
 * Canvas | 캐릭터 그리기
 * graphicsLayer | 스케일/회전 변형
 * detectVerticalDragGestures | 당김 감지
 * spring | 탄성 복귀
 *
 * 💡 동작 원리
 *
 * ```
 * [대기] 캐릭터 기본 상태
 *        ↓ 아래로 드래그
 * [당김 중]
 *   - pullOffset 증가
 *   - 캐릭터 세로로 늘어남 (scaleY 증가)
 *   - 눈이 커지고, 입이 벌어짐
 *        ↓ threshold 초과 후 놓음
 * [리프레시]
 *   - 캐릭터 회전 애니메이션
 *   - 로딩 상태 표시
 *        ↓ 완료
 * [복귀] spring으로 원래 모양으로
 * ```
 *
 * 학습 목표:
 * 1. 당김에 따른 캐릭터 변형
 * 2. Canvas로 귀여운 캐릭터 그리기
 * 3. 리프레시 상태에 따른 애니메이션
 * 4. 탄성 복귀 효과
 */
// ============================================
// 슬라임 캐릭터 Pull-to-Refresh
// ============================================
@Composable
fun SlimeCharacterRefresh(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val pullOffset = remember { Animatable(0f) }
    var isRefreshing by remember { mutableStateOf(false) }
    var frameTime by remember { mutableLongStateOf(0L) }

    val threshold = 150f
    val maxPull = 250f

    // 당김 진행률 (0~1)
    val progress = (pullOffset.value / threshold).coerceIn(0f, 1.5f)

    // 프레임 업데이트
    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE8F5E9))
    ) {
        // 슬라임 캐릭터 - 상단 고정, 당김에 따라 늘어남
        val characterSize = 100.dp
        val characterTopPadding = 20.dp

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = characterTopPadding)
                .size(characterSize)
                .graphicsLayer {
                    // 당길수록 세로로 늘어남
                    val stretchY = 1f + (pullOffset.value / maxPull) * 0.8f
                    val squashX = 1f - (pullOffset.value / maxPull) * 0.2f

                    scaleY = stretchY
                    scaleX = squashX

                    // 늘어난 만큼 아래로 이동 (피벗이 중앙이므로)
                    translationY = (stretchY - 1f) * size.height / 2

                    // 리프레시 중 흔들림
                    if (isRefreshing) {
                        rotationZ = sin(frameTime * 0.01f) * 5f
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawSlimeCharacter(
                    progress = progress,
                    isRefreshing = isRefreshing,
                    time = frameTime
                )
            }
        }

        // 상태 텍스트
        Text(
            text = when {
                isRefreshing -> "🔄 새로고침 중..."
                progress >= 1f -> "👆 놓아서 새로고침"
                progress > 0f -> "👇 당겨서 새로고침 (${(progress * 100).toInt()}%)"
                else -> ""
            },
            fontSize = 12.sp,
            color = Color(0xFF4CAF50),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = characterTopPadding + characterSize + 20.dp + (pullOffset.value * 0.3f).dp)
        )

        // 컨텐츠 영역 - 당김에 따라 아래로 이동
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.BottomCenter)
                .offset { IntOffset(0, pullOffset.value.roundToInt()) }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                if (pullOffset.value >= threshold && !isRefreshing) {
                                    isRefreshing = true
                                    // 리프레시 중에는 캐릭터가 보이는 위치 유지
                                    pullOffset.animateTo(
                                        threshold,
                                        spring(stiffness = Spring.StiffnessMedium)
                                    )

                                    // 시뮬레이션: 2초 후 완료
                                    delay(2000)
                                    isRefreshing = false
                                }

                                // 원위치로 복귀
                                pullOffset.animateTo(
                                    0f,
                                    spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }
                        },
                        onVerticalDrag = { _, dragAmount ->
                            if (!isRefreshing) {
                                scope.launch {
                                    // 저항 적용
                                    val resistance = 1f / (1f + pullOffset.value * 0.003f)
                                    val newOffset = (pullOffset.value + dragAmount * resistance)
                                        .coerceIn(0f, maxPull)
                                    pullOffset.snapTo(newOffset)
                                }
                            }
                        }
                    )
                },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // 드래그 핸들
                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFE0E0E0))
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "슬라임 캐릭터",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "위로 당기면 슬라임이 늘어나요!",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

private fun DrawScope.drawSlimeCharacter(
    progress: Float,
    isRefreshing: Boolean,
    time: Long
) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val baseRadius = size.minDimension * 0.4f

    // 색상
    val bodyColor = Color(0xFF81C784)
    val highlightColor = Color(0xFFC8E6C9)
    val shadowColor = Color(0xFF66BB6A)

    // 몸통 (물방울/슬라임 모양)
    val bodyPath = Path().apply {
        val radiusX = baseRadius
        val radiusY = baseRadius

        moveTo(centerX, centerY - radiusY * 0.8f)

        // 오른쪽 곡선
        cubicTo(
            centerX + radiusX * 1.1f, centerY - radiusY * 0.3f,
            centerX + radiusX * 0.9f, centerY + radiusY * 0.5f,
            centerX, centerY + radiusY * 0.9f
        )

        // 왼쪽 곡선
        cubicTo(
            centerX - radiusX * 0.9f, centerY + radiusY * 0.5f,
            centerX - radiusX * 1.1f, centerY - radiusY * 0.3f,
            centerX, centerY - radiusY * 0.8f
        )

        close()
    }

    // 그림자
    drawPath(path = bodyPath, color = shadowColor)

    // 하이라이트
    drawCircle(
        color = highlightColor,
        radius = baseRadius * 0.2f,
        center = Offset(centerX - baseRadius * 0.25f, centerY - baseRadius * 0.35f)
    )
    drawCircle(
        color = highlightColor.copy(alpha = 0.5f),
        radius = baseRadius * 0.1f,
        center = Offset(centerX - baseRadius * 0.4f, centerY - baseRadius * 0.15f)
    )

    // 눈 (당길수록 커짐, 리프레시 중 반짝임)
    val eyeBaseSize = baseRadius * 0.13f
    val eyeGrowth = if (isRefreshing) {
        1f + sin(time * 0.008f) * 0.15f
    } else {
        1f + progress * 0.4f
    }
    val eyeSize = eyeBaseSize * eyeGrowth

    val eyeY = centerY - baseRadius * 0.15f
    val eyeSpacing = baseRadius * 0.28f

    // 왼쪽 눈
    drawCircle(
        color = Color.White,
        radius = eyeSize,
        center = Offset(centerX - eyeSpacing, eyeY)
    )
    drawCircle(
        color = Color(0xFF333333),
        radius = eyeSize * 0.55f,
        center = Offset(centerX - eyeSpacing, eyeY)
    )
    // 눈 반짝임
    drawCircle(
        color = Color.White,
        radius = eyeSize * 0.2f,
        center = Offset(centerX - eyeSpacing - eyeSize * 0.2f, eyeY - eyeSize * 0.2f)
    )

    // 오른쪽 눈
    drawCircle(
        color = Color.White,
        radius = eyeSize,
        center = Offset(centerX + eyeSpacing, eyeY)
    )
    drawCircle(
        color = Color(0xFF333333),
        radius = eyeSize * 0.55f,
        center = Offset(centerX + eyeSpacing, eyeY)
    )
    drawCircle(
        color = Color.White,
        radius = eyeSize * 0.2f,
        center = Offset(centerX + eyeSpacing - eyeSize * 0.2f, eyeY - eyeSize * 0.2f)
    )

    // 입
    val mouthY = centerY + baseRadius * 0.2f
    val mouthWidth = baseRadius * 0.25f

    when {
        isRefreshing -> {
            // 리프레시 중: 빙글빙글 웃는 입
            val mouthPhase = (time * 0.005f) % 360f
            drawArc(
                color = Color(0xFFE57373),
                startAngle = mouthPhase,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(centerX - mouthWidth / 2, mouthY - mouthWidth / 4),
                size = Size(mouthWidth, mouthWidth / 2)
            )
        }

        progress > 0.7f -> {
            // 놀란 입 (O 모양)
            val openSize = mouthWidth * 0.4f + progress * mouthWidth * 0.2f
            drawCircle(
                color = Color(0xFFE57373),
                radius = openSize,
                center = Offset(centerX, mouthY)
            )
            // 혀
            drawCircle(
                color = Color(0xFFEF9A9A),
                radius = openSize * 0.5f,
                center = Offset(centerX, mouthY + openSize * 0.3f)
            )
        }

        else -> {
            // 일반 미소
            drawArc(
                color = Color(0xFFE57373),
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(centerX - mouthWidth / 2, mouthY),
                size = Size(mouthWidth, mouthWidth / 2.5f),
                style = Stroke(width = 3f, cap = StrokeCap.Round)
            )
        }
    }

    // 볼터치
    val blushAlpha = if (progress > 0.5f || isRefreshing) 0.7f else 0.4f
    drawCircle(
        color = Color(0xFFFFCDD2).copy(alpha = blushAlpha),
        radius = baseRadius * 0.12f,
        center = Offset(centerX - eyeSpacing - baseRadius * 0.15f, eyeY + baseRadius * 0.2f)
    )
    drawCircle(
        color = Color(0xFFFFCDD2).copy(alpha = blushAlpha),
        radius = baseRadius * 0.12f,
        center = Offset(centerX + eyeSpacing + baseRadius * 0.15f, eyeY + baseRadius * 0.2f)
    )
}

// ============================================
// 고양이 캐릭터 Pull-to-Refresh
// ============================================
@Composable
fun CatCharacterRefresh(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val pullOffset = remember { Animatable(0f) }
    var isRefreshing by remember { mutableStateOf(false) }
    var frameTime by remember { mutableLongStateOf(0L) }

    val threshold = 150f
    val maxPull = 250f
    val progress = (pullOffset.value / threshold).coerceIn(0f, 1.5f)

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFF3E0))
    ) {
        val characterSize = 110.dp
        val characterTopPadding = 15.dp

        // 고양이 캐릭터
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = characterTopPadding)
                .size(characterSize)
                .graphicsLayer {
                    // 당길수록 늘어남
                    val stretchY = 1f + (pullOffset.value / maxPull) * 0.6f
                    val squashX = 1f - (pullOffset.value / maxPull) * 0.15f

                    scaleY = stretchY
                    scaleX = squashX
                    translationY = (stretchY - 1f) * size.height / 2

                    // 리프레시 중 좌우 흔들림
                    if (isRefreshing) {
                        translationX = sin(frameTime * 0.015f) * 10f
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCatCharacter(
                    progress = progress,
                    isRefreshing = isRefreshing,
                    time = frameTime
                )
            }
        }

        // 상태 텍스트
        Text(
            text = when {
                isRefreshing -> "🐱 냥냥~ 로딩중..."
                progress >= 1f -> "😺 놓으면 새로고침!"
                progress > 0f -> "🐱 더 당겨줘~ (${(progress * 100).toInt()}%)"
                else -> ""
            },
            fontSize = 12.sp,
            color = Color(0xFFFF9800),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = characterTopPadding + characterSize + 15.dp + (pullOffset.value * 0.25f).dp)
        )

        // 컨텐츠 영역
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.BottomCenter)
                .offset { IntOffset(0, pullOffset.value.roundToInt()) }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                if (pullOffset.value >= threshold && !isRefreshing) {
                                    isRefreshing = true
                                    pullOffset.animateTo(threshold, spring(stiffness = Spring.StiffnessMedium))
                                    delay(2000)
                                    isRefreshing = false
                                }
                                pullOffset.animateTo(
                                    0f,
                                    spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }
                        },
                        onVerticalDrag = { _, dragAmount ->
                            if (!isRefreshing) {
                                scope.launch {
                                    val resistance = 1f / (1f + pullOffset.value * 0.003f)
                                    pullOffset.snapTo(
                                        (pullOffset.value + dragAmount * resistance).coerceIn(0f, maxPull)
                                    )
                                }
                            }
                        }
                    )
                },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFE0E0E0))
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "고양이 캐릭터",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "당기면 귀가 쫑긋!",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

private fun DrawScope.drawCatCharacter(
    progress: Float,
    isRefreshing: Boolean,
    time: Long
) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val baseRadius = size.minDimension * 0.32f

    val bodyColor = Color(0xFFFFB74D)
    val darkColor = Color(0xFFFF9800)
    val innerEarColor = Color(0xFFFFCDD2)

    // 얼굴
    drawCircle(
        color = bodyColor,
        radius = baseRadius,
        center = Offset(centerX, centerY)
    )

    // 귀 (당길수록 길어짐)
    val earHeight = baseRadius * 0.7f * (1f + progress * 0.5f)
    val earWidth = baseRadius * 0.5f

    // 왼쪽 귀
    val leftEarPath = Path().apply {
        moveTo(centerX - baseRadius * 0.5f, centerY - baseRadius * 0.6f)
        lineTo(centerX - baseRadius * 0.7f, centerY - baseRadius - earHeight)
        lineTo(centerX - baseRadius * 0.15f, centerY - baseRadius * 0.4f)
        close()
    }
    drawPath(leftEarPath, bodyColor)

    // 왼쪽 귀 안쪽
    val leftInnerEarPath = Path().apply {
        moveTo(centerX - baseRadius * 0.45f, centerY - baseRadius * 0.55f)
        lineTo(centerX - baseRadius * 0.6f, centerY - baseRadius - earHeight * 0.7f)
        lineTo(centerX - baseRadius * 0.25f, centerY - baseRadius * 0.45f)
        close()
    }
    drawPath(leftInnerEarPath, innerEarColor)

    // 오른쪽 귀
    val rightEarPath = Path().apply {
        moveTo(centerX + baseRadius * 0.5f, centerY - baseRadius * 0.6f)
        lineTo(centerX + baseRadius * 0.7f, centerY - baseRadius - earHeight)
        lineTo(centerX + baseRadius * 0.15f, centerY - baseRadius * 0.4f)
        close()
    }
    drawPath(rightEarPath, bodyColor)

    // 오른쪽 귀 안쪽
    val rightInnerEarPath = Path().apply {
        moveTo(centerX + baseRadius * 0.45f, centerY - baseRadius * 0.55f)
        lineTo(centerX + baseRadius * 0.6f, centerY - baseRadius - earHeight * 0.7f)
        lineTo(centerX + baseRadius * 0.25f, centerY - baseRadius * 0.45f)
        close()
    }
    drawPath(rightInnerEarPath, innerEarColor)

    // 눈
    val eyeY = centerY - baseRadius * 0.05f
    val eyeSpacing = baseRadius * 0.4f
    val eyeWidth = baseRadius * 0.18f
    val eyeHeight = baseRadius * 0.22f

    // 눈 깜빡임 (리프레시 중)
    val blinkPhase = if (isRefreshing) (time * 0.004f) % 1f else 0f
    val eyeScaleY = when {
        blinkPhase > 0.9f -> 0.1f
        progress > 0.8f -> 1.3f  // 놀란 눈
        else -> 1f
    }

    // 왼쪽 눈
    drawOval(
        color = Color(0xFF333333),
        topLeft = Offset(centerX - eyeSpacing - eyeWidth / 2, eyeY - eyeHeight * eyeScaleY / 2),
        size = Size(eyeWidth, eyeHeight * eyeScaleY)
    )
    if (eyeScaleY > 0.5f) {
        drawCircle(
            color = Color.White,
            radius = eyeWidth * 0.25f,
            center = Offset(centerX - eyeSpacing - eyeWidth * 0.15f, eyeY - eyeHeight * 0.15f)
        )
    }

    // 오른쪽 눈
    drawOval(
        color = Color(0xFF333333),
        topLeft = Offset(centerX + eyeSpacing - eyeWidth / 2, eyeY - eyeHeight * eyeScaleY / 2),
        size = Size(eyeWidth, eyeHeight * eyeScaleY)
    )
    if (eyeScaleY > 0.5f) {
        drawCircle(
            color = Color.White,
            radius = eyeWidth * 0.25f,
            center = Offset(centerX + eyeSpacing - eyeWidth * 0.15f, eyeY - eyeHeight * 0.15f)
        )
    }

    // 코
    val noseY = centerY + baseRadius * 0.15f
    drawOval(
        color = Color(0xFFE57373),
        topLeft = Offset(centerX - baseRadius * 0.08f, noseY - baseRadius * 0.05f),
        size = Size(baseRadius * 0.16f, baseRadius * 0.1f)
    )

    // 입 (ω 모양)
    val mouthY = noseY + baseRadius * 0.12f

    if (isRefreshing || progress > 0.8f) {
        // 하품/놀란 입
        drawOval(
            color = Color(0xFFE57373),
            topLeft = Offset(centerX - baseRadius * 0.12f, mouthY),
            size = Size(baseRadius * 0.24f, baseRadius * 0.18f)
        )
    } else {
        // ω 모양 입
        val mouthPath = Path().apply {
            moveTo(centerX - baseRadius * 0.2f, mouthY)
            quadraticBezierTo(
                centerX - baseRadius * 0.1f, mouthY + baseRadius * 0.12f,
                centerX, mouthY
            )
            quadraticBezierTo(
                centerX + baseRadius * 0.1f, mouthY + baseRadius * 0.12f,
                centerX + baseRadius * 0.2f, mouthY
            )
        }
        drawPath(mouthPath, Color(0xFF5D4037), style = Stroke(width = 2.5f, cap = StrokeCap.Round))
    }

    // 수염
    val whiskerY = centerY + baseRadius * 0.1f
    val whiskerLength = baseRadius * 0.55f
    val whiskerOffsets = listOf(-0.1f, 0f, 0.1f)

    whiskerOffsets.forEach { offsetY ->
        // 왼쪽 수염
        drawLine(
            color = Color(0xFF5D4037).copy(alpha = 0.6f),
            start = Offset(centerX - baseRadius * 0.35f, whiskerY + baseRadius * offsetY),
            end = Offset(
                centerX - baseRadius * 0.35f - whiskerLength,
                whiskerY + baseRadius * offsetY - offsetY * baseRadius * 0.3f
            ),
            strokeWidth = 1.5f,
            cap = StrokeCap.Round
        )

        // 오른쪽 수염
        drawLine(
            color = Color(0xFF5D4037).copy(alpha = 0.6f),
            start = Offset(centerX + baseRadius * 0.35f, whiskerY + baseRadius * offsetY),
            end = Offset(
                centerX + baseRadius * 0.35f + whiskerLength,
                whiskerY + baseRadius * offsetY - offsetY * baseRadius * 0.3f
            ),
            strokeWidth = 1.5f,
            cap = StrokeCap.Round
        )
    }

    // 볼터치
    drawCircle(
        color = Color(0xFFFFCDD2).copy(alpha = 0.5f),
        radius = baseRadius * 0.12f,
        center = Offset(centerX - eyeSpacing - baseRadius * 0.1f, eyeY + baseRadius * 0.25f)
    )
    drawCircle(
        color = Color(0xFFFFCDD2).copy(alpha = 0.5f),
        radius = baseRadius * 0.12f,
        center = Offset(centerX + eyeSpacing + baseRadius * 0.1f, eyeY + baseRadius * 0.25f)
    )
}

// ============================================
// 로봇 캐릭터 Pull-to-Refresh
// ============================================
@Composable
fun RobotCharacterRefresh(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val pullOffset = remember { Animatable(0f) }
    var isRefreshing by remember { mutableStateOf(false) }
    var frameTime by remember { mutableLongStateOf(0L) }

    val threshold = 150f
    val maxPull = 250f
    val progress = (pullOffset.value / threshold).coerceIn(0f, 1.5f)

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE3F2FD))
    ) {
        val characterSize = 100.dp
        val characterTopPadding = 20.dp

        // 로봇 캐릭터
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = characterTopPadding)
                .size(characterSize)
                .graphicsLayer {
                    val stretchY = 1f + (pullOffset.value / maxPull) * 0.5f
                    val squashX = 1f - (pullOffset.value / maxPull) * 0.1f

                    scaleY = stretchY
                    scaleX = squashX
                    translationY = (stretchY - 1f) * size.height / 2

                    // 리프레시 중 진동
                    if (isRefreshing) {
                        translationX = sin(frameTime * 0.05f) * 3f
                        rotationZ = sin(frameTime * 0.02f) * 2f
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRobotCharacter(
                    progress = progress,
                    isRefreshing = isRefreshing,
                    time = frameTime
                )
            }
        }

        // 상태 텍스트
        Text(
            text = when {
                isRefreshing -> "🤖 처리중... ${((frameTime / 20) % 100)}%"
                progress >= 1f -> "🔋 충전 완료! 놓으세요"
                progress > 0f -> "⚡ 충전중... (${(progress * 100).toInt()}%)"
                else -> ""
            },
            fontSize = 12.sp,
            color = Color(0xFF2196F3),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = characterTopPadding + characterSize + 15.dp + (pullOffset.value * 0.25f).dp)
        )

        // 컨텐츠 영역
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .align(Alignment.BottomCenter)
                .offset { IntOffset(0, pullOffset.value.roundToInt()) }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                if (pullOffset.value >= threshold && !isRefreshing) {
                                    isRefreshing = true
                                    pullOffset.animateTo(threshold, spring(stiffness = Spring.StiffnessMedium))
                                    delay(2000)
                                    isRefreshing = false
                                }
                                pullOffset.animateTo(
                                    0f,
                                    spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }
                        },
                        onVerticalDrag = { _, dragAmount ->
                            if (!isRefreshing) {
                                scope.launch {
                                    val resistance = 1f / (1f + pullOffset.value * 0.003f)
                                    pullOffset.snapTo(
                                        (pullOffset.value + dragAmount * resistance).coerceIn(0f, maxPull)
                                    )
                                }
                            }
                        }
                    )
                },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFE0E0E0))
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "로봇 캐릭터",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "당기면 안테나가 충전돼요!",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

private fun DrawScope.drawRobotCharacter(
    progress: Float,
    isRefreshing: Boolean,
    time: Long
) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val headSize = size.minDimension * 0.55f

    val bodyColor = Color(0xFF90CAF9)
    val metalColor = Color(0xFF64B5F6)
    val darkColor = Color(0xFF1976D2)

    // 안테나 (당길수록 길어짐)
    val antennaHeight = headSize * 0.35f * (1f + progress * 0.6f)
    val antennaWidth = 4f

    drawLine(
        color = darkColor,
        start = Offset(centerX, centerY - headSize / 2),
        end = Offset(centerX, centerY - headSize / 2 - antennaHeight),
        strokeWidth = antennaWidth,
        cap = StrokeCap.Round
    )

    // 안테나 끝 (리프레시 중 깜빡임)
    val antennaGlowColor = when {
        isRefreshing -> {
            val phase = (time / 150) % 3
            when (phase) {
                0L -> Color.Red
                1L -> Color.Yellow
                else -> Color.Green
            }
        }
        progress > 0.8f -> Color.Green
        progress > 0.5f -> Color.Yellow
        else -> Color.Red
    }

    val antennaGlowSize = headSize * 0.1f * (1f + if (isRefreshing) sin(time * 0.02f) * 0.3f else 0f)

    // 안테나 글로우 효과
    drawCircle(
        color = antennaGlowColor.copy(alpha = 0.3f),
        radius = antennaGlowSize * 1.5f,
        center = Offset(centerX, centerY - headSize / 2 - antennaHeight)
    )
    drawCircle(
        color = antennaGlowColor,
        radius = antennaGlowSize,
        center = Offset(centerX, centerY - headSize / 2 - antennaHeight)
    )

    // 머리 (사각형)
    drawRoundRect(
        color = bodyColor,
        topLeft = Offset(centerX - headSize / 2, centerY - headSize / 2),
        size = Size(headSize, headSize),
        cornerRadius = CornerRadius(headSize * 0.12f)
    )

    // 얼굴 패널
    drawRoundRect(
        color = Color(0xFF1E88E5),
        topLeft = Offset(centerX - headSize * 0.38f, centerY - headSize * 0.32f),
        size = Size(headSize * 0.76f, headSize * 0.55f),
        cornerRadius = CornerRadius(headSize * 0.06f)
    )

    // 눈 (LED 스타일)
    val eyeY = centerY - headSize * 0.12f
    val eyeSpacing = headSize * 0.18f
    val eyeWidth = headSize * 0.14f
    val eyeHeight = headSize * 0.1f

    val eyeColor = when {
        isRefreshing -> {
            val phase = (time / 80) % 4
            when (phase) {
                0L -> Color.Cyan
                1L -> Color.Green
                2L -> Color.Yellow
                else -> Color.Magenta
            }
        }
        progress > 0.5f -> Color.Cyan
        else -> Color(0xFF4FC3F7)
    }

    // 눈 스캔라인 효과 (리프레시 중)
    val scanOffset = if (isRefreshing) {
        ((time * 0.01f) % 1f) * eyeHeight * 2 - eyeHeight
    } else 0f

    // 왼쪽 눈
    drawRoundRect(
        color = eyeColor,
        topLeft = Offset(centerX - eyeSpacing - eyeWidth / 2, eyeY - eyeHeight / 2),
        size = Size(eyeWidth, eyeHeight),
        cornerRadius = CornerRadius(3f)
    )

    // 오른쪽 눈
    drawRoundRect(
        color = eyeColor,
        topLeft = Offset(centerX + eyeSpacing - eyeWidth / 2, eyeY - eyeHeight / 2),
        size = Size(eyeWidth, eyeHeight),
        cornerRadius = CornerRadius(3f)
    )

    // 입 (진행 바 스타일)
    val mouthY = centerY + headSize * 0.12f
    val mouthWidth = headSize * 0.45f
    val mouthHeight = headSize * 0.07f

    // 입 배경
    drawRoundRect(
        color = Color(0xFF0D47A1),
        topLeft = Offset(centerX - mouthWidth / 2, mouthY),
        size = Size(mouthWidth, mouthHeight),
        cornerRadius = CornerRadius(mouthHeight / 2)
    )

    // 입 진행 표시
    val mouthProgress = when {
        isRefreshing -> ((time * 0.001f) % 1f)
        else -> progress.coerceAtMost(1f)
    }

    val progressColor = when {
        mouthProgress > 0.8f -> Color.Green
        mouthProgress > 0.5f -> Color.Yellow
        else -> Color(0xFF4FC3F7)
    }

    drawRoundRect(
        color = progressColor,
        topLeft = Offset(centerX - mouthWidth / 2, mouthY),
        size = Size(mouthWidth * mouthProgress, mouthHeight),
        cornerRadius = CornerRadius(mouthHeight / 2)
    )

    // 볼트 장식
    val boltSize = headSize * 0.07f
    val boltY = centerY

    // 왼쪽 볼트
    drawCircle(
        color = metalColor,
        radius = boltSize,
        center = Offset(centerX - headSize / 2 + boltSize * 1.5f, boltY)
    )
    drawCircle(
        color = darkColor,
        radius = boltSize * 0.4f,
        center = Offset(centerX - headSize / 2 + boltSize * 1.5f, boltY)
    )

    // 오른쪽 볼트
    drawCircle(
        color = metalColor,
        radius = boltSize,
        center = Offset(centerX + headSize / 2 - boltSize * 1.5f, boltY)
    )
    drawCircle(
        color = darkColor,
        radius = boltSize * 0.4f,
        center = Offset(centerX + headSize / 2 - boltSize * 1.5f, boltY)
    )

    // 귀 안테나 (작은 것)
    val smallAntennaHeight = headSize * 0.15f
    listOf(-1f, 1f).forEach { side ->
        drawLine(
            color = darkColor,
            start = Offset(centerX + side * headSize * 0.35f, centerY - headSize / 2),
            end = Offset(centerX + side * headSize * 0.4f, centerY - headSize / 2 - smallAntennaHeight),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )
        drawCircle(
            color = metalColor,
            radius = headSize * 0.04f,
            center = Offset(centerX + side * headSize * 0.4f, centerY - headSize / 2 - smallAntennaHeight)
        )
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun PullToRefreshCharacterDemo() {
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
            text = "Pull-to-Refresh 캐릭터",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "슬라임 캐릭터") {
            SlimeCharacterRefresh()
        }

        DemoSection(title = "고양이 캐릭터") {
            CatCharacterRefresh()
        }

        DemoSection(title = "로봇 캐릭터") {
            RobotCharacterRefresh()
        }

        PullToRefreshGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PullToRefreshGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Pull-to-Refresh 캐릭터 가이드")

            CodeSection(
                title = "캐릭터 변형",
                code = """
// 당김 진행률에 따른 변형
graphicsLayer {
    scaleY = 1f + progress * 0.5f  // 세로 늘어남
    scaleX = 1f - progress * 0.15f // 가로 줄어듦
}
                """.trimIndent()
            )

            CodeSection(
                title = "캐릭터 표정 변화",
                code = """
// 눈 크기
val eyeSize = baseSize * (1f + progress * 0.5f)

// 입 모양
if (progress > 0.5f) {
    // 놀란 입 (O 모양)
} else {
    // 미소
}
                """.trimIndent()
            )

            FeatureSection(
                features = """
- graphicsLayer로 전체 변형
- Canvas로 세부 표정 변화
- 리프레시 중 특별 애니메이션
- 볼터치/하이라이트로 귀여움 추가
                """.trimIndent(),
                type = FeatureTextType.TIP
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1400)
@Composable
private fun PullToRefreshCharacterDemoPreview() {
    PullToRefreshCharacterDemo()
}
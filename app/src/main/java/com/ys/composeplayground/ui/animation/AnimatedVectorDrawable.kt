package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 📱 실무 애니메이션 #2: Animated Vector Drawable
 *
 * 📖 핵심 개념
 *
 * Animated Vector Drawable(AVD)은 Android 네이티브 벡터 애니메이션입니다.
 * Compose에서는 Canvas와 PathMeasure를 활용하여 XML 없이 동일한 효과를 구현할 수 있습니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * PathMeasure | 경로 길이 측정 및 부분 추출
 * Path | 벡터 경로 정의
 * Canvas | 그래픽 렌더링
 * Animatable | 진행률 애니메이션
 *
 * 💡 AVD vs Lottie
 *
 * | 항목 | AVD | Lottie |
 * |------|-----|--------|
 * | 복잡도 | 단순한 경로 애니메이션 | 복잡한 애니메이션 |
 * | 성능 | RenderThread (효율적) | 메인 스레드 |
 * | 파일 크기 | 매우 작음 | 상대적으로 큼 |
 *
 * 학습 목표:
 * 1. PathMeasure로 경로 애니메이션
 * 2. 아이콘 상태 전환 (체크, 메뉴 ↔ 화살표)
 * 3. Compose Canvas로 AVD 효과 재현
 */

// ============================================
// 1. 체크마크 애니메이션 (경로 그리기)
// ============================================

/**
 * 체크마크가 그려지는 애니메이션
 *
 * PathMeasure를 사용하여 경로의 일부분만 그리는 방식
 */
@Composable
fun AnimatedCheckmark(
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF00B894),
    strokeWidth: Float = 8f
) {
    val progress by animateFloatAsState(
        targetValue = if (isChecked) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "checkProgress"
    )

    Canvas(modifier = modifier.size(80.dp)) {
        val width = size.width
        val height = size.height

        // 체크마크 경로 정의
        val checkPath = Path().apply {
            moveTo(width * 0.2f, height * 0.5f)
            lineTo(width * 0.4f, height * 0.7f)
            lineTo(width * 0.8f, height * 0.3f)
        }

        // PathMeasure로 경로 길이 측정
        val pathMeasure = PathMeasure()
        pathMeasure.setPath(path = checkPath, forceClosed = false)

        // 진행률에 따라 부분 경로 추출
        val animatedPath = Path()
        pathMeasure.getSegment(
            startDistance = 0f,
            stopDistance = pathMeasure.length * progress,
            destination = animatedPath,
            startWithMoveTo = true
        )

        // 경로 그리기
        drawPath(
            path = animatedPath,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

/**
 * 원형 배경 + 체크마크 조합
 */
@Composable
fun CircleCheckAnimation(modifier: Modifier = Modifier) {
    var isChecked by remember { mutableStateOf(false) }

    val circleProgress by animateFloatAsState(
        targetValue = if (isChecked) 1f else 0f,
        animationSpec = tween(300),
        label = "circleProgress"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { isChecked = !isChecked },
            contentAlignment = Alignment.Center
        ) {
            // 원형 배경
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color(0xFF00B894).copy(alpha = circleProgress * 0.2f),
                    radius = size.minDimension / 2
                )
                drawCircle(
                    color = Color(0xFF00B894),
                    radius = size.minDimension / 2,
                    style = Stroke(width = 4f)
                )
            }

            // 체크마크
            AnimatedCheckmark(
                isChecked = isChecked,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isChecked) "완료!" else "클릭하여 체크",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

// ============================================
// 2. 햄버거 메뉴 ↔ 화살표 전환
// ============================================

/**
 * 햄버거 메뉴 ↔ 뒤로가기 화살표 전환
 *
 * 세 개의 선이 회전하고 변형되는 모핑 애니메이션
 */
@Composable
fun HamburgerToArrow(
    isArrow: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    strokeWidth: Float = 6f
) {
    val progress by animateFloatAsState(
        targetValue = if (isArrow) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "morphProgress"
    )

    Canvas(modifier = modifier.size(48.dp)) {
        val width = size.width
        val height = size.height
        val lineLength = width * 0.6f
        val startX = (width - lineLength) / 2
        val endX = startX + lineLength
        val centerY = height / 2

        // 중간 선 (회전)
        rotate(degrees = progress * 180f, pivot = Offset(width / 2, centerY)) {
            drawLine(
                color = color,
                start = Offset(startX, centerY),
                end = Offset(endX, centerY),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }

        // 상단 선 → 화살표 위쪽
        val topStartY = height * 0.3f
        val topEndY = topStartY + (centerY - topStartY) * progress
        val topEndX = endX - (endX - width / 2) * progress
        val topRotation = progress * 45f

        rotate(degrees = topRotation, pivot = Offset(startX, topEndY)) {
            drawLine(
                color = color,
                start = Offset(startX, topEndY),
                end = Offset(
                    startX + lineLength * (1 - progress * 0.5f),
                    topEndY
                ),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }

        // 하단 선 → 화살표 아래쪽
        val bottomStartY = height * 0.7f
        val bottomEndY = bottomStartY - (bottomStartY - centerY) * progress
        val bottomRotation = -progress * 45f

        rotate(degrees = bottomRotation, pivot = Offset(startX, bottomEndY)) {
            drawLine(
                color = color,
                start = Offset(startX, bottomEndY),
                end = Offset(
                    startX + lineLength * (1 - progress * 0.5f),
                    bottomEndY
                ),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun HamburgerMenuDemo(modifier: Modifier = Modifier) {
    var isArrow by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2D3436))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFF6C5CE7))
                .clickable { isArrow = !isArrow },
            contentAlignment = Alignment.Center
        ) {
            HamburgerToArrow(
                isArrow = isArrow,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isArrow) "뒤로가기" else "메뉴",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

// ============================================
// 3. 플러스 ↔ X 전환
// ============================================

/**
 * + ↔ × 전환 애니메이션
 *
 * FAB (Floating Action Button) 확장 시 많이 사용
 */
@Composable
fun PlusToClose(
    isClose: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    strokeWidth: Float = 6f
) {
    val rotation by animateFloatAsState(
        targetValue = if (isClose) 45f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "plusRotation"
    )

    Canvas(modifier = modifier.size(48.dp)) {
        val width = size.width
        val height = size.height
        val lineLength = width * 0.5f
        val center = Offset(width / 2, height / 2)

        rotate(degrees = rotation, pivot = center) {
            // 가로 선
            drawLine(
                color = color,
                start = Offset(center.x - lineLength / 2, center.y),
                end = Offset(center.x + lineLength / 2, center.y),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )

            // 세로 선
            drawLine(
                color = color,
                start = Offset(center.x, center.y - lineLength / 2),
                end = Offset(center.x, center.y + lineLength / 2),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun PlusToCloseDemo(modifier: Modifier = Modifier) {
    var isClose by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F0F23))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    if (isClose) Color(0xFFFF6B6B) else Color(0xFF00B894)
                )
                .clickable { isClose = !isClose },
            contentAlignment = Alignment.Center
        ) {
            PlusToClose(
                isClose = isClose,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isClose) "닫기" else "추가",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

// ============================================
// 4. 재생 ↔ 일시정지 전환
// ============================================

/**
 * ▶ ↔ ⏸ 전환 애니메이션
 *
 * 미디어 플레이어 컨트롤에 사용
 */
@Composable
fun PlayToPause(
    isPaused: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    val progress by animateFloatAsState(
        targetValue = if (isPaused) 1f else 0f,
        animationSpec = tween(300),
        label = "playPauseProgress"
    )

    Canvas(modifier = modifier.size(48.dp)) {
        val width = size.width
        val height = size.height
        val padding = width * 0.2f

        // 재생 삼각형 → 일시정지 두 막대로 모핑
        val leftPath = Path().apply {
            // 왼쪽: 삼각형 왼쪽 → 막대
            val leftX = padding + (width * 0.15f) * progress
            val topY = padding
            val bottomY = height - padding
            val rightX = if (progress < 0.5f) {
                padding + (width * 0.15f)
            } else {
                padding + (width * 0.15f)
            }

            moveTo(leftX, topY)
            lineTo(leftX + width * 0.15f, topY)
            lineTo(leftX + width * 0.15f, bottomY)
            lineTo(leftX, bottomY)
            close()
        }

        val rightPath = Path().apply {
            // 오른쪽: 삼각형 오른쪽 → 막대
            val leftX = width - padding - width * 0.15f - (width * 0.2f) * (1 - progress)
            val rightX = width - padding
            val topY = padding + (height * 0.15f) * (1 - progress)
            val bottomY = height - padding - (height * 0.15f) * (1 - progress)

            if (progress < 0.5f) {
                // 삼각형 모양 (재생)
                moveTo(padding, padding)
                lineTo(width - padding, height / 2)
                lineTo(padding, height - padding)
                close()
            } else {
                // 두 막대 모양 (일시정지)
                // 왼쪽 막대
                moveTo(padding, padding)
                lineTo(padding + width * 0.25f, padding)
                lineTo(padding + width * 0.25f, height - padding)
                lineTo(padding, height - padding)
                close()
            }
        }

        if (progress < 0.5f) {
            // 재생 삼각형
            val trianglePath = Path().apply {
                moveTo(padding, padding)
                lineTo(width - padding, height / 2)
                lineTo(padding, height - padding)
                close()
            }
            drawPath(path = trianglePath, color = color)
        } else {
            // 일시정지 막대 2개
            val barWidth = width * 0.2f
            val gap = width * 0.15f

            // 왼쪽 막대
            drawRect(
                color = color,
                topLeft = Offset(padding + gap, padding),
                size = Size(barWidth, height - padding * 2)
            )

            // 오른쪽 막대
            drawRect(
                color = color,
                topLeft = Offset(width - padding - gap - barWidth, padding),
                size = Size(barWidth, height - padding * 2)
            )
        }
    }
}

@Composable
fun PlayPauseDemo(modifier: Modifier = Modifier) {
    var isPaused by remember { mutableStateOf(false) }

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
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFF74B9FF))
                .clickable { isPaused = !isPaused },
            contentAlignment = Alignment.Center
        ) {
            PlayToPause(
                isPaused = isPaused,
                modifier = Modifier.size(40.dp),
                color = Color(0xFF2D3436)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isPaused) "재생" else "일시정지",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

// ============================================
// 5. 원형 로딩 인디케이터 (Path 애니메이션)
// ============================================

/**
 * 원형 로딩 인디케이터
 *
 * PathMeasure로 호(arc)의 시작점과 끝점을 애니메이션
 */
@Composable
fun CircularLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF6C5CE7),
    strokeWidth: Float = 8f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 30f,
        targetValue = 270f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sweep"
    )

    Canvas(modifier = modifier.size(60.dp)) {
        val diameter = size.minDimension - strokeWidth
        val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

        rotate(degrees = rotation) {
            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}

@Composable
fun LoadingIndicatorDemo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2C3E50))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CircularLoadingIndicator(color = Color(0xFF6C5CE7))
            CircularLoadingIndicator(color = Color(0xFF00B894))
            CircularLoadingIndicator(color = Color(0xFFFF6B6B))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "로딩 중...",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

// ============================================
// 6. 검색 아이콘 → X 전환
// ============================================

/**
 * 🔍 → × 전환
 *
 * 검색창 활성화 시 많이 사용
 */
@Composable
fun SearchToClose(
    isClose: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    strokeWidth: Float = 5f
) {
    val progress by animateFloatAsState(
        targetValue = if (isClose) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "searchProgress"
    )

    Canvas(modifier = modifier.size(48.dp)) {
        val width = size.width
        val height = size.height
        val center = Offset(width / 2, height / 2)

        // 돋보기 원 → X의 일부로 변환
        val circleRadius = width * 0.25f * (1 - progress)
        val circleCenter = Offset(
            width * 0.4f + (center.x - width * 0.4f) * progress,
            height * 0.4f + (center.y - height * 0.4f) * progress
        )

        if (progress < 0.8f) {
            drawCircle(
                color = color,
                radius = circleRadius,
                center = circleCenter,
                style = Stroke(width = strokeWidth)
            )
        }

        // 돋보기 손잡이 → X의 한 선으로 변환
        val handleStart = Offset(
            circleCenter.x + circleRadius * 0.7f,
            circleCenter.y + circleRadius * 0.7f
        )
        val handleEnd = Offset(
            width * 0.75f + (width * 0.8f - width * 0.75f) * progress,
            height * 0.75f + (height * 0.8f - height * 0.75f) * progress
        )

        // X의 첫 번째 선 (돋보기 손잡이에서 변환)
        val line1Start = Offset(
            width * 0.2f + (handleStart.x - width * 0.2f) * (1 - progress),
            height * 0.2f + (handleStart.y - height * 0.2f) * (1 - progress)
        )

        drawLine(
            color = color,
            start = line1Start,
            end = handleEnd,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // X의 두 번째 선 (progress에 따라 나타남)
        if (progress > 0.3f) {
            val line2Alpha = ((progress - 0.3f) / 0.7f).coerceIn(0f, 1f)
            drawLine(
                color = color.copy(alpha = line2Alpha),
                start = Offset(width * 0.8f, height * 0.2f),
                end = Offset(width * 0.2f, height * 0.8f),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun SearchToCloseDemo(modifier: Modifier = Modifier) {
    var isClose by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF34495E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFE74C3C))
                .clickable { isClose = !isClose },
            contentAlignment = Alignment.Center
        ) {
            SearchToClose(
                isClose = isClose,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isClose) "닫기" else "검색",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

// ============================================
// 7. 경로 그리기 애니메이션 (별)
// ============================================

/**
 * 별 모양 경로 그리기 애니메이션
 */
@Composable
fun AnimatedStarPath(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }
    var isDrawing by remember { mutableStateOf(false) }

    LaunchedEffect(isDrawing) {
        if (isDrawing) {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(1500)
            )
        } else {
            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(500)
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(
            modifier = Modifier
                .size(120.dp)
                .clickable { isDrawing = !isDrawing }
        ) {
            val width = size.width
            val height = size.height
            val centerX = width / 2
            val centerY = height / 2
            val outerRadius = width * 0.45f
            val innerRadius = width * 0.2f

            // 별 경로 생성
            val starPath = Path().apply {
                val points = 5
                for (i in 0 until points * 2) {
                    val radius = if (i % 2 == 0) outerRadius else innerRadius
                    val angle = (i * 360f / (points * 2) - 90f) * PI.toFloat() / 180f
                    val x = centerX + radius * cos(angle)
                    val y = centerY + radius * sin(angle)

                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                }
                close()
            }

            // PathMeasure로 부분 경로 추출
            val pathMeasure = PathMeasure()
            pathMeasure.setPath(starPath, true)

            val animatedPath = Path()
            pathMeasure.getSegment(
                startDistance = 0f,
                stopDistance = pathMeasure.length * progress.value,
                destination = animatedPath,
                startWithMoveTo = true
            )

            // 배경 별 (희미하게)
            drawPath(
                path = starPath,
                color = Color(0xFFFFE66D).copy(alpha = 0.1f),
                style = Stroke(width = 4f)
            )

            // 애니메이션 별
            drawPath(
                path = animatedPath,
                color = Color(0xFFFFE66D),
                style = Stroke(
                    width = 6f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { isDrawing = !isDrawing },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFE66D)
            )
        ) {
            Text(
                text = if (isDrawing) "지우기" else "그리기",
                color = Color(0xFF1A1A2E),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================
// 8. 화살표 방향 전환
// ============================================

/**
 * 화살표 방향 전환 (↑ ↔ ↓)
 *
 * 확장/축소 UI에 사용
 */
@Composable
fun ArrowDirectionToggle(
    isDown: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    strokeWidth: Float = 6f
) {
    val rotation by animateFloatAsState(
        targetValue = if (isDown) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "arrowRotation"
    )

    Canvas(modifier = modifier.size(48.dp)) {
        val width = size.width
        val height = size.height
        val center = Offset(width / 2, height / 2)

        rotate(degrees = rotation, pivot = center) {
            // 화살표 (^) 모양
            val arrowPath = Path().apply {
                moveTo(width * 0.25f, height * 0.6f)
                lineTo(width * 0.5f, height * 0.35f)
                lineTo(width * 0.75f, height * 0.6f)
            }

            drawPath(
                path = arrowPath,
                color = color,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

@Composable
fun ArrowToggleDemo(modifier: Modifier = Modifier) {
    var isDown by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2D3436))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFF00CEC9))
                .clickable { isDown = !isDown },
            contentAlignment = Alignment.Center
        ) {
            ArrowDirectionToggle(
                isDown = isDown,
                modifier = Modifier.size(32.dp),
                color = Color(0xFF2D3436)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isDown) "접기" else "펼치기",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun AnimatedVectorDrawableDemo() {
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
            text = "Animated Vector Drawable",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "1. 체크마크 애니메이션") {
            CircleCheckAnimation()
        }

        DemoSection(title = "2. 햄버거 → 화살표") {
            HamburgerMenuDemo()
        }

        DemoSection(title = "3. 플러스 → X") {
            PlusToCloseDemo()
        }

        DemoSection(title = "4. 재생 ↔ 일시정지") {
            PlayPauseDemo()
        }

        DemoSection(title = "5. 로딩 인디케이터") {
            LoadingIndicatorDemo()
        }

        DemoSection(title = "6. 검색 → X") {
            SearchToCloseDemo()
        }

        DemoSection(title = "7. 별 경로 그리기") {
            AnimatedStarPath()
        }

        DemoSection(title = "8. 화살표 방향 전환") {
            ArrowToggleDemo()
        }

        AnimatedVectorGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun AnimatedVectorGuide() {
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
                text = "📚 AVD 스타일 애니메이션 가이드",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = """
                    핵심 기법:

                    • PathMeasure 경로 애니메이션
                      val pathMeasure = PathMeasure()
                      pathMeasure.setPath(path, false)
                      pathMeasure.getSegment(0f, length * progress, dest, true)

                    • 아이콘 모핑
                      animateFloatAsState로 progress 제어
                      progress에 따라 Path 변형

                    • 회전 애니메이션
                      rotate(degrees, pivot) { draw... }

                    💡 AVD vs Compose Canvas:
                    • 기존 AVD XML 있음 → AnimatedImageVector
                    • 새로 만들기 → Compose Canvas
                    • 복잡한 애니메이션 → Lottie

                    🔧 제작 도구:
                    • Shape Shifter (shapeshifter.design)
                    • Android Studio Vector Asset
                """.trimIndent(),
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 2200)
@Composable
private fun AnimatedVectorDrawableDemoPreview() {
    AnimatedVectorDrawableDemo()
}
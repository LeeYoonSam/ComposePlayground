package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * 🟠 Advanced #12: Fling with Decay (관성 스크롤) 애니메이션
 *
 * 📖 핵심 개념
 *
 * splineBasedDecay 또는 exponentialDecay를 사용하여 초기 속도(velocity)를 기반으로 점진적으로 감속하는 애니메이션을 적용합니다. 스크롤, 카루셀 등에서 자연스러운 관성 효과를 만들어요.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * Animatable | 위치/속도 관리
 * splineBasedDecay | Android 기본 감속 곡선
 * exponentialDecay | 지수 감속 (friction 조절)
 * animateDecay | 감속 애니메이션 실행
 * velocity | 드래그 종료 시 속도
 *
 * 💡 동작 원리
 *
 * ```
 * [드래그 중] offset 업데이트
 *        ↓ onDragEnd(velocity)
 * [손 뗌] velocity를 기반으로 animateDecay
 *        ↓ Decay 공식 적용
 * [감속] 점점 느려지며 이동
 *        ↓ velocity ≈ 0
 * [정지]
 *
 * Decay: v(t) = v0 * e^(-friction * t)
 * ```
 *
 * 학습 목표:
 * 1. splineBasedDecay 사용법
 * 2. exponentialDecay와 friction 조절
 * 3. velocity 기반 관성 애니메이션
 * 4. 바운더리 처리
 */

// ============================================
// 기본 수평 Fling
// ============================================
@Composable
fun HorizontalFlingBasic(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val density = LocalDensity.current
    val decay = remember { splineBasedDecay<Float>(density) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.CenterStart
    ) {
        // 트랙
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .padding(horizontal = 40.dp)
                .background(Color(0xFF90CAF9))
        )

        // 드래그 가능한 공
        Box(
            modifier = Modifier
                .padding(start = 20.dp)
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .size(60.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFF2196F3))
                .pointerInput(Unit) {
                    val velocityTracker = VelocityTracker()

                    detectHorizontalDragGestures(
                        onDragStart = { velocityTracker.resetTracking() },
                        onDragEnd = {
                            val velocity = velocityTracker.calculateVelocity().x
                            scope.launch {
                                offsetX.animateDecay(
                                    initialVelocity = velocity,
                                    animationSpec = decay
                                )
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            velocityTracker.addPosition(
                                change.uptimeMillis,
                                change.position
                            )
                            scope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = null,
                tint = Color.White
            )
        }

        // 속도/위치 표시
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Text(
                "Offset: ${offsetX.value.roundToInt()}",
                fontSize = 10.sp,
                color = Color(0xFF1976D2)
            )
        }
    }
}

// ============================================
// 바운더리가 있는 Fling
// ============================================
@Composable
fun BoundedFling(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val density = LocalDensity.current
    val decay = remember { splineBasedDecay<Float>(density) }

    val minBoundDp = 0.dp
    val maxBoundDp = 180.dp
    val ballSize = 50.dp
    val startPadding = 20.dp

    // dp를 픽셀로 변환하여 bounds 설정
    val minBoundPx = with(density) { minBoundDp.toPx() }
    val maxBoundPx = with(density) { maxBoundDp.toPx() }

    // 바운더리 설정
    LaunchedEffect(Unit) {
        offsetX.updateBounds(lowerBound = minBoundPx, upperBound = maxBoundPx)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFCE4EC)),
        contentAlignment = Alignment.CenterStart
    ) {
        // 왼쪽 바 (시작 위치)
        Box(
            modifier = Modifier
                .padding(start = startPadding)
                .width(4.dp)
                .height(60.dp)
                .background(Color(0xFFE91E63))
        )

        // 오른쪽 바 (끝 위치: 시작 + maxBound + 공 크기)
        Box(
            modifier = Modifier
                .padding(start = startPadding + maxBoundDp + ballSize)
                .width(4.dp)
                .height(60.dp)
                .background(Color(0xFFE91E63))
        )


        // 드래그 가능한 공
        Box(
            modifier = Modifier
                .padding(start = startPadding)
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .size(ballSize)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFFE91E63))
                .pointerInput(Unit) {
                    val velocityTracker = VelocityTracker()

                    detectHorizontalDragGestures(
                        onDragStart = { velocityTracker.resetTracking() },
                        onDragEnd = {
                            val velocity = velocityTracker.calculateVelocity().x
                            scope.launch {
                                // 바운더리 내에서만 움직임
                                offsetX.animateDecay(
                                    initialVelocity = velocity,
                                    animationSpec = decay
                                )
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            velocityTracker.addPosition(
                                change.uptimeMillis,
                                change.position
                            )
                            scope.launch {
                                val newValue = (offsetX.value + dragAmount)
                                    .coerceIn(minBoundPx, maxBoundPx)
                                offsetX.snapTo(newValue)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text("⚾", fontSize = 24.sp)
        }

        Text(
            "Bounded: 0 ~ 250",
            fontSize = 10.sp,
            color = Color(0xFFC2185B),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

// ============================================
// Friction 비교
// ============================================
@Composable
fun FrictionComparison(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    data class FrictionConfig(
        val label: String,
        val friction: Float,
        val color: Color,
        val offset: Animatable<Float, AnimationVector1D>
    )

    val configs = remember {
        listOf(
            FrictionConfig("Low\n0.5x", 0.5f, Color(0xFF4CAF50), Animatable(0f)),
            FrictionConfig("Normal\n1.0x", 1.0f, Color(0xFFFF9800), Animatable(0f)),
            FrictionConfig("High\n2.0x", 2.0f, Color(0xFFF44336), Animatable(0f))
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "← 같은 속도로 플링해보세요 →",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        configs.forEach { config ->
            val decay = remember(config.friction) {
                exponentialDecay<Float>(
                    frictionMultiplier = config.friction,
                    absVelocityThreshold = 0.1f
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = config.label,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.width(50.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp
                )

                Box(
                    modifier = Modifier
                        .padding(start = 25.dp)
                        .offset { IntOffset(config.offset.value.roundToInt(), 0) }
                        .size(40.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(config.color)
                        .pointerInput(config.friction) {
                            val velocityTracker = VelocityTracker()

                            detectHorizontalDragGestures(
                                onDragStart = { velocityTracker.resetTracking() },
                                onDragEnd = {
                                    val velocity = velocityTracker.calculateVelocity().x
                                    scope.launch {
                                        config.offset.animateDecay(
                                            initialVelocity = velocity,
                                            animationSpec = decay
                                        )
                                    }
                                },
                                onHorizontalDrag = { change, dragAmount ->
                                    velocityTracker.addPosition(
                                        timeMillis = change.uptimeMillis,
                                        position = change.position
                                    )
                                    scope.launch {
                                        config.offset.snapTo(config.offset.value + dragAmount)
                                    }
                                }
                            )
                        }
                )
            }

            // 리셋 버튼
            IconButton(
                onClick = {
                    scope.launch {
                        config.offset.animateTo(0f)
                    }
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset",
                    tint = config.color,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// ============================================
// 2D Fling
// ============================================
@Composable
fun TwoDimensionalFling(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val density = LocalDensity.current
    val decayX = remember { splineBasedDecay<Float>(density) }
    val decayY = remember { splineBasedDecay<Float>(density) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE8F5E9)),
        contentAlignment = Alignment.Center
    ) {
        // 격자 가이드
        for (i in -2..2) {
            Box(
                modifier = Modifier
                    .offset(x = (i * 50).dp)
                    .width(1.dp)
                    .height(200.dp)
                    .background(Color(0xFFA5D6A7))
            )
            Box(
                modifier = Modifier
                    .offset(y = (i * 40).dp)
                    .width(200.dp)
                    .height(1.dp)
                    .background(Color(0xFFA5D6A7))
            )
        }

        // 드래그 가능한 공
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .size(60.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))
                .pointerInput(Unit) {
                    val velocityTracker = VelocityTracker()

                    detectDragGestures(
                        onDragStart = { velocityTracker.resetTracking() },
                        onDragEnd = {
                            val velocity = velocityTracker.calculateVelocity()
                            scope.launch {
                                launch {
                                    offsetX.animateDecay(
                                        initialVelocity = velocity.x,
                                        animationSpec = decayX
                                    )
                                }
                                launch {
                                    offsetY.animateDecay(
                                        initialVelocity = velocity.y,
                                        animationSpec = decayY
                                    )
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            velocityTracker.addPosition(
                                change.uptimeMillis,
                                change.position
                            )
                            scope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount.x)
                                offsetY.snapTo(offsetY.value + dragAmount.y)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.OpenWith,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        // 리셋 버튼
        IconButton(
            onClick = {
                scope.launch {
                    launch { offsetX.animateTo(0f) }
                    launch { offsetY.animateTo(0f) }
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reset",
                tint = Color(0xFF4CAF50)
            )
        }
    }
}

// ============================================
// 페이지 스냅 Fling
// ============================================
@Composable
fun PageSnapFling(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }

    val density = LocalDensity.current
    val pageWidthDp = 260.dp
    val pageSpacingDp = 16.dp
    val pageCount = 5

    val pageWidthPx = with(density) { pageWidthDp.toPx() }
    val pageSpacingPx = with(density) { pageSpacingDp.toPx() }
    val totalPageWidthPx = pageWidthPx + pageSpacingPx

    var currentPage by remember { mutableIntStateOf(0) }

    val colors = listOf(
        Color(0xFF9C27B0),
        Color(0xFF673AB7),
        Color(0xFF3F51B5),
        Color(0xFF2196F3),
        Color(0xFF00BCD4)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 페이지 영역 - clipToBounds 하지 않음
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .pointerInput(Unit) {
                    val velocityTracker = VelocityTracker()

                    detectHorizontalDragGestures(
                        onDragStart = { velocityTracker.resetTracking() },
                        onDragEnd = {
                            val velocity = velocityTracker.calculateVelocity().x

                            scope.launch {
                                val projectedOffset = offsetX.value + velocity * 0.2f
                                val targetPage = (-projectedOffset / totalPageWidthPx)
                                    .roundToInt()
                                    .coerceIn(0, pageCount - 1)

                                currentPage = targetPage

                                offsetX.animateTo(
                                    targetValue = -targetPage * totalPageWidthPx,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            velocityTracker.addPosition(
                                change.uptimeMillis,
                                change.position
                            )
                            scope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.CenterStart
        ) {
            // 각 카드를 개별적으로 배치
            repeat(pageCount) { index ->
                val cardOffsetPx = index * totalPageWidthPx + offsetX.value

                Card(
                    modifier = Modifier
                        .offset { IntOffset(cardOffsetPx.roundToInt(), 0) }
                        .width(pageWidthDp)
                        .height(140.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors[index])
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Page ${index + 1}",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 페이지 인디케이터
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(pageCount) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == currentPage) 10.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == currentPage) Color(0xFF9C27B0)
                            else Color(0xFFCE93D8)
                        )
                )
            }
        }
    }
}

// ============================================
// spline vs exponential 비교
// ============================================
@Composable
fun DecayTypeComparison(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val splineOffset = remember { Animatable(0f) }
    val exponentialOffset = remember { Animatable(0f) }

    val splineDecay = remember { splineBasedDecay<Float>(density) }
    val exponentialDecay = remember {
        exponentialDecay<Float>(frictionMultiplier = 1f)
    }

    // 진행 상황 추적
    var splineProgress by remember { mutableStateOf("대기") }
    var exponentialProgress by remember { mutableStateOf("대기") }
    var splineFinalDistance by remember { mutableFloatStateOf(0f) }
    var exponentialFinalDistance by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 속도 선택
        var selectedVelocity by remember { mutableIntStateOf(2000) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            listOf(1000, 2000, 5000).forEach { velocity ->
                FilterChip(
                    onClick = { selectedVelocity = velocity },
                    label = { Text("${velocity}") },
                    selected = selectedVelocity == velocity,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF607D8B)
                    )
                )
            }
        }

        // 발사 버튼
        Button(
            onClick = {
                scope.launch {
                    // 리셋
                    splineOffset.snapTo(0f)
                    exponentialOffset.snapTo(0f)
                    splineProgress = "이동 중..."
                    exponentialProgress = "이동 중..."

                    val velocity = selectedVelocity.toFloat()

                    // Spline
                    launch {
                        splineOffset.animateDecay(velocity, splineDecay)
                        splineFinalDistance = splineOffset.value
                        splineProgress = "완료: ${splineOffset.value.roundToInt()}px"
                    }

                    // Exponential
                    launch {
                        exponentialOffset.animateDecay(velocity, exponentialDecay)
                        exponentialFinalDistance = exponentialOffset.value
                        exponentialProgress = "완료: ${exponentialOffset.value.roundToInt()}px"
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF607D8B)),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("🚀 Launch (v=$selectedVelocity)")
        }

        // Spline 트랙
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Spline", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(splineProgress, fontSize = 10.sp, color = Color.Gray)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF607D8B).copy(alpha = 0.15f)),
                contentAlignment = Alignment.CenterStart
            ) {
                // 거리 눈금 (100px 단위)
                for (i in 1..5) {
                    Box(
                        modifier = Modifier
                            .padding(start = (i * 50).dp)
                            .width(1.dp)
                            .height(20.dp)
                            .background(Color(0xFF607D8B).copy(alpha = 0.3f))
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .offset { IntOffset(splineOffset.value.roundToInt(), 0) }
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF607D8B))
                )
            }
        }

        // Exponential 트랙
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Exponential", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(exponentialProgress, fontSize = 10.sp, color = Color.Gray)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF455A64).copy(alpha = 0.15f)),
                contentAlignment = Alignment.CenterStart
            ) {
                // 거리 눈금 (100px 단위)
                for (i in 1..5) {
                    Box(
                        modifier = Modifier
                            .padding(start = (i * 50).dp)
                            .width(1.dp)
                            .height(20.dp)
                            .background(Color(0xFF455A64).copy(alpha = 0.3f))
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .offset { IntOffset(exponentialOffset.value.roundToInt(), 0) }
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF455A64))
                )
            }
        }

        // 결과 비교
        if (splineFinalDistance > 0 && exponentialFinalDistance > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFECEFF1))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("📊 결과 비교", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(
                        "Spline: ${splineFinalDistance.roundToInt()}px",
                        fontSize = 11.sp,
                        color = Color(0xFF607D8B)
                    )
                    Text(
                        "Exponential: ${exponentialFinalDistance.roundToInt()}px",
                        fontSize = 11.sp,
                        color = Color(0xFF455A64)
                    )

                    val diff = splineFinalDistance - exponentialFinalDistance
                    Text(
                        "차이: ${abs(diff).roundToInt()}px (${if (diff > 0) "Spline" else "Exponential"}이 더 멀리)",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // 설명
        Text(
            """
            💡 차이점:
            • Spline: Android 기본, 초반에 속도 유지 후 급감속
            • Exponential: 처음부터 일정 비율로 감속
            • 높은 속도일수록 차이가 명확함
            """.trimIndent(),
            fontSize = 10.sp,
            color = Color.Gray,
            lineHeight = 14.sp
        )
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun FlingDecayDemo() {
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
            text = "Fling with Decay",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        // 기본 수평
        DemoSection(title = "기본 수평 Fling (splineBasedDecay)") {
            HorizontalFlingBasic()
        }

        // 바운더리
        DemoSection(title = "바운더리가 있는 Fling") {
            BoundedFling()
        }

        // Friction 비교
        DemoSection(title = "Friction 비교 (exponentialDecay)") {
            FrictionComparison()
        }

        // 2D
        DemoSection(title = "2D Fling") {
            TwoDimensionalFling()
        }

        // 페이지 스냅
        DemoSection(title = "페이지 스냅 Fling") {
            PageSnapFling()
        }

        // Decay 타입 비교
        DemoSection(title = "Spline vs Exponential Decay") {
            DecayTypeComparison()
        }

        // 가이드
        FlingGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FlingGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Fling with Decay 가이드")

            CodeSection(
                title = """
                    Decay 종류:

                    1. splineBasedDecay
                       - Android 기본 감속 곡선
                       - 자연스러운 스크롤 느낌    
                """.trimIndent(),
                code = "val decay = splineBasedDecay<Float>(density)"
            )

            CodeSection(
                title = """
                    2. exponentialDecay
                        - friction 조절 가능  
                """.trimIndent(),
                code = """
                    exponentialDecay<Float>(
                       frictionMultiplier = 1f,  // 높을수록 빨리 멈춤
                       absVelocityThreshold = 0.1f
                   )
                """.trimIndent()
            )

            CodeSection(
                title = "사용법:",
                code = """
                    val velocityTracker = VelocityTracker()
                
                    onDragEnd = {
                        val velocity = velocityTracker.calculateVelocity()
                        offset.animateDecay(velocity.x, decay)
                    }
                """.trimIndent()
            )

            FeatureSection(
                features = """
                    • updateBounds()로 범위 제한
                    • 페이지 스냅은 velocity로 방향 판단
                    • 2D는 X, Y 각각 animateDecay
                """.trimIndent(),
                type = FeatureTextType.TIP
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
fun FlingDecayDemoPreview() {
    FlingDecayDemo()
}

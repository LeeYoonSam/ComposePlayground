package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * 🟠 Advanced #11: Elastic Drag (탄성 드래그) 애니메이션
 *
 * 📖 핵심 개념
 *
 * 드래그 거리에 **저항(resistance)** 을 적용하여 멀리 드래그할수록 점점 느려지게 만들고, 놓으면 spring으로 원위치로 튕겨 돌아옵니다. 고무줄을 당기는 느낌이에요!
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * Animatable | 드래그 위치 관리
 * detectDragGestures | 드래그 감지
 * detectVerticalDragGestures | 수직 드래그 감지
 * spring | 탄성 복귀 애니메이션
 * Resistance 공식 | 드래그 저항 계산
 *
 * 💡 동작 원리
 *
 * ```
 * [드래그 중]
 * actualOffset = dragAmount * resistance
 * resistance = 1 / (1 + abs(currentOffset) * factor)
 *
 * → 멀리 드래그할수록 resistance 감소
 * → 이동 거리가 점점 줄어듦
 *
 * [놓음]
 * animateTo(0f, spring(HighBouncy, Low))
 * → 탄성있게 원위치로 복귀
 * ```
 *
 * 학습 목표:
 * 1. 드래그에 저항(resistance) 적용
 * 2. 멀리 갈수록 느려지는 효과
 * 3. spring으로 탄성 복귀
 * 4. 다양한 방향의 elastic 효과
 */

// ============================================
// 기본 수직 Elastic Drag
// ============================================
@Composable
fun VerticalElasticDrag(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }

    // 저항 계수 (높을수록 빨리 느려짐)
    val resistanceFactor = 0.008f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.Center
    ) {
        // 가이드 라인
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(200.dp)
                .background(Color(0xFF90CAF9))
        )

        // 드래그 가능한 요소
        Box(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .size(80.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFF2196F3))
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                offsetY.animateTo(
                                    targetValue = 0f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioHighBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            }
                        },
                        onVerticalDrag = { _, dragAmount ->
                            scope.launch {
                                // 저항 공식: 멀수록 저항 증가

                                val resistance = 1f / (1f + abs(offsetY.value) * resistanceFactor)
                                val newOffset = offsetY.value + dragAmount * resistance
                                offsetY.snapTo(newOffset)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SwapVert,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        // 현재 오프셋 표시
        Text(
            text = "Offset: ${offsetY.value.roundToInt()}",
            fontSize = 12.sp,
            color = Color(0xFF1976D2),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

// ============================================
// 수평 Elastic Drag
// ============================================
@Composable
fun HorizontalElasticDrag(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }

    val resistanceFactor = 0.01f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFCE4EC)),
        contentAlignment = Alignment.Center
    ) {
        // 가이드 라인
        Box(
            modifier = Modifier
                .width(250.dp)
                .height(2.dp)
                .background(Color(0xFFF8BBD9))
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .size(60.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFFE91E63))
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                offsetX.animateTo(
                                    targetValue = 0f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMediumLow
                                    )
                                )
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val resistance = 1f / (1f + abs(offsetX.value) * resistanceFactor)
                                offsetX.snapTo(offsetX.value + dragAmount * resistance)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// ============================================
// 2D Elastic Drag (전방향)
// ============================================
@Composable
fun TwoDimensionalElasticDrag(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    val resistanceFactor = 0.006f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE8F5E9)),
        contentAlignment = Alignment.Center
    ) {
        // 십자 가이드
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(180.dp)
                .background(Color(0xFFA5D6A7))
        )
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(2.dp)
                .background(Color(0xFFA5D6A7))
        )

        // 드래그 가능한 요소
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .size(70.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            scope.launch {
                                launch {
                                    offsetX.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioHighBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                                launch {
                                    offsetY.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioHighBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                            }
                        },
                        onDrag = { _, dragAmount ->
                            scope.launch {
                                // 중심으로부터의 거리 기반 저항
                                val distance = sqrt(offsetX.value * offsetX.value + offsetY.value * offsetY.value )
                                val resistance = 1f / (1f + distance * resistanceFactor)

                                offsetX.snapTo(offsetX.value + dragAmount.x * resistance)
                                offsetY.snapTo(offsetY.value + dragAmount.y * resistance)
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
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

// ============================================
// Pull-to-Refresh 스타일
// ============================================
@Composable
fun PullToRefreshElastic(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val pullOffset = remember { Animatable(0f) }
    var isRefreshing by remember { mutableStateOf(false) }

    val threshold = 150f
    val resistanceFactor = 0.005f

    // 당긴 정도에 따른 진행률 (0~1)
    val progress = (pullOffset.value / threshold).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFF3E0))
    ) {
        // 인디케이터
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset { IntOffset(0, (pullOffset.value * 0.5f - 40).roundToInt()) }
                .size(40.dp)
                .graphicsLayer {
                    rotationZ = progress * 360f
                    alpha = progress
                }
                .clip(CircleShape)
                .background(Color(0xFFFF9800)),
            contentAlignment = Alignment.Center
        ) {
            if (isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // 콘텐츠
        Card(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, pullOffset.value.roundToInt()) }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                if (pullOffset.value >= threshold && !isRefreshing) {
                                    // 리프레시 트리거
                                    isRefreshing = true
                                    pullOffset.animateTo(threshold * 0.5f)

                                    // 시뮬레이션: 2초 후 완료
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
                                    val resistance = 1f / (1f + pullOffset.value * resistanceFactor)
                                    val newOffset = (pullOffset.value + dragAmount * resistance)
                                        .coerceAtLeast(0f)  // 아래로만 당기기
                                    pullOffset.snapTo(newOffset)
                                }
                            }
                        }
                    )
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isRefreshing) "Refreshing..."
                    else if (progress >= 1f) "Release to refresh"
                    else "Pull down to refresh",
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFF9800)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Progress: ${(progress * 100).roundToInt()}%",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// ============================================
// 저항 강도 비교
// ============================================
@Composable
fun ResistanceComparison(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    data class ResistanceConfig(
        val label: String,
        val factor: Float,
        val color: Color,
        val offset: Animatable<Float, AnimationVector1D>
    )

    val configs = remember {
        listOf(
            ResistanceConfig(
                label = "약함\n0.003",
                factor = 0.003f,
                color = Color(0xFF4CAF50),
                offset = Animatable(0f)
            ),
            ResistanceConfig(
                label = "중간\n0.008",
                factor = 0.008f,
                color = Color(0xFFFF9800),
                offset = Animatable(0f)
            ),
            ResistanceConfig(
                label = "강함\n0.015",
                factor = 0.015f,
                color = Color(0xFFF44336),
                offset = Animatable(0f)
            )
        )
    }

    Column (
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        configs.forEach { config ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = config.label,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.width(50.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(config.color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // 트랙
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .padding(horizontal = 25.dp)
                            .background(config.color.copy(alpha = 0.3f))
                    )

                    // 드래그 핸들
                    Box(
                        modifier = Modifier
                            .padding(start = 25.dp)
                            .offset { IntOffset(config.offset.value.roundToInt(), 0) }
                            .size(40.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(config.color)
                            .pointerInput(config.factor) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        scope.launch {
                                            config.offset.animateTo(
                                                targetValue = 0f,
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioHighBouncy,
                                                    stiffness = Spring.StiffnessLow
                                                )
                                            )
                                        }
                                    },
                                    onHorizontalDrag = { _, dragAmount ->
                                        scope.launch {
                                            val resistance = 1f / (1f + abs(config.offset.value) * config.factor)
                                            val newOffset = config.offset.value + dragAmount * resistance
                                            config.offset.snapTo(newOffset)
                                        }
                                    }
                                )
                            }
                    )
                }
            }
        }
    }
}

// ============================================
// 바운스 강도 비교
// ============================================
@Composable
fun BounceComparison(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    data class BounceConfig(
        val label: String,
        val damping: Float,
        val color: Color,
        val offset: Animatable<Float, AnimationVector1D>
    )

    val configs = remember {
        listOf(
            BounceConfig(
                label = "High\nBouncy",
                damping = Spring.DampingRatioHighBouncy,
                color = Color(0xFF9C27B0),
                offset = Animatable(0f)
            ),
            BounceConfig(
                label = "Medium\nBouncy",
                damping = Spring.DampingRatioMediumBouncy,
                color = Color(0xFF3F51B5),
                offset = Animatable(0f)
            ),
            BounceConfig(
                label = "No\nBounce",
                damping = Spring.DampingRatioNoBouncy,
                color = Color(0xFF009688),
                offset = Animatable(0f)
            )
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        configs.forEach { config ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = config.label,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.width(50.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(config.color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 25.dp)
                            .offset { IntOffset(config.offset.value.roundToInt(), 0) }
                            .size(40.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(config.color)
                            .pointerInput(config.damping) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        scope.launch {
                                            config.offset.animateTo(
                                                targetValue = 0f,
                                                animationSpec = spring(
                                                    dampingRatio = config.damping,
                                                    stiffness = Spring.StiffnessLow
                                                )
                                            )
                                        }
                                    },
                                    onHorizontalDrag = { _, dragAmount ->
                                        scope.launch {
                                            val resistance = 1f / (1f + abs(config.offset.value) * 0.008f)
                                            config.offset.snapTo(config.offset.value + dragAmount * resistance)
                                        }
                                    }
                                )
                            }
                    )
                }
            }
        }
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun ElasticDragDemo() {
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
            text = "Elastic Drag Animation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        // 수직
        DemoSection(title = "수직 Elastic Drag") {
            VerticalElasticDrag()
        }

        // 수평
        DemoSection(title = "수평 Elastic Drag") {
            HorizontalElasticDrag()
        }

        // 2D
        DemoSection(title = "2D Elastic Drag (전방향)") {
            TwoDimensionalElasticDrag()
        }

        // Pull to Refresh
        DemoSection(title = "Pull-to-Refresh 스타일") {
            PullToRefreshElastic()
        }

        // 저항 비교
        DemoSection(title = "저항(Resistance) 강도 비교") {
            ResistanceComparison()
        }

        // 바운스 비교
        DemoSection(title = "바운스(Damping) 강도 비교") {
            BounceComparison()
        }

        // 가이드
        ElasticGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ElasticGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Elastic Drag 가이드")
            CodeSection(
                title = "저항(Resistance) 공식:",
                code = """
                    resistance = 1f / (1f + abs(offset) * factor)
                    newOffset = offset + dragAmount * resistance
                """.trimIndent()
            )

            FeatureSection(
                customTitle = "factor 값:",
                features = """
                    factor 값:
                    • 0.003f = 약한 저항 (멀리 드래그 가능)
                    • 0.008f = 중간 저항
                    • 0.015f = 강한 저항 (금방 느려짐)
                """.trimIndent()
            )

            CodeSection(
                title = "복귀 애니메이션:",
                code = """
                    spring(
                        dampingRatio = HighBouncy,  // 많이 튕김
                        stiffness = Low             // 느리게
                    )
                """.trimIndent()
            )

            FeatureSection(
                features = """
                    • 2D는 sqrt(x² + y²)로 거리 계산
                    • Pull-to-Refresh는 threshold 설정
                    • coerceAtLeast(0f)로 방향 제한
                """.trimIndent(),
                type = FeatureTextType.TIP
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun ElasticDragDemoPreview() {
    ElasticDragDemo()
}
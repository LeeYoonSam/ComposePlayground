package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * 🟠 Advanced #13: Chained Springs (연결된 스프링)
 *
 * 📖 핵심 개념
 *
 * 첫 번째 요소의 위치를 변경하면, 나머지 요소들이 spring 애니메이션으로 앞 요소의 위치를 따라갑니다. 각 요소는 자신의 목표값으로 앞 요소의 현재 위치를 사용합니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * Animatable | 각 요소의 위치 관리
 * spring | 탄성 애니메이션
 * LaunchedEffect | 앞 요소 위치 관찰
 * snapshotFlow | 상태 변화 감지
 *
 *
 * 학습 목표:
 * 1. 여러 요소가 연쇄적으로 움직이는 효과
 * 2. snapshotFlow로 상태 변화 감지
 * 3. 각 요소가 앞 요소를 따라가는 패턴
 */

private val ChainColors = listOf(
    Color(0xFFE91E63),
    Color(0xFF9C27B0),
    Color(0xFF673AB7),
    Color(0xFF3F51B5),
    Color(0xFF2196F3),
    Color(0xFF00BCD4),
    Color(0xFF009688),
    Color(0xFF4CAF50)
)

// ============================================
// 기본 체인 스프링
// ============================================
@Composable
fun BasicChainedSprings(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val chainCount = 6
    val ballSize = 40.dp

    // 리더(첫 번째)의 위치
    val leaderPosition = remember { Animatable(Offset(50f, 50f), Offset.VectorConverter) }

    // 팔로워들의 애니메이션 상태
    val followerPositions = remember {
        List(chainCount - 1) {
            Animatable(Offset(50f, 50f), Offset.VectorConverter)
        }
    }

    // 첫 번째 팔로워 → 리더를 따라감
    LaunchedEffect(Unit) {
        snapshotFlow { leaderPosition.value }
            .collect { target ->
                followerPositions[0].animateTo(
                    targetValue = target,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
    }

    // 나머지 팔로워들 → 앞 팔로워를 따라감
    for (i in 1 until followerPositions.size) {
        val current = followerPositions[i]
        val previous = followerPositions[i - 1]

        LaunchedEffect(Unit) {
            snapshotFlow { previous.value }
                .collect { target ->
                    current.animateTo(
                        targetValue = target,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFCE4EC))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        val newPosition = Offset(
                            x = (leaderPosition.value.x + dragAmount.x).coerceIn(0f, size.width - 100f),
                            y = (leaderPosition.value.y + dragAmount.y).coerceIn(0f, size.height - 100f)
                        )
                        leaderPosition.snapTo(newPosition)
                    }

                }
            },
        contentAlignment = Alignment.TopStart
    ) {
        // 팔로워들 (뒤에서부터 그려서 리더가 위에 오도록)
        for (i in followerPositions.indices.reversed()) {
            ChainBall(
                position = followerPositions[i].value,
                color = ChainColors[(i + 1) % ChainColors.size],
                size = ballSize,
                label = "${i + 2}"
            )
        }

        // 리더
        ChainBall(
            position = leaderPosition.value,
            color = ChainColors[0],
            size = ballSize,
            label = "1",
            isLeader = true
        )

        // 안내 텍스트
        Text(
            text = "드래그하여 이동",
            fontSize = 12.sp,
            color = Color(0xFFC2185B),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

@Composable
private fun ChainBall(
    position: Offset,
    color: Color,
    size: Dp,
    label: String,
    isLeader: Boolean = false
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
            .size(size)
            .shadow(if (isLeader) 8.dp else 4.dp, CircleShape)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ============================================
// 수평 체인 (다양한 stiffness)
// ============================================
@Composable
fun HorizontalChainWithStiffness(modifier: Modifier = Modifier) {
    val chainCount = 5
    val ballSize = 36.dp
    val scope = rememberCoroutineScope()

    val leaderX = remember { Animatable(0f) }

    val stiffnessValues = listOf(
        Spring.StiffnessHigh,
        Spring.StiffnessMedium,
        Spring.StiffnessMediumLow,
        Spring.StiffnessLow
    )

    val followerXs = remember {
        List(chainCount - 1) { Animatable(0f) }
    }

    // 첫 번째 팔로워 → 리더를 따라감
    LaunchedEffect(Unit) {
        snapshotFlow { leaderX.value }
            .collect { target ->
                followerXs[0].animateTo(
                    targetValue = target,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = stiffnessValues[0]
                    )
                )
            }
    }

    // 나머지 팔로워들
    for (i in 1 until followerXs.size) {
        val current = followerXs[i]
        val previous = followerXs[i - 1]
        val stiffness = stiffnessValues[i]

        LaunchedEffect(Unit) {
            snapshotFlow { previous.value }
                .collect { target ->
                    current.animateTo(
                        targetValue = target,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = stiffness
                        )
                    )
                }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Stiffness: High → Low",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE8F5E9))
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            val newX = (leaderX.value + dragAmount.x)
                                .coerceIn(0f, size.width - 200f)
                            leaderX.animateTo(newX)
                        }
                    }
                },
            contentAlignment = Alignment.CenterStart
        ) {
            // 팔로워들
            for (i in followerXs.indices.reversed()) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(followerXs[i].value.roundToInt() + (i + 1) * 30, 0) }
                        .size(ballSize)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(ChainColors[(i + 1) % ChainColors.size]),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${i + 2}", color = Color.White, fontSize = 12.sp)
                }
            }

            // 리더
            Box(
                modifier = Modifier
                    .offset { IntOffset(leaderX.value.roundToInt(), 0) }
                    .size(ballSize)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(ChainColors[0]),
                contentAlignment = Alignment.Center
            ) {
                Text("1", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ============================================
// 뱀(Snake) 효과
// ============================================
@Composable
fun SnakeEffect(modifier: Modifier = Modifier) {
    val segmentCount = 10
    val segmentSize = 24.dp
    val scope = rememberCoroutineScope()

    val headPosition = remember { Animatable(Offset(100f, 100f), Offset.VectorConverter) }

    val segmentPositions = remember {
        List(segmentCount - 1) {
            Animatable(Offset(100f, 100f), Offset.VectorConverter)
        }
    }

    // 첫 번째 세그먼트 → 머리를 따라감
    LaunchedEffect(Unit) {
        snapshotFlow { headPosition.value }
            .collect { target ->
                segmentPositions[0].animateTo(
                    targetValue = target,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
    }

    // 나머지 세그먼트들
    for (i in 1 until segmentPositions.size) {
        val current = segmentPositions[i]
        val previous = segmentPositions[i - 1]

        LaunchedEffect(Unit) {
            snapshotFlow { previous.value }
                .collect { target ->
                    current.animateTo(
                        targetValue = target,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF263238))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        val newPosition = Offset(
                            x = (headPosition.value.x + dragAmount.x)
                                .coerceIn(0f, size.width - 80f),
                            y = (headPosition.value.y + dragAmount.y)
                                .coerceIn(0f, size.height - 80f)
                        )
                        headPosition.snapTo(newPosition)
                    }
                }
            },
        contentAlignment = Alignment.TopStart
    ) {
        // 몸통 (뒤에서부터)
        for (i in segmentPositions.indices.reversed()) {
            val alpha = 1f - (i * 0.08f)
            val currentSize = segmentSize * (1f - i * 0.05f)

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            segmentPositions[i].value.x.roundToInt(),
                            segmentPositions[i].value.y.roundToInt()
                        )
                    }
                    .size(currentSize)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50).copy(alpha = alpha))
            )
        }

        // 머리
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        headPosition.value.x.roundToInt(),
                        headPosition.value.y.roundToInt()
                    )
                }
                .size(segmentSize)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFF8BC34A)),
            contentAlignment = Alignment.Center
        ) {
            Text("🐍", fontSize = 12.sp)
        }

        Text(
            text = "Snake Effect",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

// ============================================
// 로딩 인디케이터 체인
// ============================================
@Composable
fun ChainLoadingIndicator(modifier: Modifier = Modifier) {
    val dotCount = 5
    val dotSize = 16.dp

    val offsetYs = remember {
        List(dotCount) { Animatable(0f) }
    }

    // 첫 번째 점 무한 애니메이션
    LaunchedEffect(Unit) {
        while (true) {
            offsetYs[0].animateTo(
                targetValue = -30f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            offsetYs[0].animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            kotlinx.coroutines.delay(300)
        }
    }

    // 나머지 점들이 앞 점을 따라감
    for (i in 1 until dotCount) {
        val previous = offsetYs[i - 1]
        val current = offsetYs[i]

        LaunchedEffect(Unit) {
            snapshotFlow { previous.value }
                .collect { target ->
                    current.animateTo(
                        targetValue = target,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
                }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF37474F)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            offsetYs.forEachIndexed { index, animatable ->
                Box(
                    modifier = Modifier
                        .offset { IntOffset(0, animatable.value.roundToInt()) }
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(ChainColors[index % ChainColors.size])
                )
            }
        }

        Text(
            text = "Chain Loading",
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

// ============================================
// 탄성 꼬리 효과
// ============================================
@Composable
fun ElasticTailEffect(modifier: Modifier = Modifier) {
    val tailCount = 8
    val scope = rememberCoroutineScope()

    val headX = remember { Animatable(100f) }

    val tailXs = remember {
        List(tailCount - 1) { Animatable(100f) }
    }

    // 첫 번째 꼬리 → 머리를 따라감
    LaunchedEffect(Unit) {
        snapshotFlow { headX.value }
            .collect { target ->
                tailXs[0].animateTo(
                    targetValue = target,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
    }

    // 나머지 꼬리들
    for (i in 1 until tailXs.size) {
        val current = tailXs[i]
        val previous = tailXs[i - 1]

        LaunchedEffect(Unit) {
            snapshotFlow { previous.value }
                .collect { target ->
                    current.animateTo(
                        targetValue = target,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioHighBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFF8E1))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        val newX = (headX.value + dragAmount.x).coerceIn(20f, size.width - 80f)
                        headX.snapTo(newX)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // 꼬리들
        for (i in tailXs.indices.reversed()) {
            val width = (50 - i * 5).coerceAtLeast(10)
            val alpha = 1f - (i * 0.1f)

            Box(
                modifier = Modifier
                    .offset { IntOffset(tailXs[i].value.roundToInt(), 0) }
                    .size(width.dp, 20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFF9800).copy(alpha = alpha))
            )
        }

        // 머리
        Box(
            modifier = Modifier
                .offset { IntOffset(headX.value.roundToInt(), 0) }
                .size(50.dp, 30.dp)
                .shadow(4.dp, RoundedCornerShape(15.dp))
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFFFF9800)),
            contentAlignment = Alignment.Center
        ) {
            Text("🚀", fontSize = 16.sp)
        }

        Text(
            text = "Elastic Tail",
            fontSize = 10.sp,
            color = Color(0xFFFF6F00),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun ChainedSpringsDemo() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Chained Springs",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "기본 체인 (2D 드래그)") {
            BasicChainedSprings()
        }

        DemoSection(title = "수평 체인 (Stiffness 비교)") {
            HorizontalChainWithStiffness()
        }

        DemoSection(title = "뱀 효과 (Snake)") {
            SnakeEffect()
        }

        DemoSection(title = "체인 로딩 인디케이터") {
            ChainLoadingIndicator()
        }

        DemoSection(title = "탄성 꼬리 효과") {
            ElasticTailEffect()
        }

        ChainedSpringsGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ChainedSpringsGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Chained Springs 가이드")

            CodeSection(
                title = "",
                code = """
                    // 앞 요소 위치 관찰
                    LaunchedEffect(Unit) {
                        snapshotFlow { previousPosition }
                            .collect { target ->
                                currentPosition.animateTo(
                                    target,
                                    spring(...)
                                )
                            }
                    }
                """.trimIndent()
            )

            FeatureSection(
                features = """
                    파라미터 효과:
                    • stiffness ↓ = 더 느리게 따라감
                    • dampingRatio ↓ = 더 많이 튕김
                    • 요소 수 ↑ = 긴 꼬리 효과
                """.trimIndent()
            )

            FeatureSection(
                features = """
                    • 뒤에서부터 그려야 리더가 위에 보임
                    • coerceIn으로 화면 범위 제한
                    • NoBouncy면 부드러운 따라가기
                """.trimIndent(),
                type = FeatureTextType.TIP
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1600)
@Composable
private fun ChainedSpringsDemoPreview() {
    ChainedSpringsDemo()
}
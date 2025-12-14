package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/**
 * 🟡 Intermediate #7: Spring 바운스 애니메이션
 *
 * 📖 핵심 개념
 *
 * spring() AnimationSpec은 물리 법칙을 기반으로 애니메이션합니다. dampingRatio(감쇠비)와 stiffness(강성)를 조절하여 다양한 바운스 효과를 만들 수 있어요. tween과 달리 duration이 없고, 물리적으로 자연스러운 종료 시점을 계산합니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * spring() | 물리 기반 애니메이션 스펙
 * dampingRatio | 감쇠비 (0~1, 낮을수록 바운스)
 * stiffness | 강성 (높을수록 빠름)
 * Animatable | 저수준 애니메이션 제어
 * animateTo | 목표값으로 애니메이션
 * snapTo | 즉시 이동
 *
 * 💡 동작 원리
 *
 * ```
 * Spring 물리 모델:
 *
 * F = -kx - cv
 *
 * k = stiffness (스프링 강성)
 * c = damping (감쇠 계수)
 * x = 변위
 * v = 속도
 *
 * dampingRatio:
 * - 0.0 = 무한 바운스 (undamped)
 * - 0.2 = HighBouncy (많이 튕겨)
 * - 0.5 = MediumBouncy (중간)
 * - 0.75 = LowBouncy (조금 튕겨)
 * - 1.0 = NoBouncy (튕김 없음, critically damped)
 * ```
 *
 * 학습 목표:
 * 1. spring() AnimationSpec 이해
 * 2. dampingRatio와 stiffness 조절
 * 3. Animatable을 사용한 저수준 제어
 * 4. 다양한 바운스 효과 구현
 */

// ============================================
// 기본 Spring 버튼 (animateFloatAsState)
// ============================================
@Composable
fun SpringButtonBasic(
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF6200EE))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            }
            .padding(horizontal = 32.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Press Me!",
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

// ============================================
// DampingRatio 비교 데모
// ============================================
@Composable
fun DampingRatioComparison(
    modifier: Modifier = Modifier
) {
    val dampingOptions = listOf(
        "HighBouncy (0.2)" to Spring.DampingRatioHighBouncy,
        "MediumBouncy (0.5)" to Spring.DampingRatioMediumBouncy,
        "LowBouncy (0.75)" to Spring.DampingRatioLowBouncy,
        "NoBouncy (1.0)" to Spring.DampingRatioNoBouncy
    )

    var trigger by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { trigger = !trigger },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Bounce All!")
        }

        dampingOptions.forEach { (label, damping) ->
            val offsetX by animateFloatAsState(
                targetValue = if (trigger) 200f else 0f,
                animationSpec = spring(
                    dampingRatio = damping,
                    stiffness = Spring.StiffnessLow
                ),
                label = label
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.width(120.dp)
                )

                Box(
                    modifier = Modifier
                        .offset(x = offsetX.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0XFF2196F3))
                )
            }
        }
    }
}
// ============================================
// Stiffness 비교 데모
// ============================================
@Composable
fun StiffnessComparison(
    modifier: Modifier = Modifier
) {
    val stiffnessOptions = listOf(
        "High (10000)" to Spring.StiffnessHigh,
        "Medium (1500)" to Spring.StiffnessMedium,
        "MediumLow (400)" to Spring.StiffnessMediumLow,
        "Low (200)" to Spring.StiffnessLow,
        "VeryLow (50)" to Spring.StiffnessVeryLow
    )

    var trigger by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { trigger = !trigger },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Move All!")
        }

        stiffnessOptions.forEach { (label, stiffness) ->
            val offsetX by animateFloatAsState(
                targetValue = if (trigger) 180f else 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = stiffness
                ),
                label = label
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.width(120.dp)
                )

                Box(
                    modifier = Modifier
                        .offset(x = offsetX.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                )
            }
        }
    }
}

// ============================================
// Animatable로 바운스 볼
// ============================================
@Composable
fun BouncingBall(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                scope.launch {
                    // 위로 올렸다가
                    offsetY.animateTo(
                        targetValue = -100f,
                        animationSpec = tween(200)
                    )
                    // 바운스하며 내려옴
                    offsetY.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = 0.3f,
                            stiffness = 300f
                        )
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF5722)
            )
        ) {
            Text("Drop Ball")
        }

        Box(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            // 그림자
            Box(
                modifier = Modifier
                    .offset(y = (-10).dp)
                    .width((60 + offsetY.value * 0.2f).dp)
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.2f))
            )
            // 공
            Box(
                modifier = Modifier
                    .offset(y = offsetY.value.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF5722)),
                contentAlignment = Alignment.Center
            ) {
                Text("🏀", fontSize = 32.sp)
            }
        }
    }
}

// ============================================
// 알림 배지 바운스
// ============================================
@Composable
fun NotificationBadgeBounce(
    modifier: Modifier = Modifier
) {
    var count by remember { mutableIntStateOf(0) }
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    count ++
                    scope.launch {
                        // 커졌다가
                        scale.animateTo(
                            targetValue = 1.5f,
                            animationSpec = tween(100)
                        )
                        // 바운스하며 돌아옴
                        scale.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(
                                dampingRatio = 0.4f,
                                stiffness = 400f
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0)
                )
            ) {
                Text("+1")
            }

            OutlinedButton(
                onClick = { count = 0 }
            ) {
                Text("Reset")
            }
        }

        // 알림 아이콘
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE1BEE7)),
                contentAlignment = Alignment.Center
            ) {
                Text("🔔", fontSize = 28.sp)
            }

            // 배지
            if (count > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = 8.dp, y = (-8).dp)
                        .scale(scale.value)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE91E63)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (count > 99) "99+" else count.toString(),
                        color = Color.White,
                        fontSize = if (count > 99) 8.sp else 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ============================================
// 카드 플립 with Spring
// ============================================
@Composable
fun SpringCardFlip(
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 100f
        ),
        label = "rotation"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { isFlipped = !isFlipped },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00BCD4)
            )
        ) {
            Text("Flip Card")
        }

        Box(
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                }
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (rotation <= 90f) Color(0xFF00BCD4)
                    else Color(0xFFFF9800)
                )
                .clickable { isFlipped = !isFlipped },
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                Text("Front", color = Color.White, fontWeight = FontWeight.Bold)
            } else {
                Text(
                    text = "Back",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.graphicsLayer { rotationY = 180f }
                )
            }
        }
    }
}

// ============================================
// 인터랙티브 Spring 파라미터 조절
// ============================================
@Composable
fun InteractiveSpringDemo(
    modifier: Modifier = Modifier
) {
    var dampingRatio by remember { mutableFloatStateOf(0.5f) }
    var stiffness by remember { mutableFloatStateOf(400f) }
    var trigger by remember { mutableStateOf(false) }

    val offsetX by animateFloatAsState(
        targetValue = if (trigger) 250f else 0f,
        animationSpec = spring(
            dampingRatio = dampingRatio,
            stiffness = stiffness
        ),
        label = "offset"
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Damping Ratio 슬라이더
        Column {
            Text(
                text = "Damping Ratio: %.2f".format(dampingRatio),
                fontSize = 12.sp,
                color = Color.Gray
            )
            Slider(
                value = dampingRatio,
                onValueChange = { dampingRatio = it },
                valueRange = 0.1f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF673AB7),
                    activeTrackColor = Color(0xFF673AB7)
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Bouncy", fontSize = 10.sp, color = Color.Gray)
                Text("No Bounce", fontSize = 10.sp, color = Color.Gray)
            }
        }

        // Stiffness 슬라이더
        Column {
            Text(
                text = "Stiffness: %.0f".format(stiffness),
                fontSize = 12.sp,
                color = Color.Gray
            )
            Slider(
                value = stiffness,
                onValueChange = { stiffness = it },
                valueRange = 50f..2000f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF673AB7),
                    activeTrackColor = Color(0xFF673AB7)
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Slow", fontSize = 10.sp, color = Color.Gray)
                Text("Fast", fontSize = 10.sp, color = Color.Gray)
            }
        }

        // 트리거 버튼
        Button(
            onClick = { trigger = !trigger },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF673AB7)
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Animate!")
        }

        // 애니메이션 대상
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .offset(x = offsetX.dp)
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF673AB7)),
                contentAlignment = Alignment.Center
            ) {
                Text("🎾", fontSize = 24.sp)
            }
        }
    }
}

// ============================================
// 연속 바운스 체인
// ============================================
@Composable
fun ChainedBounce(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val balls = remember {
        List(5) { Animatable(0f) }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                scope.launch {
                    balls.forEachIndexed { index, animatable ->
                        launch {
                            kotlinx.coroutines.delay(index * 80L)
                            animatable.animateTo(
                                targetValue = -80f,
                                animationSpec = tween(150)
                            )
                            animatable.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(
                                    dampingRatio = 0.4f,
                                    stiffness = 500f
                                )
                            )
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE91E63)
            )
        ) {
            Text("Chain Bounce!")
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.height(120.dp)
        ) {
            val colors = listOf(
                Color(0xFFE91E63),
                Color(0xFFFF5722),
                Color(0xFFFFEB3B),
                Color(0xFF4CAF50),
                Color(0xFF2196F3)
            )

            balls.forEachIndexed { index, animatable ->
                Box(
                    modifier = Modifier
                        .offset(y = animatable.value.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colors[index])
                )
            }
        }
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun SpringBounceDemo() {
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
            text = "Spring Bounce Animation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        // 기본 버튼
        DemoSection(title = "기본 Spring 버튼") {
            SpringButtonBasic(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // DampingRatio 비교
        DemoSection(title = "DampingRatio 비교 (감쇠비)") {
            DampingRatioComparison()
        }

        // Stiffness 비교
        DemoSection(title = "Stiffness 비교 (강성)") {
            StiffnessComparison()
        }

        // 바운싱 볼
        DemoSection(title = "Animatable - 바운싱 볼") {
            BouncingBall()
        }

        // 알림 배지
        DemoSection(title = "알림 배지 바운스") {
            NotificationBadgeBounce()
        }

        // 카드 플립
        DemoSection(title = "Spring 카드 플립") {
            SpringCardFlip()
        }

        // 인터랙티브
        DemoSection(title = "인터랙티브 파라미터 조절") {
            InteractiveSpringDemo()
        }

        // 체인 바운스
        DemoSection(title = "연속 바운스 체인") {
            ChainedBounce()
        }

        // 가이드
        SpringGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SpringGuide() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            "📚 Spring 파라미터 가이드",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            """
            DampingRatio (감쇠비):
            • 0.2 (HighBouncy) = 많이 튕김
            • 0.5 (MediumBouncy) = 중간
            • 0.75 (LowBouncy) = 조금 튕김
            • 1.0 (NoBouncy) = 튕김 없음
            
            Stiffness (강성):
            • 10000 (High) = 매우 빠름
            • 1500 (Medium) = 보통
            • 400 (MediumLow) = 약간 느림
            • 200 (Low) = 느림
            • 50 (VeryLow) = 매우 느림
            
            💡 조합 팁:
            • 버튼 press: Low damping + Medium stiffness
            • 드래그 drop: Medium damping + Low stiffness
            • 빠른 반응: High stiffness + Any damping
            """.trimIndent(),
            fontSize = 12.sp,
            color = Color.Gray,
            lineHeight = 18.sp
        )
    }
}

@Preview(showBackground = true, heightDp = 2200)
@Composable
fun SpringBounceDemoPreview() {
    SpringBounceDemo()
}
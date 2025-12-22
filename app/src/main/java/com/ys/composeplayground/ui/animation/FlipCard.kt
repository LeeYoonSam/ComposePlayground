package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * 🟡 Intermediate #10: 3D 카드 플립 애니메이션
 *
 * 📖 핵심 개념
 *
 * graphicsLayer의 rotationY로 Y축 회전을 적용하고, cameraDistance로 원근감을 조절합니다. 회전 각도에 따라 앞/뒤 면을 교체하여 플립 효과를 구현해요.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * graphicsLayer | 3D 변환 적용
 * rotationY | Y축 회전 (0°~180°)
 * rotationX | X축 회전
 * cameraDistance | 원근감 조절
 * animateFloatAsState | 회전 값 애니메이션
 *
 * 💡 동작 원리
 *
 * ```
 * [앞면] rotation = 0°
 *        ↓ animateFloatAsState
 * [회전 중] rotation = 90° (측면, 안보임)
 *        ↓
 * [뒷면] rotation = 180°
 *
 * 판정 기준: rotation <= 90° → 앞면
 *           rotation > 90° → 뒷면 (반전 보정 필요)
 * ```
 *
 * 학습 목표:
 * 1. graphicsLayer로 3D 회전 적용
 * 2. rotationY/rotationX 사용법
 * 3. cameraDistance로 원근감 조절
 * 4. 앞/뒤 면 전환 로직
 */

// ============================================
// 기본 플립 카드
// ============================================
@Composable
fun BasicFlipCard(
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .size(200.dp, 280.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable { isFlipped = !isFlipped },
        contentAlignment = Alignment.Center
    ) {
        if (rotation <= 90f) {
            // 앞면
            CardFront()
        } else {
            // 뒷면 (180도 반전 보정)
            CardBack(
                modifier = Modifier.graphicsLayer { rotationY = 180f }
            )
        }
    }
}

@Composable
fun CardFront(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6200EE)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🎴",
                    fontSize = 64.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Tap to Flip",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun CardBack(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFF5722)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🎯",
                    fontSize = 64.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Back Side!",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ============================================
// Spring 플립 카드
// ============================================
@Composable
fun SpringFlipCard(
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(
        modifier = modifier
            .size(width = 180.dp, height = 250.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12 * density
            }
            .clickable { isFlipped = !isFlipped },
        contentAlignment = Alignment.Center
    ) {
        if (rotation <= 90) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏀", fontSize = 48.sp)
                        Text("Spring!", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎾", fontSize = 48.sp)
                    Text("Bounce!", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ============================================
// X축 플립 (세로 방향)
// ============================================
@Composable
fun VerticalFlipCard(
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(600),
        label = "verticalRotation"
    )

    Box(
        modifier = modifier
            .size(180.dp, 250.dp)
            .graphicsLayer {
                rotationX = rotation  // X축 회전!
                cameraDistance = 12f * density
            }
            .clickable { isFlipped = !isFlipped },
        contentAlignment = Alignment.Center
    ) {
        if (rotation <= 90f) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF9C27B0))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⬆️", fontSize = 48.sp)
                        Text("Vertical", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationX = 180f },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE91E63))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⬇️", fontSize = 48.sp)
                        Text("Flip!", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ============================================
// 드래그로 플립
// ============================================
@Composable
fun DragFlipCard(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val rotation = remember { Animatable(0f) }

    // 0~180 범위에서만 동작하도록 단순화
    val showFront = abs(rotation.value) < 90f

    Box(
        modifier = modifier
            .size(180.dp, 250.dp)
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        scope.launch {
                            // 90° 기준으로 스냅
                            val target = if (abs(rotation.value) >= 90f) {
                                // 뒷면으로 스냅 (방향 유지)
                                if (rotation.value > 0) 180f else -180f
                            } else {
                                // 앞면으로 복귀
                                0f
                            }

                            rotation.animateTo(
                                target,
                                spring(dampingRatio = 0.6f, stiffness = 300f)
                            )
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        scope.launch {
                            // 방향 수정: 드래그 방향과 회전 방향 일치
                            // -180 ~ 180 범위로 제한
                            val newValue = (rotation.value + dragAmount * 0.8f)
                                .coerceIn(-180f, 180f)
                            rotation.snapTo(newValue)
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (showFront) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF00BCD4))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("👆", fontSize = 48.sp)
                        Text("Drag Me", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎉", fontSize = 48.sp)
                        Text("Found!", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ============================================
// 신용카드 스타일
// ============================================
@Composable
fun CreditCardFlip(
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "creditCardRotation"
    )

    Box(
        modifier = modifier
            .width(320.dp)
            .height(200.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 16f * density
            }
            .clickable { isFlipped = !isFlipped }
    ) {
        if (rotation <= 90f) {
            // 카드 앞면
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF1A237E),
                                    Color(0xFF3949AB)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("VISA", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text("💳", fontSize = 24.sp)
                        }

                        Text(
                            "4242 4242 4242 4242",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 2.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("CARD HOLDER", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                                Text("JOHN DOE", color = Color.White, fontSize = 14.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("EXPIRES", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                                Text("12/28", color = Color.White, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        } else {
            // 카드 뒷면
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f },
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF3949AB),
                                    Color(0xFF1A237E)
                                )
                            )
                        )
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(24.dp))

                        // 마그네틱 스트립
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(Color.Black)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // CVV 영역
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Box(
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(36.dp)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "123",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            "Tap to flip back",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

// ============================================
// 플래시카드 (학습용)
// ============================================
@Composable
fun FlashCard(
    question: String,
    answer: String,
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 200f
        ),
        label = "flashCardRotation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .graphicsLayer {
                rotationX = rotation
                cameraDistance = 12f * density
            }
            .clickable { isFlipped = !isFlipped }
    ) {
        if (rotation <= 90) {
            // 질문
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3F51B5))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Q",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            question,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else  {
            // 답변
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationX = 180f },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "A",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            answer,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// ============================================
// cameraDistance 비교
// ============================================
@Composable
fun CameraDistanceComparison(
    modifier: Modifier = Modifier
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(800),
        label = "cameraCompare"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { isFlipped = !isFlipped },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF607D8B))
        ) {
            Text("Flip All")
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf(4f to "4f", 12f to "12f", 16f to "16f", 24f to "24").forEach { (distance, label) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(label, fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(90.dp, 120.dp)
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = distance * density
                            }
                    ) {
                        if (rotation <= 90f) {
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF607D8B))
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("F", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        } else {
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer { rotationY = 180f },
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF455A64))
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("B", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun FlipCardDemo() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "3D Card Flip Animation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        // 기본 플립
        DemoSection(title = "기본 플립 (Tap)") {
            BasicFlipCard()
        }

        // Spring 플립
        DemoSection(title = "Spring 플립 (Bounce)") {
            SpringFlipCard()
        }

        // 세로 플립
        DemoSection(title = "세로 플립 (rotationX)") {
            VerticalFlipCard()
        }

        // 드래그 플립
        DemoSection(title = "드래그 플립") {
            DragFlipCard()
        }

        // 신용카드
        DemoSection(title = "신용카드 스타일") {
            CreditCardFlip()
        }

        // 플래시카드
        DemoSection(title = "플래시카드 (학습용)") {
            FlashCard(
                question = "Compose에서 3D 회전에 사용하는 Modifier는?",
                answer = "graphicsLayer { rotationY = ... }"
            )
        }

        // cameraDistance 비교
        DemoSection(title = "cameraDistance 비교") {
            CameraDistanceComparison()
        }

        // 가이드
        FlipGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FlipGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 3D 카드 플립 가이드")

            CodeSection(
                title = "핵심 코드:",
                code = """
                    Modifier.graphicsLayer {
                        rotationY = rotation    // Y축 회전
                        rotationX = rotation    // X축 회전
                        cameraDistance = 12f * density
                    }
                """.trimIndent()
            )

            CodeSection(
                title = "앞/뒤 판정:",
                code = """
                    if (rotation <= 90f) {
                        FrontCard()
                    } else {
                        // 뒷면은 180도 보정!
                        BackCard(
                            Modifier.graphicsLayer { rotationY = 180f }
                        )
                    }
                """.trimIndent()
            )

            FeatureSection(
                features = """
                    • cameraDistance 낮음 = 원근감 강함
                    • cameraDistance 높음 = 평면적
                    • spring으로 바운스 효과
                    • 뒷면은 반드시 180도 반전 보정
                """.trimIndent(),
                type = FeatureTextType.TIP
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
fun FlipCardDemoPreview() {
    FlipCardDemo()
}
package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 🟢 Beginner #1: 버튼 Press 스케일 애니메이션
 *
 * 📖 핵심 개념
 *
 * 버튼을 누르면 살짝 축소되었다가 놓으면 원래 크기로 돌아오는 마이크로 인터랙션이에요. 이 간단한 효과가 사용자에게 "내 터치가 인식됐구나"라는 피드백을 주어 앱이 훨씬 생동감 있게 느껴집니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * animateFloatAsState | 상태 변화에 따라 Float 값을 부드럽게 애니메이션
 * pointerInput | 터치 이벤트 감지 (press/release)
 * graphicsLayer | scale, rotation, alpha 등 변환 적용
 * spring() | 물리 기반 자연스러운 애니메이션
 *
 * 💡 동작 원리
 * ```
 * [손가락 Down] → isPressed = true → scale: 1.0 → 0.95
 *                     ↓
 * [손가락 Up]   → isPressed = false → scale: 0.95 → 1.0 (with bounce)
 * ```
 *
 * 학습 목표:
 * 1. animateFloatAsState 사용법
 * 2. pointerInput으로 터치 이벤트 감지
 * 3. graphicsLayer로 변환 적용
 * 4. spring 애니메이션 커스터마이징
 */

// ============================================
// 기본 버전: 가장 심플한 구현
// ============================================
@Composable
fun PressScaleButtonBasic(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1️⃣ 눌림 상태 관리
    var isPressed by remember { mutableStateOf(false) }

    // 2️⃣ 상태에 따라 scale 값 애니메이션
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scaleAnimation"
    )

    Box(
        modifier = modifier
            // 3️⃣ scale 적용
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            // 4️⃣ 터치 이벤트 감지
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        // 손가락 떼기를 기다림
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            }
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF6200EE))
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ============================================
// 향상된 버전: 더 풍부한 피드백
// ============================================
@Composable
fun PressScaleButtonEnhanced(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    // 커스터마이징 가능한 파라미터들
    pressedScale: Float = 0.92f,
    pressedAlpha: Float  = 0.8f,
    baseColor: Color = Color(0XFF6200EE)
) {
    var isPressed by remember { mutableStateOf(false) }

    // 여러 속성을 동시 애니메이션
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isPressed) pressedAlpha else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            }
            .clip(RoundedCornerShape(16.dp))
            .background(baseColor)
            .padding(horizontal = 32.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ============================================
// 고급 버전: Spring 파라미터 실험용
// ============================================
@Composable
fun PressScaleButtonWithSpringConfig(
    text: String,
    onClick: () -> Unit,
    dampingRatio: Float = Spring.DampingRatioMediumBouncy,
    stiffness: Float = Spring.StiffnessLow,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = dampingRatio,
            stiffness = stiffness
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            }
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF03DAC5))
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ============================================
// 데모 화면: 다양한 버전 비교
// ============================================
@Composable
fun PressScaleDemo() {
    var clickCount by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Press Scale Animation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Text(
            text = "클릭 횟수: $clickCount",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 기본 버전
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("기본 버전", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            PressScaleButtonBasic(
                text = "Basic Button",
                onClick = { clickCount++ }
            )
        }

        // 향상된 버전
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("향상된 버전 (Alpha 포함)", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            PressScaleButtonEnhanced(
                text = "Enhanced Button",
                onClick = { clickCount++ }
            )
        }

        // Spring 실험: 높은 바운스
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("High Bounce", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            PressScaleButtonWithSpringConfig(
                text = "Bouncy!",
                onClick = { clickCount++ },
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessLow
            )
        }

        // Spring 실험: 바운스 없음, 빠름
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No Bounce (빠르고 딱딱함)", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            PressScaleButtonWithSpringConfig(
                text = "Snappy",
                onClick = { clickCount++ },
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessHigh
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Spring 파라미터 설명
        SpringParameterGuide()
    }
}

@Composable
fun SpringParameterGuide() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        FeatureSection(
            features = """
                dampingRatio (바운스 정도):
                • HighBouncy (0.2): 많이 튕김
                • MediumBouncy (0.5): 적당히 튕김
                • LowBouncy (0.75): 조금 튕김
                • NoBouncy (1.0): 안 튕김
                
                stiffness (속도):
                • High (10000): 매우 빠름
                • Medium (1500): 보통
                • Low (200): 느림
                • VeryLow (50): 매우 느림
            """.trimIndent(),
            type = FeatureTextType.TIP
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun PressScaleDemoPreview() {
    PressScaleDemo()
}
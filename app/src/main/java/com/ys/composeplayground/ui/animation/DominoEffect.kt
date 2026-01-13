package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * 🎯 Bonus #25: Domino Effect (도미노 효과)
 *
 * 📖 핵심 개념
 *
 *
 * 요소들이 연쇄적으로 넘어지거나 변화하는 효과입니다. 한 요소의 변화가 다음 요소로 전파되며, 시각적으로 매력적인 순차 애니메이션을 만듭니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * Animatable | 개별 요소 애니메이션
 * launch | 순차 실행
 * delay | 시차 효과
 * rotationZ / rotationX | 넘어지는 효과
 *
 * 💡 동작 원리
 *
 * ```
 * [트리거] 첫 번째 도미노 클릭
 *    ↓ delay(50ms)
 * [도미노 1] rotationZ: 0° → 90° (넘어짐)
 *    ↓ delay(50ms)
 * [도미노 2] rotationZ: 0° → 90°
 *    ↓ delay(50ms)
 * [도미노 3] rotationZ: 0° → 90°
 *    ... 연쇄 반응
 * ```
 *
 * 학습 목표:
 * 1. 순차적 애니메이션 트리거
 * 2. transformOrigin으로 회전 축 설정
 * 3. 다양한 도미노 패턴
 * 4. 리셋 애니메이션
 */

// ============================================
// 기본 도미노 효과
// ============================================
@Composable
fun BasicDominoEffect(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val dominoCount = 8

    val rotations = remember {
        List(dominoCount) { Animatable(0f) }
    }

    var isFallen by remember { mutableStateOf(false) }

    fun triggerDomino() {
        scope.launch {
            if (isFallen) {
                // 리셋: 역순으로 일어남
                for (i in (dominoCount - 1) downTo 0) {
                    launch {
                        rotations[i].animateTo(
                            targetValue = 0f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
                    }
                    delay(40)
                }
                delay(dominoCount * 40L + 300)
                isFallen = false
            } else {
                // 넘어짐: 순차적으로
                for (i in 0 until dominoCount) {
                    launch {
                        rotations[i].animateTo(
                            targetValue = 75f,
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                    delay(60)
                }

                delay(dominoCount * 60L + 300)
                isFallen = true
            }
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
        // 도미노들
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.height(120.dp)
        ) {
            rotations.forEachIndexed { index, rotation ->
                val hue = (index * 360f / dominoCount)
                val color = Color.hsl(
                    hue = hue,
                    saturation = 0.7f,
                    lightness = 0.6f
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = rotation.value
                            transformOrigin = TransformOrigin(
                                pivotFractionX = 0.5f,
                                pivotFractionY = 1f
                            ) // 하단 중앙 기준
                        }
                        .width(20.dp)
                        .height(80.dp)
                        .shadow(4.dp, RoundedCornerShape(4.dp))
                        .clip(RoundedCornerShape(4.dp))
                        .background(color)
                        .clickable { triggerDomino() }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { triggerDomino() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFallen) Color(0xFF00B894) else Color(0xFF6C5CE7)
            )
        ) {
            Text(
                text = if (isFallen) "일으키기" else "넘어뜨리기",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================
// 3D 도미노 (앞으로 넘어짐)
// ============================================
@Composable
fun Domino3DEffect(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val dominoCount = 6

    val rotations = remember {
        List(dominoCount) { Animatable(0f) }
    }

    var isFallen by remember { mutableStateOf(false) }

    fun triggerDomino() {
        scope.launch {
            if (isFallen) {
                for (i in (dominoCount - 1) downTo 0) {
                    launch {
                        rotations[i].animateTo(
                            targetValue = 0f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
                    }
                    delay(80)
                }
                delay(dominoCount * 80L + 400)
                isFallen = false
            } else {
                for (i in 0 until dominoCount) {
                    launch {
                        rotations[i].animateTo(
                            targetValue = -85f,
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                    delay(100)
                }
                delay(dominoCount * 100L + 400)
                isFallen = true
            }
        }
    }

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
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                rotations.forEachIndexed { index, rotation ->
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                rotationX = rotation.value
                                transformOrigin = TransformOrigin(0.5f, 1f)
                                cameraDistance = 12f * density
                            }
                            .width(40.dp)
                            .height(100.dp)
                            .shadow(6.dp, RoundedCornerShape(6.dp))
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFFFFE66D),
                                        Color(0xFFFDCB6E)
                                    )
                                )
                            )
                            .clickable { triggerDomino() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            color = Color(0xFF2D3436),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { triggerDomino() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFallen) Color(0xFFFFE66D) else Color(0xFF74B9FF)
            )
        ) {
            Text(
                text = if (isFallen) "세우기" else "밀기",
                color = Color(0xFF2D3436),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================
// 카드 도미노 (펼쳐지는 카드)
// ============================================
@Composable
fun CardDominoEffect(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val cardCount = 5

    val rotations = remember {
        List(cardCount) { Animatable(0f) }
    }

    val translations = remember {
        List(cardCount) { Animatable(0f) }
    }

    var isSpread by remember { mutableStateOf(false) }

    fun triggerCards() {
        scope.launch {
            if (isSpread) {
                // 모으기
                for (i in (cardCount - 1) downTo 0) {
                    launch {
                        rotations[i].animateTo(0f, tween(300))
                        translations[i].animateTo(0f, tween(300))
                    }
                    delay(50)
                }
                delay(cardCount * 50L + 300)
                isSpread = false
            } else {
                // 펼치기
                for (i in 0 until cardCount) {
                    val targetRotation = -20f + i * 10f
                    val targetTranslation = (i - cardCount / 2) * 60f

                    launch {
                        launch { rotations[i].animateTo(targetRotation, tween(400)) }
                        launch { translations[i].animateTo(targetTranslation, tween(400)) }
                    }
                    delay(80)
                }
                delay(cardCount * 80L + 400)
                isSpread = true
            }
        }
    }

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
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {
            rotations.forEachIndexed { index, rotation ->
                val suits = listOf("♠", "♥", "♦", "♣", "★")
                val colors = listOf(
                    Color(0xFF2D3436),
                    Color(0xFFFF6B6B),
                    Color(0xFFFF6B6B),
                    Color(0xFF2D3436),
                    Color(0xFFFDCB6E)
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = rotation.value
                            translationX = translations[index].value
                            transformOrigin = TransformOrigin(0.5f, 1f)
                        }
                        .width(70.dp)
                        .height(100.dp)
                        .shadow(8.dp, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .clickable { triggerCards() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = suits[index],
                        color = colors[index],
                        fontSize = 32.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { triggerCards() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSpread) Color(0xFFFF6B6B) else Color(0xFF00CEC9)
            )
        ) {
            Text(
                text = if (isSpread) "모으기" else "펼치기",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================
// 원형 도미노
// ============================================
@Composable
fun CircularDominoEffect(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val itemCount = 12

    val scales = remember {
        List(itemCount) { Animatable(1f) }
    }

    val alphas = remember {
        List(itemCount) { Animatable(1f) }
    }

    var isTriggered by remember { mutableStateOf(false) }

    fun triggerCircle() {
        scope.launch {
            if (isTriggered) {
                // 복원
                for (i in 0 until itemCount) {
                    launch {
                        scales[i].animateTo(1f, spring(stiffness = Spring.StiffnessMedium))
                        alphas[i].animateTo(1f, tween(200))
                    }
                    delay(30)
                }
                delay(itemCount * 30L + 300)
                isTriggered = false
            } else {
                // 순차적으로 펄스
                for (i in 0 until itemCount) {
                    launch {
                        scales[i].animateTo(1.5f, tween(150))
                        alphas[i].animateTo(0.5f, tween(150))
                        scales[i].animateTo(0.8f, tween(150))
                        alphas[i].animateTo(0.3f, tween(150))
                    }
                    delay(50)
                }
                delay(itemCount * 50L + 300)
                isTriggered = true
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E272E)),
        contentAlignment = Alignment.Center
    ) {
        val centerX = with(density) { maxWidth.toPx() / 2 }
        val centerY = with(density) { maxHeight.toPx() / 2 }
        val radius = with(density) { 80.dp.toPx() }

        scales.forEachIndexed { index, scale ->
            val angle = (index * 360f / itemCount) - 90f
            val radians = Math.toRadians(angle.toDouble())
            val x = centerX + radius * kotlin.math.cos(radians).toFloat()
            val y = centerY + radius * kotlin.math.sin(radians).toFloat()

            val itemSize = 30.dp
            val itemSizePx = with(density) { itemSize.toPx() }

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (x - itemSizePx / 2).roundToInt(),
                            (y - itemSizePx / 2).roundToInt()
                        )
                    }
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                        alpha = alphas[index].value
                    }
                    .size(itemSize)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.hsl(index * 30f, 0.7f, 0.6f))
                    .clickable { triggerCircle() }
            )
        }

        // 중앙 버튼
        Box(
            modifier = Modifier
                .size(60.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(
                    if (isTriggered) Color(0xFFFF6B6B) else Color(0xFF6C5CE7)
                )
                .clickable { triggerCircle() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isTriggered) "↺" else "▶",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================
// 타일 도미노 (그리드)
// ============================================
@Composable
fun TileDominoEffect(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val rows = 4
    val cols = 5

    val rotations = remember {
        List(rows * cols) { Animatable(0f) }
    }

    var isFlipped by remember { mutableStateOf(false) }

    fun triggerTiles() {
        scope.launch {
            if (isFlipped) {
                // 역순 복원
                for (row in (rows - 1) downTo 0) {
                    for (col in (cols - 1) downTo 0) {
                        val index = row * cols + col
                        launch {
                            rotations[index].animateTo(
                                0f,
                                spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                        }
                        delay(30)
                    }
                }
                delay((rows * cols) * 30L + 300)
                isFlipped = false
            } else {
                // 대각선 순서로 뒤집기
                for (diagonal in 0 until (rows + cols - 1)) {
                    for (row in 0 until rows) {
                        val col = diagonal - row
                        if (col in 0 until cols) {
                            val index = row * cols + col
                            launch {
                                rotations[index].animateTo(
                                    180f,
                                    tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            }
                        }
                    }
                    delay(60)
                }
                delay((rows + cols) * 60L + 300)
                isFlipped = true
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2C3E50))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (row in 0 until rows) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (col in 0 until cols) {
                        val index = row * cols + col
                        val rotation = rotations[index]

                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    rotationY = rotation.value
                                    cameraDistance = 12f * density
                                }
                                .size(50.dp)
                                .shadow(4.dp, RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (rotation.value < 90f) {
                                        Color(0xFF74B9FF)
                                    } else {
                                        Color(0xFFFF7675)
                                    }
                                )
                                .clickable { triggerTiles() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (rotation.value < 90f) "●" else "★",
                                color = Color.White,
                                fontSize = 20.sp,
                                modifier = Modifier.graphicsLayer {
                                    // 뒷면일 때 텍스트도 뒤집기
                                    if (rotation.value >= 90f) {
                                        rotationY = 180f
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { triggerTiles() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFlipped) Color(0xFFFF7675) else Color(0xFF74B9FF)
            )
        ) {
            Text(
                text = if (isFlipped) "원래대로" else "뒤집기",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================
// 텍스트 도미노
// ============================================
@Composable
fun TextDominoEffect(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val text = "DOMINO"

    val rotations = remember {
        List(text.length) { Animatable(0f) }
    }

    val offsets = remember {
        List(text.length) { Animatable(0f) }
    }

    var isFallen by remember { mutableStateOf(false) }

    fun triggerText() {
        scope.launch {
            if (isFallen) {
                for (i in text.indices.reversed()) {
                    launch {
                        rotations[i].animateTo(
                            0f,
                            spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        offsets[i].animateTo(0f, tween(300))
                    }
                    delay(60)
                }
                delay(text.length * 60L + 400)
                isFallen = false
            } else {
                for (i in text.indices) {
                    launch {
                        rotations[i].animateTo(
                            -15f + i * 5f,
                            tween(200)
                        )
                        offsets[i].animateTo(
                            i * 8f,
                            tween(200)
                        )
                    }
                    delay(80)
                }
                delay(text.length * 80L + 200)
                isFallen = true
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F0F23))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .height(100.dp)
                .clickable { triggerText() }
        ) {
            text.forEachIndexed { index, char ->
                val colors = listOf(
                    Color(0xFFFF6B6B),
                    Color(0xFFFFE66D),
                    Color(0xFF4ECDC4),
                    Color(0xFF74B9FF),
                    Color(0xFFA29BFE),
                    Color(0xFFFF6B6B)
                )

                Text(
                    text = char.toString(),
                    color = colors[index % colors.size],
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = rotations[index].value
                            translationY = offsets[index].value
                            transformOrigin = TransformOrigin(0.5f, 1f)
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { triggerText() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFallen) Color(0xFFA29BFE) else Color(0xFF00CEC9)
            )
        ) {
            Text(
                text = if (isFallen) "세우기" else "넘어뜨리기",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================
// 웨이브 도미노
// ============================================
@Composable
fun WaveDominoEffect(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val barCount = 15

    val heights = remember {
        List(barCount) { Animatable(1f) }
    }

    var isWaving by remember { mutableStateOf(false) }

    fun triggerWave() {
        scope.launch {
            if (isWaving) {
                // 리셋
                for (i in 0 until barCount) {
                    launch {
                        heights[i].animateTo(
                            1f,
                            spring(stiffness = Spring.StiffnessMedium)
                        )
                    }
                    delay(20)
                }
                delay(barCount * 20L + 300)
                isWaving = false
            } else {
                // 웨이브
                repeat(3) { wave ->
                    for (i in 0 until barCount) {
                        launch {
                            heights[i].animateTo(2.5f, tween(100))
                            heights[i].animateTo(0.5f, tween(100))
                            heights[i].animateTo(1.5f, tween(100))
                            heights[i].animateTo(1f, tween(100))
                        }
                        delay(30)
                    }
                    delay(200)
                }
                isWaving = true
            }
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.height(100.dp)
        ) {
            heights.forEachIndexed { index, height ->
                val baseHeight = 40.dp

                Box(
                    modifier = Modifier
                        .width(12.dp)
                        .height(baseHeight * height.value)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.hsl(index * 24f, 0.8f, 0.6f),
                                    Color.hsl(index * 24f, 0.6f, 0.4f)
                                )
                            )
                        )
                        .clickable { triggerWave() }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { triggerWave() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6C5CE7)
            )
        ) {
            Text(
                text = if (isWaving) "다시" else "웨이브",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun DominoEffectDemo() {
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
            text = "Domino Effect",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "기본 도미노") {
            BasicDominoEffect()
        }

        DemoSection(title = "3D 도미노") {
            Domino3DEffect()
        }

        DemoSection(title = "카드 펼치기") {
            CardDominoEffect()
        }

        DemoSection(title = "원형 도미노") {
            CircularDominoEffect()
        }

        DemoSection(title = "타일 뒤집기") {
            TileDominoEffect()
        }

        DemoSection(title = "텍스트 도미노") {
            TextDominoEffect()
        }

        DemoSection(title = "웨이브 도미노") {
            WaveDominoEffect()
        }

        DominoEffectGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DominoEffectGuide() {
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
                text = "📚 Domino Effect 가이드",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = """
                    핵심 원리:
                    
                    • 순차 트리거
                      for (i in indices) {
                          launch { animate() }
                          delay(50)  // 시차
                      }
                    
                    • transformOrigin
                      하단 기준 회전: TransformOrigin(0.5f, 1f)
                    
                    • 3D 회전
                      rotationX/Y + cameraDistance
                    
                    • 대각선 순서
                      diagonal = row + col
                    
                    💡 팁:
                    • delay 값으로 속도 조절
                    • 역순 복원으로 자연스러운 리셋
                    • spring으로 바운스 효과
                """.trimIndent(),
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 2400)
@Composable
private fun DominoEffectDemoPreview() {
    DominoEffectDemo()
}
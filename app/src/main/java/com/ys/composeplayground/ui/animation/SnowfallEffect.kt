package com.ys.composeplayground.ui.animation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * 🔴 Expert #17: Snowfall Effect (눈 내리기) 애니메이션
 *
 * 📖 핵심 개념
 *
 * Canvas와 무한 애니메이션 루프를 사용하여 눈송이가 자연스럽게 내리는 효과를 만듭니다. 각 눈송이는 다른 크기, 속도, 좌우 흔들림을 가집니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * Canvas | 눈송이 그리기
 * LaunchedEffect | 무한 애니메이션 루프
 * withFrameMillis | 프레임별 업데이트
 * sin() | 좌우 흔들림 (사인파)
 * Random | 랜덤 속성 생성
 *
 * 💡 동작 원리
 *
 * ```
 * [초기화] 눈송이 N개 생성 (화면 전체에 분포)
 *        ↓ 각각 랜덤 속성 부여
 * [매 프레임]
 *        - y += fallSpeed (아래로)
 *        - x += sin(y * frequency) * amplitude (좌우 흔들림)
 *        ↓ 화면 아래로 벗어나면
 * [재활용] y = 0 (위로 리셋), x = 랜덤
 *
 * 사인파 흔들림:
 * x_offset = sin(y * frequency) * amplitude
 * - frequency: 흔들림 빈도
 * - amplitude: 흔들림 폭
 * ```
 *
 * 학습 목표:
 * 1. Canvas로 눈송이 그리기
 * 2. 사인파를 이용한 좌우 흔들림
 * 3. 파티클 재활용 (리셋)
 * 4. 다양한 눈송이 스타일
 *
 * 핵심 포인트:
 * - data class 대신 일반 class 사용 (속성 변경 감지)
 * - frameTime 상태로 recomposition 트리거
 */

// ============================================
// 눈송이 데이터 클래스
// ============================================
data class Snowflake(
    var x: Float,
    var y: Float,
    val size: Float,
    val speed: Float,
    val alpha: Float,
    val amplitude: Float,    // 좌우 흔들림 폭
    val frequency: Float,    // 흔들림 빈도
    var rotation: Float = 0f,
    val rotationSpeed: Float = 0f
)

// ============================================
// 기본 눈 내리기 효과
// ============================================
@Composable
fun BasicSnowfall(modifier: Modifier = Modifier) {
    var canvasSize by remember { mutableStateOf(Pair(0f, 0f)) }
    var frameTime by remember { mutableLongStateOf(0L) }

    val snowflakes = remember {
        List(80) {
            Snowflake(
                x = Random.nextFloat() * 1000f,
                y = Random.nextFloat() * 600f,
                size = Random.nextFloat() * 4f + 2f,
                speed = Random.nextFloat() * 2f + 1f,
                alpha = Random.nextFloat() * 0.5f + 0.3f,
                amplitude = Random.nextFloat() * 0.8f + 0.2f,
                frequency = Random.nextFloat() * 0.02f + 0.01f
            )
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time

                snowflakes.forEach { flake ->
                    flake.y += flake.speed
                    flake.x += sin(flake.y * flake.frequency) * flake.amplitude

                    if (flake.y > canvasSize.second + 20f) {
                        flake.y = -20f
                        flake.x = Random.nextFloat() * canvasSize.first.coerceAtLeast(100f)
                    }

                    if (flake.x < -20f) flake.x = canvasSize.first + 10f
                    if (flake.x > canvasSize.first + 20f) flake.x = -10f
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F2027),
                            Color(0xFF203A43),
                            Color(0xFF2C5364)
                        )
                    )
                )
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            canvasSize = Pair(size.width, size.height)

            frameTime.let {
                snowflakes.forEach { flake ->
                    drawCircle(
                        color = Color.White.copy(alpha = flake.alpha),
                        radius = flake.size,
                        center = Offset(flake.x, flake.y)
                    )
                }
            }
        }

        Text(
            text = "❄️ ${snowflakes.size} snowflakes",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )
    }
}

// ============================================
// 크리스탈 눈송이
// ============================================
@Composable
fun CrystalSnowfall(modifier: Modifier = Modifier) {
    var canvasSize by remember { mutableStateOf(Pair(0f, 0f)) }
    var frameTime by remember { mutableLongStateOf(0L) }

    val snowflakes = remember {
        List(40) {
            Snowflake(
                x = Random.nextFloat() * 1000f,
                y = Random.nextFloat() * 600f,
                size = Random.nextFloat() * 12f + 6f,
                speed = Random.nextFloat() * 1.5f + 0.5f,
                alpha = Random.nextFloat() * 0.4f + 0.4f,
                amplitude = Random.nextFloat() * 0.5f + 0.2f,
                frequency = Random.nextFloat() * 0.015f + 0.005f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextFloat() * 2f - 1f
            )
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time

                snowflakes.forEach { flake ->
                    flake.y += flake.speed
                    flake.x += sin(flake.y * flake.frequency) * flake.amplitude
                    flake.rotation += flake.rotationSpeed

                    if (flake.y > canvasSize.second + 30f) {
                        flake.y = -30f
                        flake.x = Random.nextFloat() * canvasSize.first.coerceAtLeast(100f)
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1a1a2e),
                        Color(0xFF16213e),
                        Color(0xFF0f3460)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            canvasSize = Pair(size.width, size.height)

            frameTime.let {
                snowflakes.forEach { flake ->
                    drawCrystalSnowflake(
                        center = Offset(flake.x, flake.y),
                        size = flake.size,
                        alpha = flake.alpha,
                        rotation = flake.rotation
                    )
                }
            }
        }

        Text(
            text = "✨ Crystal Snowflakes",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )
    }
}

private fun DrawScope.drawCrystalSnowflake(
    center: Offset,
    size: Float,
    alpha: Float,
    rotation: Float
) {
    val color = Color.White.copy(alpha = alpha)
    val branches = 6

    rotate(rotation, center) {
        repeat(branches) { i ->
            val angle = (i * 60f) * (PI / 180f).toFloat()

            val endX = center.x + cos(angle) * size
            val endY = center.y + sin(angle) * size

            drawLine(
                color = color,
                start = center,
                end = Offset(endX, endY),
                strokeWidth = 2f
            )

            val branchLength = size * 0.4f
            val branchPos = 0.6f

            val midX = center.x + cos(angle) * size * branchPos
            val midY = center.y + sin(angle) * size * branchPos

            val leftAngle = angle - (PI / 4).toFloat()
            drawLine(
                color = color.copy(alpha = alpha * 0.7f),
                start = Offset(midX, midY),
                end = Offset(
                    midX + cos(leftAngle) * branchLength,
                    midY + sin(leftAngle) * branchLength
                ),
                strokeWidth = 1.5f
            )

            val rightAngle = angle + (PI / 4).toFloat()
            drawLine(
                color = color.copy(alpha = alpha * 0.7f),
                start = Offset(midX, midY),
                end = Offset(
                    midX + cos(rightAngle) * branchLength,
                    midY + sin(rightAngle) * branchLength
                ),
                strokeWidth = 1.5f
            )
        }

        drawCircle(
            color = color,
            radius = size * 0.15f,
            center = center
        )
    }
}

// ============================================
// 조절 가능한 눈 효과
// ============================================
@Composable
fun AdjustableSnowfall(modifier: Modifier = Modifier) {
    var snowCount by remember { mutableFloatStateOf(50f) }
    var fallSpeed by remember { mutableFloatStateOf(1.5f) }
    var swingAmplitude by remember { mutableFloatStateOf(0.5f) }

    var canvasSize by remember { mutableStateOf(Pair(0f, 0f)) }
    var frameTime by remember { mutableLongStateOf(0L) }

    val snowflakes = remember { mutableListOf<Snowflake>() }

    LaunchedEffect(snowCount) {
        val targetCount = snowCount.toInt()
        while (snowflakes.size < targetCount) {
            snowflakes.add(
                Snowflake(
                    x = Random.nextFloat() * canvasSize.first.coerceAtLeast(500f),
                    y = Random.nextFloat() * canvasSize.second.coerceAtLeast(300f),
                    size = Random.nextFloat() * 4f + 2f,
                    speed = Random.nextFloat() * 0.5f + 0.75f,
                    alpha = Random.nextFloat() * 0.5f + 0.3f,
                    amplitude = Random.nextFloat() * 0.5f + 0.5f,
                    frequency = Random.nextFloat() * 0.02f + 0.01f
                )
            )
        }

        while (snowflakes.size > targetCount) {
            snowflakes.removeLastOrNull()
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time

                snowflakes.forEach { flake ->
                    flake.y += fallSpeed * flake.speed
                    flake.x += sin(flake.y * flake.frequency) * swingAmplitude * flake.amplitude

                    if (flake.y > canvasSize.second + 20f) {
                        flake.y = -20f
                        flake.x = Random.nextFloat() * canvasSize.first.coerceAtLeast(100f)
                    }
                }
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2C3E50),
                            Color(0xFF3498DB),
                            Color(0xFF87CEEB)
                        )
                    )
                )
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                canvasSize = Pair(size.width, size.height)

                frameTime.let {
                    snowflakes.forEach { flake ->
                        drawCircle(
                            color = Color.White.copy(alpha = flake.alpha),
                            radius = flake.size,
                            center = Offset(flake.x, flake.y)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SliderControl(
                    label = "눈송이 수",
                    value = snowCount,
                    onValueChange = { snowCount = it },
                    valueRange = 10f..150f,
                    displayValue = "${snowCount.toInt()}개"
                )

                SliderControl(
                    label = "낙하 속도",
                    value = fallSpeed,
                    onValueChange = { fallSpeed = it },
                    valueRange = 0.5f..4f,
                    displayValue = String.format("%.1fx", fallSpeed)
                )

                SliderControl(
                    label = "흔들림 폭",
                    value = swingAmplitude,
                    onValueChange = { swingAmplitude = it },
                    valueRange = 0f..2f,
                    displayValue = String.format("%.1f", swingAmplitude)
                )
            }
        }
    }
}

@Composable
private fun SliderControl(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    displayValue: String
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = Color.Gray
            )
            Text(
                text = displayValue,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2196F3)
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF2196F3),
                activeTrackColor = Color(0xFF2196F3)
            )
        )
    }
}

// ============================================
// 밤하늘 눈 효과 (별과 함께)
// ============================================
@Composable
fun NightSkySnowfall(modifier: Modifier = Modifier) {
    var canvasSize by remember { mutableStateOf(Pair(0f, 0f)) }
    var frameTime by remember { mutableLongStateOf(0L) }

    val stars = remember {
        List(30) {
            Offset(
                Random.nextFloat() * 1000f,
                Random.nextFloat() * 400f
            ) to (Random.nextFloat() * 2f + 1f)
        }
    }

    val snowflakes = remember {
        List(60) {
            Snowflake(
                x = Random.nextFloat() * 1000f,
                y = Random.nextFloat() * 500f,
                size = Random.nextFloat() * 3f + 1.5f,
                speed = Random.nextFloat() * 1.5f + 0.5f,
                alpha = Random.nextFloat() * 0.6f + 0.2f,
                amplitude = Random.nextFloat() * 0.6f + 0.2f,
                frequency = Random.nextFloat() * 0.015f + 0.008f
            )
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time

                snowflakes.forEach { flake ->
                    flake.y += flake.speed
                    flake.x += sin(flake.y * flake.frequency) * flake.amplitude

                    if (flake.y > canvasSize.second + 20f) {
                        flake.y = -20f
                        flake.x = Random.nextFloat() * canvasSize.first.coerceAtLeast(100f)
                    }
                }

            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0a0a1a),
                        Color(0xFF1a1a3a),
                        Color(0xFF2a2a4a)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            canvasSize = Pair(size.width, size.height)

            frameTime.let { time ->
                val starTwinkle = (time % 2000) / 2000f

                stars.forEach { (position, starSize) ->
                    val twinkleAlpha = (sin(starTwinkle * 2 * PI + position.x).toFloat() + 1f) / 2f
                    drawCircle(
                        color = Color(0xFFFFFFCC).copy(alpha = 0.3f + twinkleAlpha * 0.5f),
                        radius = starSize,
                        center = position
                    )
                }

                drawCircle(
                    color = Color(0xFFFFFACD),
                    radius = 40f,
                    center = Offset(size.width - 80f, 80f)
                )

                drawCircle(
                    color = Color(0xFF1a1a3a),
                    radius = 35f,
                    center = Offset(size.width - 70f, 75f)
                )

                snowflakes.forEach { flake ->
                    drawCircle(
                        color = Color.White.copy(alpha = flake.alpha),
                        radius = flake.size,
                        center = Offset(flake.x, flake.y)
                    )
                }
            }
        }

        Text(
            text = "🌙 Winter Night",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun SnowfallEffectDemo() {
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
            text = "Snowfall Effect",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "기본 눈 내리기") {
            BasicSnowfall()
        }

        DemoSection(title = "크리스탈 눈송이") {
            CrystalSnowfall()
        }

        DemoSection(title = "조절 가능한 눈 효과") {
            AdjustableSnowfall()
        }

        DemoSection(title = "밤하늘 눈 효과") {
            NightSkySnowfall()
        }

        SnowfallEffectGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SnowfallEffectGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Snowfall Effect 가이드")

            CodeSection(
                title = "눈송이 데이터 구조",
                code = """
data class Snowflake(
    var x: Float,
    var y: Float,
    val size: Float,
    val speed: Float,
    val amplitude: Float,  // 흔들림 폭
    val frequency: Float   // 흔들림 빈도
)
                """.trimIndent()
            )

            CodeSection(
                title = "사인파 흔들림",
                code = """
// 매 프레임
flake.y += flake.speed
flake.x += sin(flake.y * frequency) * amplitude

// 화면 아래로 벗어나면 재활용
if (flake.y > height) {
    flake.y = 0f
    flake.x = Random.nextFloat() * width
}
                """.trimIndent()
            )

            FeatureSection(
                features = """
- sin()으로 자연스러운 좌우 흔들림
- 파티클 재활용으로 메모리 효율화
- 랜덤 속성으로 다양한 움직임
- 크기/투명도 변화로 깊이감
                """.trimIndent(),
                type = FeatureTextType.TIP
            )

            FeatureSection(
                features = """
- 화면 경계 체크 필수
- 너무 많은 파티클은 성능 저하
- canvasSize 초기화 타이밍 주의
                """.trimIndent(),
                type = FeatureTextType.CAUTION
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
private fun SnowfallEffectDemoPreview() {
    SnowfallEffectDemo()
}
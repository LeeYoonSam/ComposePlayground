package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.sin


/**
 * 🔴 Expert #20: Interactive Waveform (인터랙티브 파형)
 *
 * 📖 핵심 개념
 *
 * Canvas와 pointerInput을 결합하여 터치에 반응하는 파형을 그립니다. 터치 위치에 따라 파형이 변형되고, 오디오 비주얼라이저나 인터랙티브 배경으로 활용할 수 있습니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * Canvas | 파형 그리기
 * Path | 연속적인 곡선
 * pointerInput | 터치 감지
 * quadraticBezierTo | 부드러운 곡선
 * LaunchedEffect + withFrameMillis | 애니메이션 루프
 *
 * 💡 동작 원리
 *
 * ```
 * [기본 상태]
 *   sin(x * frequency + time) * amplitude로 파형 생성
 *        ↓ 터치 시
 * [터치 반응]
 *   터치 위치 근처의 amplitude 증가
 *   거리에 따라 영향력 감소
 *        ↓ 터치 종료
 * [복귀]
 *   spring으로 원래 amplitude로 복귀
 *
 * 파형 공식:
 * y = centerY + sin(x * frequency + phase) * amplitude * touchInfluence
 * ```
 *
 * 학습 목표:
 * 1. Canvas로 파형 그리기
 * 2. 터치에 반응하는 파형 변형
 * 3. sin 함수로 부드러운 곡선
 * 4. 실시간 애니메이션 루프
 */

// ============================================
// 기본 파형
// ============================================
@Composable
fun BasicWaveform(modifier: Modifier = Modifier) {
    var frameTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E))
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2

        val amplitude = height * 0.3f
        val frequency = 0.02f
        val phase = frameTime * 0.003f

        val path = Path()
        path.moveTo(0f, centerY)

        for (x in 0..width.toInt() step 2) {
            val y = centerY + sin(x * frequency + phase) * amplitude
            path.lineTo(x.toFloat(), y)
        }

        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF6C63FF),
                    Color(0xFFE91E63),
                    Color(0xFF6C63FF)
                )
            ),
            style = Stroke(width = 4f, cap = StrokeCap.Round)
        )
    }

}

// ============================================
// 터치 반응 파형
// ============================================
@Composable
fun TouchReactiveWaveform(modifier: Modifier = Modifier) {
    var frameTime by remember { mutableLongStateOf(0L) }
    var touchPoint by remember { mutableStateOf<Offset?>(null) }
    val scope = rememberCoroutineScope()
    val touchInfluence = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0D1B2A))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        touchPoint = offset
                        scope.launch {
                            touchInfluence.animateTo(
                                targetValue = 1f,
                                animationSpec = spring(stiffness = Spring.StiffnessHigh)
                            )
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            touchInfluence.animateTo(
                                targetValue = 0f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        touchPoint = change.position
                    }
                )
            }
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2

        val baseAmplitude = height * 0.2f
        val frequency = 0.015f
        val phase = frameTime * 0.002f

        val path = Path()
        path.moveTo(0f, centerY)

        for (x in 0..width.toInt() step 2) {
            var amplitude = baseAmplitude

            // 터치 영향 계산
            touchPoint?.let { touch ->
                val distance = abs(x - touch.x)
                val influence = exp(-distance * 0.01f) * touchInfluence.value
                amplitude += height * 0.3f * influence
            }

            val y = centerY + sin(x * frequency + phase) * amplitude
            path.lineTo(x.toFloat(), y)
        }

        // 파형 그리기
        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF00D9FF),
                    Color(0xFF00FF88),
                    Color(0xFF00D9FF)
                )
            ),
            style = Stroke(width = 3f, cap = StrokeCap.Round)
        )

        // 터치 포인트 표시
        touchPoint?.let { point ->
            if (touchInfluence.value > 0.01f) {
                drawCircle(
                    color = Color.White.copy(alpha = touchInfluence.value * 0.5f),
                    radius = 20f * touchInfluence.value,
                    center = point
                )
            }
        }
    }
}

// ============================================
// 다중 레이어 파형
// ============================================
@Composable
fun MultiLayerWaveform(modifier: Modifier = Modifier) {
    var frameTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    data class WaveLayer(
        val color: Color,
        val amplitude: Float,
        val frequency: Float,
        val speed: Float,
        val alpha: Float
    )

    val layers = listOf(
        WaveLayer(Color(0xFF6C63FF), 0.15f, 0.01f, 0.001f, 0.3f),
        WaveLayer(Color(0xFF9C27B0), 0.2f, 0.015f, 0.002f, 0.5f),
        WaveLayer(Color(0xFFE91E63), 0.25f, 0.02f, 0.003f, 0.7f),
        WaveLayer(Color(0xFFFF5722), 0.18f, 0.025f, 0.0025f, 1f)
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF121212))
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2

        layers.forEach { layer ->
            val path = Path()
            val amplitude = height * layer.amplitude
            val phase = frameTime * layer.speed

            path.moveTo(0f, centerY)

            for (x in 0..width.toInt() step 2) {
                val y = centerY + sin(x * layer.frequency + phase) * amplitude
                path.lineTo(x.toFloat(), y)
            }

            drawPath(
                path = path,
                color = layer.color.copy(alpha = layer.alpha),
                style = Stroke(width = 3f, cap = StrokeCap.Round)
            )
        }
    }
}

// ============================================
// 오디오 비주얼라이저 스타일
// ============================================
@Composable
fun AudioVisualizerWaveform(modifier: Modifier = Modifier) {
    var frameTime by remember { mutableLongStateOf(0L) }
    var touchY by remember { mutableFloatStateOf(0.5f) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E1E1E))
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    touchY = (change.position.y / size.height).coerceIn(0f, 1f)
                }
            }
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2

        val barCount = 50
        val barWidth = width / barCount * 0.6f
        val gap = width / barCount * 0.4f
        val phase = frameTime * 0.005f

        val colors = listOf(
            Color(0xFF00E5FF),
            Color(0xFF00E676),
            Color(0xFFFFEB3B),
            Color(0xFFFF5722)
        )

        for (i in 0 until barCount) {
            val x = i * (barWidth + gap) + gap / 2

            // 여러 sin 파를 합성하여 복잡한 패턴 생성
            val wave1 = sin(i * 0.2f + phase) * 0.5f
            val wave2 = sin(i * 0.15f + phase * 1.3f) * 0.3f
            val wave3 = sin(i * 0.1f + phase * 0.7f) * 0.2f
            val combined = (wave1 + wave2 + wave3 + 1f) / 2f

            // 터치 영향
            val touchInfluence = 0.5f + touchY * 0.5f
            val barHeight = height * 0.8f * combined + touchInfluence

            val colorIndex = (i * colors.size / barCount) % colors.size
            val barColor = colors[colorIndex]

            // 바 그리기
            drawRoundRect(
                color = barColor,
                topLeft = Offset(x, centerY - barHeight / 2),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2)
            )

            // 반사 효과
            drawRoundRect(
                color = barColor.copy(alpha = 0.3f),
                topLeft = Offset(x, centerY + barHeight / 2 + 4),
                size = Size(barWidth, barHeight * 0.3f),
                cornerRadius = CornerRadius(barWidth / 2)
            )
        }
    }
}

// ============================================
// 물결 효과 (Ripple Wave)
// ============================================
@Composable
fun RippleWaveform(modifier: Modifier = Modifier) {
    var frameTime by remember { mutableLongStateOf(0L) }
    var ripples by remember { mutableStateOf(listOf<Pair<Offset, Long>>()) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
                // 오래된 ripple 제거
                ripples = ripples.filter { time - it.second < 2000 }
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0A192F))
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    ripples = ripples + (offset to frameTime)
                }
            }
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2

        // 기본 파형
        val basePath = Path()
        val basePhase = frameTime * 0.002f

        basePath.moveTo(0f, centerY)
        for (x in 0..width.toInt() step 2) {
            var y = centerY + sin(x * 0.015f + basePhase) * height * 0.1f

            // ripple  영향 추가
            ripples.forEach { (center, startTime) ->
                val age = (frameTime - startTime) / 1000f
                val distance = kotlin.math.sqrt(
                    (x - center.x) * (x - center.x) +
                            (centerY - center.y) * (centerY - center.y)
                )
                val rippleRadius = age * 300f
                val rippleWidth = 100f

                if (abs(distance - rippleRadius) < rippleWidth) {
                    val rippleStrength = (1f - age / 2f).coerceAtLeast(0f)
                    val distanceFromRipple = abs(distance - rippleRadius) / rippleWidth
                    val influence = (1f - distanceFromRipple) * rippleStrength
                    y += sin(distance * 0.05f - frameTime * 0.01f) * height * 0.2f * influence
                }
            }

            basePath.lineTo(x.toFloat(), y)
        }

        drawPath(
            path = basePath,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF64FFDA),
                    Color(0xFF7C4DFF),
                    Color(0xFF64FFDA)
                )
            ),
            style = Stroke(width = 3f, cap = StrokeCap.Round)
        )

        // ripple 원 표시
        ripples.forEach { (center, startTime) ->
            val age = (frameTime - startTime) / 1000f
            val radius = age * 300f
            val alpha = (1f - age / 2f).coerceIn(0f, 0.5f)

            drawCircle(
                color = Color(0xFF64FFDA).copy(alpha = alpha),
                radius = radius,
                center = center,
                style = Stroke(width = 2f)
            )
        }

        // 안내 텍스트
        if (ripples.isEmpty()) {
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "탭하여 물결 생성",
                    width / 2,
                    height - 20,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.argb(128, 255, 255, 255)
                        textSize = 32f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

// ============================================
// 조절 가능한 파형
// ============================================
@Composable
fun AdjustableWaveform(modifier: Modifier = Modifier) {
    var frameTime by remember { mutableLongStateOf(0L) }
    var amplitude by remember { mutableFloatStateOf(0.3f) }
    var frequency by remember { mutableFloatStateOf(0.02f) }
    var speed by remember { mutableFloatStateOf(0.003f) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF2D3436))
        ) {
            val width = size.width
            val height = size.height
            val centerY = height / 2

            val phase = frameTime * speed

            val path = Path()
            path.moveTo(0f, centerY)

            for (x in 0..width.toInt() step 2) {
                val y = centerY + sin(x * frequency + phase) * height * amplitude
                path.lineTo(x.toFloat(), y)
            }

            // 채우기
            val fillPath = Path()
            fillPath.addPath(path)
            fillPath.lineTo(width, height)
            fillPath.lineTo(0f, height)
            fillPath.close()

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF00CEC9).copy(alpha = 0.5f),
                        Color(0xFF00CEC9).copy(alpha = 0.1f)
                    )
                )
            )

            drawPath(
                path = path,
                color = Color(0xFF00CEC9)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 컨트롤
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2D3436), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            SliderControl(
                label = "진폭",
                value = amplitude,
                range = 0.1f..0.5f,
                onValueChange = { amplitude = it }
            )

            SliderControl(
                label = "주파수",
                value = frequency,
                range = 0.005f..0.05f,
                onValueChange = { frequency = it }
            )

            SliderControl(
                label = "속도",
                value = speed,
                range = 0.001f..0.01f,
                onValueChange = { speed = it }
            )
        }
    }
}

@Composable
private fun SliderControl(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.2f)
        )

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            modifier = Modifier.weight(0.8f),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF00CEC9),
                activeTrackColor = Color(0xFF00CEC9)
            )
        )
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun InteractiveWaveformDemo() {
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
            text = "Interactive Waveform",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "기본 파형") {
            BasicWaveform()
        }

        DemoSection(title = "터치 반응 파형") {
            TouchReactiveWaveform()
        }

        DemoSection(title = "다중 레이어 파형") {
            MultiLayerWaveform()
        }

        DemoSection(title = "오디오 비주얼라이저") {
            AudioVisualizerWaveform()
        }

        DemoSection(title = "물결 효과 (탭하세요)") {
            RippleWaveform()
        }

        DemoSection(title = "조절 가능한 파형") {
            AdjustableWaveform()
        }

        WaveformGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun WaveformGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Interactive Waveform 가이드")

            CodeSection(
                title = "기본 파형 공식",
                code = """
y = centerY + sin(x * frequency + phase) * amplitude

// phase가 시간에 따라 변화 → 움직이는 파형
val phase = frameTime * speed
                """.trimIndent()
            )

            CodeSection(
                title = "터치 영향 계산",
                code = """
touchPoint?.let { touch ->
    val distance = abs(x - touch.x)
    val influence = exp(-distance * 0.01f)
    amplitude += extraAmplitude * influence
}
                """.trimIndent()
            )

            FeatureSection(
                features = """
- sin()으로 부드러운 파형 생성
- exp(-distance)로 거리 기반 감쇠
- 여러 sin 합성 → 복잡한 패턴
- Gradient로 시각적 풍부함
                """.trimIndent(),
                type = FeatureTextType.TIP
            )

            FeatureSection(
                features = """
- 너무 많은 포인트 → 성능 저하
- step 값으로 샘플링 조절
- ripple 제한으로 메모리 관리
                """.trimIndent(),
                type = FeatureTextType.CAUTION
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
private fun InteractiveWaveformDemoPreview() {
    InteractiveWaveformDemo()
}
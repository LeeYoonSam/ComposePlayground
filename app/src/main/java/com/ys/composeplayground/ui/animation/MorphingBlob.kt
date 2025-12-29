package com.ys.composeplayground.ui.animation

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 🔴 Expert #18: Morphing Blob (변형 블롭)
 *
 * 📖 핵심 개념
 *
 * Canvas와 Path를 사용하여 유기적으로 형태가 변하는 부드러운 블롭 효과를 만듭니다. 여러 제어점을 사인파로 움직여 살아있는 듯한 느낌을 표현합니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * Canvas | 블롭 그리기
 * Path | 곡선 경로 정의
 * cubicTo | 베지어 곡선
 * sin() / cos() | 주기적 변형
 * withFrameMillis | 시간 기반 애니메이션
 *
 * 💡 동작 원리
 *
 * ```
 * [기본 형태] N개의 제어점으로 원형 블롭 생성
 *        ↓ 각 점에 다른 위상(phase)의 사인파 적용
 * [변형] 매 프레임마다:
 *        - radius = baseRadius + sin(time * speed + phase) * amplitude
 *        - 각 점의 반지름이 독립적으로 변화
 *        ↓ cubicTo로 부드럽게 연결
 * [결과] 유기적으로 꿈틀거리는 블롭
 *
 * 공식:
 * point[i].radius = base + sin(time * speed + i * phaseOffset) * amp
 * ```
 *
 * 학습 목표:
 * 1. Path와 cubicTo로 부드러운 곡선
 * 2. 사인파를 이용한 유기적 변형
 * 3. 여러 제어점의 독립적 움직임
 * 4. 그라데이션과 그림자 효과
 */

// ============================================
// 기본 모핑 블롭
// ============================================
@Composable
fun BasicMorphingBlob(modifier: Modifier = Modifier) {
    var frameTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1a1a2e)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val time = frameTime / 1000f
            val centerX = size.width / 2
            val centerY = size.height / 2
            val baseRadius = size.minDimension / 2 * 0.7f

            val path = createBlobPath(
                centerX = centerX,
                centerY = centerY,
                baseRadius = baseRadius,
                time = time,
                points = 8,
                amplitude = baseRadius * 0.15f,
                speed = 2f,
                phaseOffset = 0.8f
            )

            drawPath(
                path = path,
                color = Color(0xFF6C63FF)
            )
        }
    }
}

private fun createBlobPath(
    centerX: Float,
    centerY: Float,
    baseRadius: Float,
    time: Float,
    points: Int,
    amplitude: Float,
    speed: Float,
    phaseOffset: Float
): Path {
    val path = Path()
    val angleStep = (2 * PI / points).toFloat()

    // 각 점의 위치 계산
    val pointList = mutableListOf<Offset>()
    for (i in 0 until points) {
        val angle = i * angleStep - PI.toFloat() / 2
        val phase = i * phaseOffset
        val radius = baseRadius + sin(time * speed + phase) * amplitude

        val x = centerX + cos(angle) * radius
        val y = centerY + sin(angle) * radius
        pointList.add(Offset(x, y))
    }

    // 부드러운 곡선으로 연결
    if (pointList.isNotEmpty()) {
        path.moveTo(pointList[0].x, pointList[0].y)

        for (i in pointList.indices) {
            val current = pointList[i]
            val next = pointList[(i + 1) % pointList.size]
            val nextNext = pointList[(i + 2) % pointList.size]

            val controlX1 = current.x + (next.x - pointList[(i - 1 + pointList.size) % pointList.size].x) / 4
            val controlY1 = current.y + (next.y - pointList[(i - 1 + pointList.size) % pointList.size].y) / 4
            val controlX2 = next.x - (nextNext.x - current.x) / 4
            val controlY2 = next.y - (nextNext.y - current.y) / 4

            path.cubicTo(controlX1, controlY1, controlX2, controlY2, next.x, next.y)
        }
    }

    path.close()
    return path
}

// ============================================
// 그라데이션 블롭
// ============================================
@Composable
fun GradientMorphingBlob(modifier: Modifier = Modifier) {
    var frameTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0f0f23)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(220.dp)) {
            val time = frameTime / 1000f
            val centerX = size.width / 2
            val centerY = size.height / 2
            val baseRadius = size.minDimension / 2 * 0.65f

            // 배경 블롭 (더 크고 투명)
            val bgPath = createBlobPath(
                centerX = centerX,
                centerY = centerY,
                baseRadius = baseRadius * 1.3f,
                time = time * 0.7f,
                points = 6,
                amplitude = baseRadius * 0.2f,
                speed = 1.5f,
                phaseOffset = 1f
            )

            drawPath(
                path = bgPath,
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF667eea).copy(alpha = 0.3f),
                        Color(0xFF764ba2).copy(alpha = 0.1f)
                    ),
                    center = Offset(centerX, centerY),
                    radius = baseRadius * 1.5f
                )
            )

            // 메인 블롭
            val mainPath = createBlobPath(
                centerX = centerX,
                centerY = centerY,
                baseRadius = baseRadius,
                time = time,
                points = 8,
                amplitude = baseRadius * 0.18f,
                speed = 2f,
                phaseOffset = 0.9f
            )

            drawPath(
                path = mainPath,
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF667eea),
                        Color(0xFF764ba2)
                    ),
                    center = Offset(centerX, centerY),
                    radius = baseRadius
                )
            )

            // 하이라이트
            val highlightPath = createBlobPath(
                centerX = centerX - baseRadius * 0.2f,
                centerY = centerY - baseRadius * 0.2f,
                baseRadius = baseRadius * 0.3f,
                time = time * 1.2f,
                points = 6,
                amplitude = baseRadius * 0.05f,
                speed = 2.5f,
                phaseOffset = 0.7f
            )

            drawPath(
                path = highlightPath,
                color = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

// ============================================
// 다중 블롭
// ============================================
@Composable
fun MultipleMorphingBlobs(modifier: Modifier = Modifier) {
    var frameTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    val blobConfigs = listOf(
        BlobConfig(Color(0xFFFF6B6B), 0.8f, Offset(-0.2f, -0.15f), 2.2f),
        BlobConfig(Color(0xFF4ECDC4), 0.7f, Offset(0.25f, 0.1f), 1.8f),
        BlobConfig(Color(0xFFFFE66D), 0.6f, Offset(-0.1f, 0.25f), 2.5f)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2C3E50)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val time = frameTime / 1000f
            val centerX = size.width / 2
            val centerY = size.height / 2
            val baseRadius = size.minDimension / 4

            blobConfigs.forEach { config ->
                val blobCenterX = centerX + config.offset.x * size.width * 0.3f
                val blobCenterY = centerY + config.offset.y * size.height * 0.3f

                val path = createBlobPath(
                    centerX = blobCenterX,
                    centerY = blobCenterY,
                    baseRadius = baseRadius * config.scale,
                    time = time,
                    points = 7,
                    amplitude = baseRadius * 0.15f * config.scale,
                    speed = config.speed,
                    phaseOffset = 0.9f
                )

                drawPath(
                    path = path,
                    color = config.color.copy(alpha = 0.7f)
                )
            }
        }
    }
}

private data class BlobConfig(
    val color: Color,
    val scale: Float,
    val offset: Offset,
    val speed: Float
)

// ============================================
// 조절 가능한 블롭
// ============================================
@Composable
fun AdjustableMorphingBlob(modifier: Modifier = Modifier) {
    var frameTime by remember { mutableLongStateOf(0L) }
    var points by remember { mutableFloatStateOf(8f) }
    var amplitude by remember { mutableFloatStateOf(0.15f) }
    var speed by remember { mutableFloatStateOf(2f) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
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
                            Color(0xFF1a1a2e),
                            Color(0xFF16213e)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(180.dp)) {
                val time = frameTime / 1000f
                val centerX = size.width / 2
                val centerY = size.height / 2
                val baseRadius = size.minDimension / 2 * 0.7f

                val path = createBlobPath(
                    centerX = centerX,
                    centerY = centerY,
                    baseRadius = baseRadius,
                    time = time,
                    points = points.toInt().coerceAtLeast(3),
                    amplitude = baseRadius * amplitude,
                    speed = speed,
                    phaseOffset = 0.8f
                )

                drawPath(
                    path = path,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFf093fb),
                            Color(0xFFf5576c)
                        )
                    )
                )
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
                    label = "제어점 수",
                    value = points,
                    onValueChange = { points = it },
                    valueRange = 3f..12f,
                    displayValue = "${points.toInt()}개"
                )

                SliderControl(
                    label = "변형 폭",
                    value = amplitude,
                    onValueChange = { amplitude = it },
                    valueRange = 0.05f..0.35f,
                    displayValue = "${(amplitude * 100).toInt()}%"
                )

                SliderControl(
                    label = "속도",
                    value = speed,
                    onValueChange = { speed = it },
                    valueRange = 0.5f..5f,
                    displayValue = String.format("%.1fx", speed)
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
                color = Color(0xFFf5576c)
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFf5576c),
                activeTrackColor = Color(0xFFf5576c)
            )
        )
    }
}

// ============================================
// 로딩 블롭
// ============================================
@Composable
fun LoadingBlob(modifier: Modifier = Modifier) {
    var frameTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E3A5F)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            val time = frameTime / 1000f
            val centerX = size.width / 2
            val centerY = size.height / 2
            val baseRadius = size.minDimension / 2 * 0.6f

            // 외곽 링
            val outerPath = createBlobPath(
                centerX = centerX,
                centerY = centerY,
                baseRadius = baseRadius * 1.3f,
                time = time,
                points = 6,
                amplitude = baseRadius * 0.1f,
                speed = 3f,
                phaseOffset = 1.2f
            )

            drawPath(
                path = outerPath,
                color = Color(0xFF4FC3F7).copy(alpha = 0.3f)
            )

            // 메인 블롭
            val mainPath = createBlobPath(
                centerX = centerX,
                centerY = centerY,
                baseRadius = baseRadius,
                time = time,
                points = 8,
                amplitude = baseRadius * 0.2f,
                speed = 2.5f,
                phaseOffset = 0.8f
            )

            drawPath(
                path = mainPath,
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF4FC3F7),
                        Color(0xFF0288D1)
                    ),
                    center = Offset(centerX, centerY),
                    radius = baseRadius
                )
            )
        }

        Text(
            text = "Loading...",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}

// ============================================
// 인터랙티브 블롭 (터치 반응)
// ============================================
@Composable
fun InteractiveBlob(modifier: Modifier = Modifier) {
    var frameTime by remember { mutableLongStateOf(0L) }
    var touchOffset by remember { mutableStateOf(Offset.Zero) }
    var isTouching by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { time ->
                frameTime = time
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2D3436))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isTouching = true
                        touchOffset = offset
                    },
                    onDragEnd = {
                        isTouching = false
                    },
                    onDrag = { change, _ ->
                        touchOffset = change.position
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val time = frameTime / 1000f
            val centerX = if (isTouching) touchOffset.x else size.width / 2
            val centerY = if (isTouching) touchOffset.y else size.height / 2
            val baseRadius = size.minDimension / 4
            val currentAmplitude = if (isTouching) 0.25f else 0.15f
            val currentSpeed = if (isTouching) 4f else 2f

            val path = createBlobPath(
                centerX = centerX,
                centerY = centerY,
                baseRadius = baseRadius,
                time = time,
                points = 10,
                amplitude = baseRadius * currentAmplitude,
                speed = currentSpeed,
                phaseOffset = 0.6f
            )

            drawPath(
                path = path,
                brush = Brush.radialGradient(
                    colors = if (isTouching) {
                        listOf(Color(0xFFFF6B6B), Color(0xFFEE5A24))
                    } else {
                        listOf(Color(0xFF74b9ff), Color(0xFF0984e3))
                    },
                    center = Offset(centerX, centerY),
                    radius = baseRadius * 1.2f
                )
            )
        }

        Text(
            text = if (isTouching) "🔥 Active!" else "터치하여 이동",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun MorphingBlobDemo() {
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
            text = "Morphing Blob",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "기본 모핑 블롭") {
            BasicMorphingBlob()
        }

        DemoSection(title = "그라데이션 블롭") {
            GradientMorphingBlob()
        }

        DemoSection(title = "다중 블롭") {
            MultipleMorphingBlobs()
        }

        DemoSection(title = "조절 가능한 블롭") {
            AdjustableMorphingBlob()
        }

        DemoSection(title = "로딩 블롭") {
            LoadingBlob()
        }

        DemoSection(title = "인터랙티브 블롭") {
            InteractiveBlob()
        }

        MorphingBlobGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MorphingBlobGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Morphing Blob 가이드")

            CodeSection(
                title = "블롭 경로 생성",
                code = """
for (i in 0 until points) {
    val angle = i * angleStep
    val phase = i * phaseOffset
    val radius = base + sin(time * speed + phase) * amp
    
    val x = centerX + cos(angle) * radius
    val y = centerY + sin(angle) * radius
}
                """.trimIndent()
            )

            CodeSection(
                title = "부드러운 곡선 연결",
                code = """
path.cubicTo(
    controlX1, controlY1,  // 첫 번째 제어점
    controlX2, controlY2,  // 두 번째 제어점
    nextX, nextY           // 끝점
)
                """.trimIndent()
            )

            FeatureSection(
                features = """
- 제어점 수가 많을수록 부드러움
- phaseOffset으로 각 점의 독립적 움직임
- cubicTo로 자연스러운 곡선
- 그라데이션으로 입체감 추가
                """.trimIndent(),
                type = FeatureTextType.TIP
            )

            FeatureSection(
                features = """
- points는 최소 3개 이상
- amplitude가 너무 크면 형태 왜곡
- frameTime으로 recomposition 트리거
                """.trimIndent(),
                type = FeatureTextType.CAUTION
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 2200)
@Composable
private fun MorphingBlobDemoPreview() {
    MorphingBlobDemo()
}
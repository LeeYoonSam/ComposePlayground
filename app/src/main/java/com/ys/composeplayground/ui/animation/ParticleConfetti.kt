package com.ys.composeplayground.ui.animation

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * 🔴 Expert #16: Particle Confetti (파티클 폭죽)
 *
 * 📖 핵심 개념
 *
 * Canvas와 LaunchedEffect를 사용하여 수많은 파티클이 폭발하듯 퍼지는 축하 효과를 만듭니다. 각 파티클은 독립적인 위치, 속도, 색상, 크기를 가집니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * Canvas | 파티클 그리기
 * LaunchedEffect | 애니메이션 루프
 * withFrameMillis | 프레임별 업데이트
 * Random | 랜덤 파티클 생성
 * data class | 파티클 상태 관리
 *
 * 💡 동작 원리
 *
 * ```
 * [트리거] 버튼 클릭/이벤트 발생
 *        ↓ 파티클 N개 생성
 * [생성] 각 파티클에 랜덤 속성 부여
 *        - 위치, 속도, 각도, 색상, 크기
 *        ↓ 애니메이션 루프 시작
 * [업데이트] 매 프레임마다:
 *        - 위치 += 속도
 *        - 속도.y += 중력
 *        - alpha 감소
 *        ↓ 파티클 소멸 조건 체크
 * [종료] alpha <= 0 또는 화면 밖
 *
 * 물리 공식:
 * - position += velocity * deltaTime
 * - velocity.y += gravity
 * - alpha -= fadeRate
 * ```
 *
 * 학습 목표:
 * 1. Canvas로 파티클 그리기
 * 2. 물리 기반 애니메이션 (중력, 속도)
 * 3. 대량 파티클 상태 관리
 * 4. 다양한 파티클 모양
 */

// ============================================
// 파티클 데이터 클래스
// ============================================
data class ConfettiParticle(
    var x: Float,
    var y: Float,
    var velocityX: Float,
    var velocityY: Float,
    var alpha: Float = 1f,
    var rotation: Float = 0f,
    var rotationSpeed: Float = 0f,
    var size: Float,
    val color: Color,
    val shape: ParticleShape = ParticleShape.CIRCLE
)

enum class ParticleShape {
    CIRCLE,
    RECTANGLE,
    STAR,
    TRIANGLE
}

// ============================================
// 기본 폭죽 효과
// ============================================
@Composable
fun BasicConfetti(modifier: Modifier = Modifier) {
    val particles = remember { mutableStateListOf<ConfettiParticle>() }
    var isActive by remember { mutableStateOf(false) }
    var lastFrameTime by remember { mutableLongStateOf(0L) }

    val confettiColors = listOf(
        Color(0xFFE91E63),
        Color(0xFF9C27B0),
        Color(0xFF673AB7),
        Color(0xFF3F51B5),
        Color(0xFF2196F3),
        Color(0xFF00BCD4),
        Color(0xFF4CAF50),
        Color(0xFFFFEB3B),
        Color(0xFFFF9800),
        Color(0xFFFF5722)
    )

    // 애니메이션 루프
    LaunchedEffect(isActive) {
        if (isActive) {
            lastFrameTime = 0L
            while (isActive && particles.isNotEmpty()) {
                withFrameMillis { frameTime ->
                    if (lastFrameTime == 0L) lastFrameTime = frameTime
                    val deltaTime = ((frameTime - lastFrameTime) / 16f).coerceIn(0.5f, 2f)
                    lastFrameTime = frameTime

                    val iterator = particles.iterator()
                    while (iterator.hasNext()) {
                        val particle = iterator.next()
                        // 위치 업데이트
                        particle.x += particle.velocityX * deltaTime
                        particle.y += particle.velocityY * deltaTime
                        // 중력 적용
                        particle.velocityY += 0.3f * deltaTime
                        // 회전
                        particle.rotation += particle.rotationSpeed * deltaTime
                        // 페이드 아웃
                        particle.alpha -= 0.008f * deltaTime

                        // 제거 조건
                        if (particle.alpha <= 0f || particle.y > 1000f) {
                            iterator.remove()
                        }
                    }

                    if (particles.isEmpty()) {
                        isActive = false
                    }
                }
            }
        }
    }

    fun explode(centerX: Float, centerY: Float) {
        particles.clear()
        repeat(100) {
            val angle = Random.nextFloat() * 2 * PI.toFloat()
            val speed = Random.nextFloat() * 12f + 4f
            particles.add(
                ConfettiParticle(
                    x = centerX,
                    y = centerY,
                    velocityX = cos(angle) * speed,
                    velocityY = sin(angle) * speed - 8f,
                    size = Random.nextFloat() * 8f + 4f,
                    color = confettiColors.random(),
                    rotation = Random.nextFloat() * 360f,
                    rotationSpeed = Random.nextFloat() * 10f - 5f,
                    shape = ParticleShape.entries.random()
                )
            )
        }
        isActive = true
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { particle ->
                drawParticle(particle)
            }
        }

        Button(
            onClick = { explode(500f, 400f) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
        ) {
            Icon(
                imageVector = Icons.Default.Celebration,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text("Celebrate!")
        }

        Text(
            text = "Particles: ${particles.size}",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )
    }
}

private fun DrawScope.drawParticle(particle: ConfettiParticle) {
    val color = particle.color.copy(alpha = particle.alpha.coerceIn(0f, 1f))

    when (particle.shape) {
        ParticleShape.CIRCLE -> {
            drawCircle(
                color = color,
                radius = particle.size,
                center = Offset(particle.x, particle.y)
            )
        }
        ParticleShape.RECTANGLE -> {
            rotate(particle.rotation, Offset(particle.x, particle.y)) {
                drawRect(
                    color = color,
                    topLeft = Offset(particle.x - particle.size, particle.y - particle.size / 2),
                    size = Size(particle.size * 2, particle.size)
                )
            }
        }
        ParticleShape.STAR -> {
            rotate(particle.rotation, Offset(particle.x, particle.y)) {
                drawStar(particle.x, particle.y, particle.size, color)
            }
        }
        ParticleShape.TRIANGLE -> {
            rotate(particle.rotation, Offset(particle.x, particle.y)) {
                drawTriangle(particle.x, particle.y, particle.size, color)
            }
        }
    }
}

private fun DrawScope.drawStar(cx: Float, cy: Float, size: Float, color: Color) {
    val path = Path()
    val points = 5
    val outerRadius = size
    val innerRadius = size * 0.5f

    for (i in 0 until points * 2) {
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val angle = (i * PI / points - PI / 2).toFloat()
        val x = cx + radius * cos(angle)
        val y = cy + radius * sin(angle)

        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }

    path.close()
    drawPath(path, color)
}

private fun DrawScope.drawTriangle(cx: Float, cy: Float, size: Float, color: Color) {
    val path = Path()
    path.moveTo(cx, cy - size)
    path.lineTo(cx - size, cy + size)
    path.lineTo(cx + size, cy + size)
    path.close()
    drawPath(path, color)
}

// ============================================
// 연속 폭죽 (Continuous)
// ============================================
@Composable
fun ContinuousConfetti(modifier: Modifier = Modifier) {
    val particles = remember { mutableStateListOf<ConfettiParticle>() }
    var isRunning by remember { mutableStateOf(false) }
    var lastSpawnTime by remember { mutableLongStateOf(0L) }

    val confettiColors = listOf(
        Color(0xFFFFD700),
        Color(0xFFFFA500),
        Color(0xFFFF6347),
        Color(0xFFFF1493),
        Color(0xFF00CED1)
    )

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning) {
                withFrameMillis { frameTime ->
                    // 일정 간격으로 파티클 생성
                    if (frameTime - lastSpawnTime > 50) {
                        lastSpawnTime = frameTime
                        repeat(3) {
                            particles.add(
                                ConfettiParticle(
                                    x = Random.nextFloat() * 800f + 100f,
                                    y = -20f,
                                    velocityX = Random.nextFloat() * 4f - 2f,
                                    velocityY = Random.nextFloat() * 2f + 1f,
                                    size = Random.nextFloat() * 6f + 3f,
                                    color = confettiColors.random(),
                                    rotation = Random.nextFloat() * 360f,
                                    rotationSpeed = Random.nextFloat() * 6f - 3f,
                                    shape = ParticleShape.entries.random()
                                )
                            )
                        }
                    }

                    // 파티클 업데이트
                    val iterator = particles.iterator()
                    while (iterator.hasNext()) {
                        val particle = iterator.next()
                        particle.x += particle.velocityX
                        particle.y += particle.velocityY
                        particle.velocityY += 0.1f
                        particle.velocityX *= 0.99f
                        particle.rotation += particle.rotationSpeed

                        if (particle.y > 600f) {
                            iterator.remove()
                        }
                    }
                }
            }
        } else {
            particles.clear()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color(0xFF16213E)
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { particle ->
                drawParticle(particle)
            }
        }

        Button(
            onClick = { isRunning = !isRunning },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRunning) Color(0xFFE74C3C) else Color(0xFF2ECC71)
            )
        ) {
            Text(if (isRunning) "Stop" else "Start Rain")
        }
    }
}

// ============================================
// 폭발 효과 (Burst from point)
// ============================================
@Composable
fun BurstConfetti(modifier: Modifier = Modifier) {
    val particles = remember { mutableStateListOf<ConfettiParticle>() }
    var isActive by remember { mutableStateOf(false) }

    val burstColors = listOf(
        Color(0xFFE91E63),
        Color(0xFFFF4081),
        Color(0xFFFF80AB),
        Color(0xFFF8BBD9)
    )

    LaunchedEffect(isActive) {
        if (isActive) {
            while (particles.isNotEmpty()) {
                withFrameMillis {
                    val iterator = particles.iterator()
                    while (iterator.hasNext()) {
                        val particle = iterator.next()
                        particle.x += particle.velocityX
                        particle.y += particle.velocityY
                        particle.velocityY += 0.2f
                        particle.velocityX *= 0.98f
                        particle.alpha -= 0.015f
                        particle.rotation += particle.rotationSpeed
                        particle.size *= 0.995f

                        if (particle.alpha <= 0f) {
                            iterator.remove()
                        }
                    }

                    if (particles.isEmpty()) {
                        isActive = false
                    }
                }
            }
        }
    }

    fun burst(x: Float, y: Float) {
        particles.clear()
        repeat(50) {
            val angle = Random.nextFloat() * 2 * PI.toFloat()
            val speed = Random.nextFloat() * 15f + 5f
            particles.add(
                ConfettiParticle(
                    x = x,
                    y = y,
                    velocityX = cos(angle) * speed,
                    velocityY = sin(angle) * speed,
                    size = Random.nextFloat() * 10f + 5f,
                    color = burstColors.random(),
                    rotation = Random.nextFloat() * 360f,
                    rotationSpeed = Random.nextFloat() * 15f - 7.5f,
                    shape = ParticleShape.CIRCLE
                )
            )
        }
        isActive = true
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2C3E50))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        burst(offset.x, offset.y)
                    }
                }
        ) {
            particles.forEach { particle ->
                drawCircle(
                    color = particle.color.copy(alpha = particle.alpha.coerceIn(0f, 1f)),
                    radius = particle.size,
                    center = Offset(particle.x, particle.y)
                )
            }
        }

        Text(
            text = "탭하여 폭발!",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

// ============================================
// 무지개 분수 효과
// ============================================
@Composable
fun RainbowFountain(modifier: Modifier = Modifier) {
    val particles = remember { mutableStateListOf<ConfettiParticle>() }
    var isRunning by remember { mutableStateOf(false) }

    val rainbowColors = listOf(
        Color(0xFFFF0000),
        Color(0xFFFF7F00),
        Color(0xFFFFFF00),
        Color(0xFF00FF00),
        Color(0xFF0000FF),
        Color(0xFF4B0082),
        Color(0xFF9400D3)
    )

    var colorIndex by remember { mutableStateOf(0) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            var frameCount = 0

            while (isRunning) {
                withFrameMillis {
                    frameCount++

                    // 매 2프레임마다 파티클 생성
                    if (frameCount % 2 == 0) {
                        val baseAngle = -PI.toFloat() / 2
                        val spreadAngle = PI.toFloat() / 6

                        repeat(2) {
                            val angle = baseAngle + Random.nextFloat() * spreadAngle - spreadAngle / 2
                            val speed = Random.nextFloat() * 8f + 10f

                            particles.add(
                                ConfettiParticle(
                                    x = 500f,
                                    y = 450f,
                                    velocityX = cos(angle) * speed,
                                    velocityY = sin(angle) * speed,
                                    size = Random.nextFloat() * 4f + 3f,
                                    color = rainbowColors[colorIndex % rainbowColors.size],
                                    shape = ParticleShape.CIRCLE
                                )
                            )
                        }
                        colorIndex++
                    }

                    // 업데이트
                    val iterator = particles.iterator()
                    while (iterator.hasNext()) {
                        val particle = iterator.next()
                        particle.x += particle.velocityX
                        particle.y += particle.velocityY
                        particle.velocityY += 0.4f
                        particle.alpha -= 0.008f

                        if (particle.alpha <= 0f || particle.y > 500f) {
                            iterator.remove()
                        }
                    }
                }
            }
        } else {
            particles.clear()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F0F23))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { particle ->
                drawCircle(
                    color = particle.color.copy(alpha = particle.alpha.coerceIn(0f, 1f)),
                    radius = particle.size,
                    center = Offset(particle.x, particle.y)
                )
            }

            // 분수대 베이스
            drawCircle(
                color = Color(0xFF34495E),
                radius = 30f,
                center = Offset(500f, 470f)
            )
        }

        Button(
            onClick = { isRunning = !isRunning },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRunning) Color(0xFFE74C3C) else Color(0xFF9B59B6)
            )
        ) {
            Text(if (isRunning) "Stop" else "Start Fountain")
        }
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun ParticleConfettiDemo() {
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
            text = "Particle Confetti",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "기본 폭죽 효과") {
            BasicConfetti()
        }

        DemoSection(title = "연속 파티클 (Rain)") {
            ContinuousConfetti()
        }

        DemoSection(title = "터치 폭발 효과") {
            BurstConfetti()
        }

        DemoSection(title = "무지개 분수") {
            RainbowFountain()
        }

        ParticleConfettiGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ParticleConfettiGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Particle Confetti 가이드")

            CodeSection(
                title = "파티클 데이터 구조",
                code = """
data class Particle(
    var x: Float,
    var y: Float,
    var velocityX: Float,
    var velocityY: Float,
    var alpha: Float,
    val color: Color
)
                """.trimIndent()
            )

            CodeSection(
                title = "물리 업데이트",
                code = """
// 매 프레임
particle.x += particle.velocityX
particle.y += particle.velocityY
particle.velocityY += gravity  // 중력
particle.alpha -= fadeRate     // 페이드
                """.trimIndent()
            )

            FeatureSection(
                features = """
- withFrameMillis로 프레임 동기화
- mutableStateListOf로 파티클 관리
- coerceIn으로 alpha 범위 제한
- iterator.remove()로 안전한 삭제
                """.trimIndent(),
                type = FeatureTextType.TIP
            )

            FeatureSection(
                features = """
- 너무 많은 파티클은 성능 저하
- 화면 밖 파티클은 즉시 제거
- alpha <= 0 체크 필수
                """.trimIndent(),
                type = FeatureTextType.CAUTION
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
private fun ParticleConfettiDemoPreview() {
    ParticleConfettiDemo()
}
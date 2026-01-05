package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * 🔴 Expert #19: Liquid Swipe (액체 스와이프)
 *
 * 📖 핵심 개념
 *
 * 스와이프 시 액체가 흐르듯이 화면이 전환되는 효과입니다. 베지어 곡선으로 물결 모양의 경계를 만들고, 드래그에 따라 유동적으로 변형됩니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * Canvas | 액체 모양 그리기
 * Path | 물결 경로 정의
 * cubicTo / quadraticBezierTo | 부드러운 곡선
 * clipPath | 경로로 클리핑
 * detectHorizontalDragGestures | 스와이프 감지
 * Animatable | 스와이프 후 애니메이션
 *
 * 💡 동작 원리
 *
 * ```
 * [대기] 초기 상태 (직선 경계)
 *        ↓ 드래그 시작
 * [드래그 중]
 *        - 드래그 위치에 따라 곡선 변형
 *        - 중앙이 가장 많이 밀려남
 *        - 위/아래는 덜 밀려남
 *        ↓ 드래그 종료
 * [스냅] threshold 초과 시:
 *        - 다음 페이지로 애니메이션
 *        threshold 미만 시:
 *        - 원위치로 복귀
 *
 * 곡선 공식:
 * controlX = dragX * waveFactor
 * waveY = centerY + sin(progress) * amplitude
 * ```
 *
 * 학습 목표:
 * 1. Path로 액체 모양 경계 만들기
 * 2. clipPath로 콘텐츠 클리핑
 * 3. 드래그에 따른 곡선 변형
 * 4. 페이지 전환 애니메이션
 */

// ============================================
// 페이지 데이터
// ============================================
data class LiquidPage(
    val backgroundColor: Color,
    val title: String,
    val description: String,
    val icon: ImageVector
)

// ============================================
// 기본 Liquid Swipe
// ============================================
@Composable
fun BasicLiquidSwipe(modifier: Modifier = Modifier) {
    val pages = listOf(
        LiquidPage(
            backgroundColor = Color(0xFF6C63FF),
            title = "Welcome",
            description = "스와이프하여 시작하세요",
            icon = Icons.Default.Star
        ),
        LiquidPage(
            backgroundColor = Color(0xFFFF6B6B),
            title = "Discover",
            description = "새로운 기능을 발견하세요",
            icon = Icons.Default.Favorite
        ),
        LiquidPage(
            backgroundColor = Color(0xFF4ECDC4),
            title = "Complete",
            description = "준비가 완료되었습니다",
            icon = Icons.Default.Check
        )
    )

    var currentPage by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    val dragProgress = remember { Animatable(0f) }
    val waveHeight = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(RoundedCornerShape(16.dp))
            .pointerInput(currentPage) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        scope.launch {
                            waveHeight.snapTo(size.height / 2f)
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            val threshold = size.width * 0.4f
                            if (abs(dragProgress.value) > threshold) {
                                // 다음/이전 페이지로
                                if (dragProgress.value < 0 && currentPage < pages.size - 1) {
                                    dragProgress.animateTo(
                                        -size.width.toFloat(),
                                        spring(stiffness = Spring.StiffnessLow)
                                    )
                                    currentPage++
                                    dragProgress.snapTo(0f)
                                } else if (dragProgress.value > 0 && currentPage > 0) {
                                    dragProgress.animateTo(
                                        size.width.toFloat(),
                                        spring(stiffness = Spring.StiffnessLow)
                                    )
                                    currentPage--
                                    dragProgress.snapTo(0f)
                                } else {
                                    dragProgress.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
                                }
                            } else {
                                // 원위치로
                                dragProgress.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
                            }
                            waveHeight.animateTo(0f, spring(stiffness = Spring.StiffnessLow))
                        }
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            val newProgress = dragProgress.value + dragAmount
                            // 첫 페이지에서 오른쪽, 마지막 페이지에서 왼쪽 제한
                            val bounded = when {
                                currentPage == 0 && newProgress > 0 -> newProgress * 0.3f
                                currentPage == pages.size - 1 && newProgress < 0 -> newProgress * 0.3f
                                else -> newProgress
                            }
                            dragProgress.snapTo(bounded)

                            // 드래그 중 wave height 업데이트
                            val targetWaveHeight = size.height / 2f + abs(dragProgress.value) * 0.2f
                            waveHeight.snapTo(targetWaveHeight.coerceAtMost(size.height * 0.8f))
                        }
                    }
                )
            }
    ) {
        // 현재 페이지 (배경)
        PageContent(
            page = pages[currentPage],
            modifier = Modifier.fillMaxSize()
        )

        // 다음 페이지 (클리핑된 오버레이)
        if (dragProgress.value < 0 && currentPage < pages.size - 1) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = createLiquidPath(
                    width = size.width,
                    height = size.height,
                    progress = -dragProgress.value / size.width,
                    waveHeight = waveHeight.value,
                    fromRight = true
                )

                clipPath(path) {
                    drawRect(pages[currentPage + 1].backgroundColor)
                }
            }

            // 클리핑된 콘텐츠
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = createLiquidPath(
                        width = size.width,
                        height = size.height,
                        progress = -dragProgress.value / size.width,
                        waveHeight = waveHeight.value,
                        fromRight = true
                    )
                    clipPath(path) {
                        drawRect(pages[currentPage + 1].backgroundColor)
                    }
                }
            }
        }

        // 이전 페이지 (왼쪽에서)
        if (dragProgress.value > 0 && currentPage > 0) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = createLiquidPath(
                    width = size.width,
                    height = size.height,
                    progress = dragProgress.value / size.width,
                    waveHeight = waveHeight.value,
                    fromRight = false
                )

                clipPath(path) {
                    drawRect(pages[currentPage - 1].backgroundColor)
                }
            }
        }

        // 페이지 인디케이터
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pages.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(if (index == currentPage) 24.dp else 8.dp, 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (index == currentPage) Color.White
                            else Color.White.copy(alpha = 0.5f)
                        )
                )
            }
        }

        // 스와이프 힌트
        if (currentPage < pages.size - 1) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .size(32.dp)
            )
        }
    }
}

@Composable
private fun PageContent(
    page: LiquidPage,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(page.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = page.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = page.description,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun createLiquidPath(
    width: Float,
    height: Float,
    progress: Float,
    waveHeight: Float,
    fromRight: Boolean
): Path {
    val path = Path()
    val clampedProgress = progress.coerceIn(0f, 1f)

    if (fromRight) {
        // 오른쪽에서 왼쪽으로
        val startX = width - (width * clampedProgress * 1.2f)
        val controlX = startX - waveHeight * 0.5f * clampedProgress

        path.moveTo(width, 0f)
        path.lineTo(startX.coerceAtLeast(0f), 0f)

        // 상단에서 중앙으로 베지어 곡선
        path.cubicTo(
            controlX, height * 0.25f,
            controlX - waveHeight * 0.3f * clampedProgress, height * 0.4f,
            controlX - waveHeight * 0.4f * clampedProgress, height * 0.5f
        )

        // 중앙에서 하단으로 베지어 곡선
        path.cubicTo(
            controlX - waveHeight * 0.3f * clampedProgress, height * 0.6f,
            controlX, height * 0.75f,
            startX.coerceAtLeast(0f), height
        )

        path.lineTo(width, height)
        path.close()
    } else {
        // 왼쪽에서 오른쪽으로
        val endX = width * clampedProgress * 1.2f
        val controlX = endX + waveHeight * 0.5f * clampedProgress

        path.moveTo(0f, 0f)
        path.lineTo(endX.coerceAtMost(width), 0f)

        path.cubicTo(
            controlX, height * 0.25f,
            controlX + waveHeight * 0.3f * clampedProgress, height * 0.4f,
            controlX + waveHeight * 0.4f * clampedProgress, height * 0.5f
        )

        path.cubicTo(
            controlX + waveHeight * 0.3f * clampedProgress, height * 0.6f,
            controlX, height * 0.75f,
            endX.coerceAtMost(width), height
        )

        path.lineTo(0f, height)
        path.close()
    }

    return path
}

// ============================================
// 수직 Liquid Swipe
// ============================================
@Composable
fun VerticalLiquidSwipe(modifier: Modifier = Modifier) {
    val colors = listOf(
        Color(0xFF667eea),
        Color(0xFFf093fb),
        Color(0xFF4facfe)
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val dragProgress = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(16.dp))
            .pointerInput(currentIndex) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        scope.launch {
                            val threshold = size.height * 0.3f
                            if (abs(dragProgress.value) > threshold) {
                                if (dragProgress.value < 0 && currentIndex < colors.size - 1) {
                                    dragProgress.animateTo(
                                        targetValue = -size.height.toFloat(),
                                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                                    )
                                    currentIndex++
                                    dragProgress.snapTo(0f)
                                } else if (dragProgress.value > 0 && currentIndex > 0) {
                                    dragProgress.animateTo(
                                        targetValue = size.height.toFloat(),
                                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                                    )
                                    currentIndex--
                                    dragProgress.snapTo(0f)
                                }
                            } else {
                                dragProgress.animateTo(0f)
                            }
                        }
                    },
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            val newProgress = dragProgress.value + dragAmount
                            val bounded = when (currentIndex) {
                                0 if newProgress > 0 -> newProgress * 0.3f
                                colors.size - 1 if newProgress < 0 -> newProgress * 0.3f
                                else -> newProgress
                            }
                            dragProgress.snapTo(bounded)
                        }
                    }
                )
            }
    ) {
        // 현재 색상
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors[currentIndex])
        )

        // 다음 색상 (위에서 내려옴)
        if (dragProgress.value < 0 && currentIndex < colors.size - 1) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val progress = -dragProgress.value / size.height
                val path = createVerticalLiquidPath(
                    width = size.width,
                    height = size.height,
                    progress = progress.coerceIn(0f, 1f),
                    fromTop = true
                )
                clipPath(path) {
                    drawRect(colors[currentIndex + 1])
                }
            }
        }

        // 이전 색상 (아래에서 올라옴)
        if (dragProgress.value > 0 && currentIndex > 0) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val progress = dragProgress.value / size.height
                val path = createVerticalLiquidPath(
                    width = size.width,
                    height = size.height,
                    progress = progress.coerceIn(0f, 1f),
                    fromTop = false
                )
                clipPath(path) {
                    drawRect(colors[currentIndex - 1])
                }
            }
        }

        // 인덱스 표시
        Text(
            text = "${currentIndex + 1} / ${colors.size}",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
        )

        Text(
            text = "↕ 위아래로 스와이프",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

private fun createVerticalLiquidPath(
    width: Float,
    height: Float,
    progress: Float,
    fromTop: Boolean
): Path {
    val path = Path()
    val waveAmplitude = width * 0.15f * progress

    if (fromTop) {
        val bottomY = height * progress * 1.2f

        path.moveTo(0f, 0f)
        path.lineTo(width, 0f)
        path.lineTo(width, bottomY)

        path.cubicTo(
            x1 = width * 0.75f, y1 = bottomY + waveAmplitude,
            x2 = width * 0.5f, y2 = bottomY + waveAmplitude * 1.5f,
            x3 = width * 0.25f, y3 = bottomY + waveAmplitude
        )

        path.cubicTo(
            x1 = width * 0.1f, y1 = bottomY + waveAmplitude * 0.5f,
            x2 = 0f, y2 = bottomY,
            x3 = 0f, y3 = bottomY
        )

        path.close()
    } else {
        val topY = height - (height * progress * 1.2f)

        path.moveTo(0f, height)
        path.lineTo(width, height)
        path.lineTo(width, topY)

        path.cubicTo(
            x1 = width * 0.75f, y1 = topY - waveAmplitude,
            x2 = width * 0.5f, y2 = topY - waveAmplitude * 1.5f,
            x3 = width * 0.25f, y3 = topY - waveAmplitude
        )

        path.cubicTo(
            x1 = width * 0.1f, y1 = topY - waveAmplitude * 0.5f,
            x2 = 0f, y2 = topY,
            x3 = 0f, y3 = topY
        )

        path.close()
    }

    return path
}

// ============================================
// 원형 Liquid Reveal
// ============================================
@Composable
fun CircularLiquidReveal(modifier: Modifier = Modifier) {
    val colors = listOf(
        Color(0xFFE91E63),
        Color(0xFF9C27B0),
        Color(0xFF3F51B5),
        Color(0xFF00BCD4)
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val revealProgress = remember { Animatable(0f) }
    var touchPoint by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .pointerInput(currentIndex) {
                detectTapGestures { offset ->
                    touchPoint = offset
                    scope.launch {
                        revealProgress.snapTo(0f)
                        revealProgress.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        currentIndex = (currentIndex + 1) % colors.size
                        revealProgress.snapTo(0f)
                    }
                }
            }
    ) {
        // 현재 색상
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors[currentIndex])
        )

        // 다음 색상 (원형 reveal)
        if (revealProgress.value > 0f) {
            val nextIndex = (currentIndex + 1) % colors.size

            Canvas(modifier = Modifier.fillMaxSize()) {
                val maxRadius = sqrt(
                    (size.width * size.width + size.height * size.height).toDouble()
                ).toFloat()

                val currentRadius = maxRadius * revealProgress.value

                val path = Path().apply {
                    addOval(
                        Rect(
                            center = touchPoint,
                            radius = currentRadius
                        )
                    )
                }

                clipPath(path) {
                    drawRect(colors[nextIndex])
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "탭하여 전환",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${currentIndex + 1} / ${colors.size}",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun LiquidSwipeDemo() {
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
            text = "Liquid Swipe",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "기본 Liquid Swipe") {
            BasicLiquidSwipe()
        }

        DemoSection(title = "수직 Liquid Swipe") {
            VerticalLiquidSwipe()
        }

        DemoSection(title = "원형 Liquid Reveal") {
            CircularLiquidReveal()
        }

        LiquidSwipeGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LiquidSwipeGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Liquid Swipe 가이드")

            CodeSection(
                title = "액체 경로 생성",
                code = """
path.moveTo(width, 0f)
path.lineTo(startX, 0f)

// 베지어 곡선으로 물결 모양
path.cubicTo(
    controlX, height * 0.25f,
    controlX - wave, height * 0.5f,
    controlX, height * 0.75f
)

path.lineTo(width, height)
path.close()
                """.trimIndent()
            )

            CodeSection(
                title = "clipPath로 콘텐츠 클리핑",
                code = """
Canvas(modifier) {
    val path = createLiquidPath(...)
    
    clipPath(path) {
        drawRect(nextPageColor)
        // 또는 다른 콘텐츠 그리기
    }
}
                """.trimIndent()
            )

            FeatureSection(
                features = """
- cubicTo로 부드러운 물결 곡선
- clipPath로 다음 페이지 클리핑
- 드래그 진행률에 따른 곡선 변형
- threshold로 페이지 전환 판단
                """.trimIndent(),
                type = FeatureTextType.TIP
            )

            FeatureSection(
                features = """
- progress는 0~1 범위로 제한
- 첫/마지막 페이지 경계 처리
- 애니메이션 완료 후 상태 리셋
                """.trimIndent(),
                type = FeatureTextType.CAUTION
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1600)
@Composable
private fun LiquidSwipeDemoPreview() {
    LiquidSwipeDemo()
}
package com.ys.composeplayground.ui.animation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/**
 * 🟠 Advanced #14: Morphing Shape (도형 변환)
 *
 * 📖 핵심 개념
 *
 * RoundedCornerShape의 corner radius를 애니메이션하거나, GenericShape/Path를 사용하여 도형의 정점(vertex)을 보간합니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * animateFloatAsState | corner radius 애니메이션
 * RoundedCornerShape | 원 ↔ 사각형 변환
 * GenericShape | 커스텀 도형 정의
 * lerp | 두 값 사이 보간
 * Path | 복잡한 도형 그리기
 *
 * 학습 목표:
 * 1. RoundedCornerShape로 원 ↔ 사각형 변환
 * 2. Path를 사용한 커스텀 도형 모핑
 * 3. 여러 도형 간 전환 애니메이션
 */

// ============================================
// 원 ↔ 사각형 변환 (Corner Radius)
// ============================================
@Composable
fun CircleToSquareMorph(modifier: Modifier = Modifier) {
    var isCircle by remember { mutableStateOf(true) }

    val cornerPercent by animateFloatAsState(
        targetValue = if (isCircle) 50f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cornerPercent"
    )

    val color by animateColorAsState(
        targetValue = if (isCircle) Color(0xFF2196F3) else Color(0xFF4CAF50),
        animationSpec = tween(300),
        label = "color"
    )

    val rotation by animateFloatAsState(
        targetValue = if (isCircle) 0f else 45f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "rotation"
    )

    val safeCornerPercent = cornerPercent.coerceIn(0f, 50f).toInt()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer { rotationZ = rotation }
                .clip(RoundedCornerShape(safeCornerPercent))
                .background(color)
                .clickable { isCircle = !isCircle },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isCircle) "●" else "■",
                fontSize = 32.sp,
                color = Color.White,
                modifier = Modifier.graphicsLayer { rotationZ = -rotation }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "탭하여 변환 (Corner: ${cornerPercent.toInt()}%)",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

// ============================================
// 다중 도형 변환
// ============================================
@Composable
fun MultiShapeMorph(modifier: Modifier = Modifier) {
    var shapeIndex by remember { mutableIntStateOf(0) }

    val cornerPercent by animateFloatAsState(
        targetValue = when (shapeIndex) {
            0 -> 50f
            1 -> 25f
            2 -> 0f
            else -> 0f
        },
        animationSpec = tween(400),
        label = "multiCorner"
    )

    val rotation by animateFloatAsState(
        targetValue = if (shapeIndex == 3) 45f else 0f,
        animationSpec = tween(400),
        label = "multiRotation"
    )

    val color by animateColorAsState(
        targetValue = when (shapeIndex) {
            0 -> Color(0xFFE91E63)
            1 -> Color(0xFF9C27B0)
            2 -> Color(0xFF673AB7)
            else -> Color(0xFF3F51B5)
        },
        animationSpec = tween(400),
        label = "multiColor"
    )

    val safeCornerPercent = cornerPercent.coerceIn(0f, 50f).toInt()
    val shapeNames = listOf("원", "둥근 사각형", "사각형", "다이아몬드")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer { rotationZ = rotation }
                .clip(RoundedCornerShape(safeCornerPercent))
                .background(color)
                .clickable { shapeIndex = (shapeIndex + 1) % 4 },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${shapeIndex + 1}",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer { rotationZ = -rotation }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            shapeNames.forEachIndexed { index, name ->
                Text(
                    text = name,
                    fontSize = 11.sp,
                    color = if (index == shapeIndex) color else Color.Gray,
                    fontWeight = if (index == shapeIndex) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

// ============================================
// FAB 확장 모핑
// ============================================
@Composable
fun ExpandableFabMorph(modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(false) }

    val width by animateDpAsState(
        targetValue = if (isExpanded) 160.dp else 56.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "fabWidth"
    )

    val cornerRadius by animateDpAsState(
        targetValue = if (isExpanded) 16.dp else 28.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "fabCorner"
    )

    val iconRotation by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "fabIconRotation"
    )

    Box(
        modifier = modifier
            .width(width)
            .height(56.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color(0xFF6200EE))
            .clickable { isExpanded = !isExpanded },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.graphicsLayer { rotationZ = iconRotation }
            )

            if (isExpanded) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Create",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ============================================
// 별 모양 변환 (Path 기반)
// ============================================
@Composable
fun StarMorph(modifier: Modifier = Modifier) {
    var pointCount by remember { mutableIntStateOf(5) }

    val animatedPointCount by animateFloatAsState(
        targetValue = pointCount.toFloat(),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "starPoints"
    )

    val innerRadiusRatio by animateFloatAsState(
        targetValue = when (pointCount) {
            3 -> 0.4f
            4 -> 0.5f
            5 -> 0.5f
            6 -> 0.55f
            else -> 0.6f
        },
        animationSpec = tween(300),
        label = "innerRadius"
    )

    val color by animateColorAsState(
        targetValue = when (pointCount) {
            3 -> Color(0xFFFF5722)
            4 -> Color(0xFFFF9800)
            5 -> Color(0xFFFFEB3B)
            6 -> Color(0xFF8BC34A)
            else -> Color(0xFF00BCD4)
        },
        animationSpec = tween(300),
        label = "starColor"
    )

    val safePointCount = animatedPointCount.coerceIn(3f, 8f).toInt()
    val safeInnerRadiusRatio = innerRadiusRatio.coerceIn(0.2f, 0.8f)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .drawBehind {
                    val path = createStarPath(
                        center = Offset(size.width / 2, size.height / 2),
                        outerRadius = size.minDimension / 2 * 0.9f,
                        innerRadius = size.minDimension / 2 * 0.9f * safeInnerRadiusRatio,
                        points = safePointCount
                    )
                    drawPath(path, color)
                }
                .clickable {
                    pointCount = if (pointCount >= 8) 3 else pointCount + 1
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${pointCount}각",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (3..8).forEach { count ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (count == pointCount) color else Color.LightGray)
                        .clickable { pointCount = count },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$count",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

private fun createStarPath(
    center: Offset,
    outerRadius: Float,
    innerRadius: Float,
    points: Int
): Path {
    val path = Path()
    val safePoints = points.coerceAtLeast(3)
    val angleStep = Math.PI / safePoints

    for (i in 0 until safePoints * 2 ) {
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val angle = i * angleStep - Math.PI / 2

        val x = center.x + (radius * cos(angle)).toFloat()
        val y = center.y + (radius * sin(angle)).toFloat()

        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()
    return path
}

// ============================================
// 하트 ↔ 원 변환
// ============================================
@Composable
fun HeartCircleMorph(modifier: Modifier = Modifier) {
    var isHeart by remember { mutableStateOf(true) }

    val morphProgress by animateFloatAsState(
        targetValue = if (isHeart) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "heartMorph"
    )

    val scale by animateFloatAsState(
        targetValue = if (isHeart) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "heartScale"
    )

    val safeMorphProgress = morphProgress.coerceIn(0f, 1f)
    val safeScale = scale.coerceIn(0.5f, 1.5f)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer {
                    scaleX = safeScale
                    scaleY = safeScale
                }
                .drawBehind {
                    if (safeMorphProgress < 0.01f) {
                        // 완전한 원
                        drawCircle(
                            color = Color((0xFFE91E63)),
                            radius = size.minDimension / 2 * 0.8f
                        )
                    } else{
                        val heartPath = createHeartPath(size, safeMorphProgress)
                        drawPath(heartPath, Color(0xFFE91E63))
                    }
                }
                .clickable { isHeart = !isHeart },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isHeart) Icons.Default.Favorite else Icons.Default.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (isHeart) "하트" else "원",
            fontSize = 14.sp,
            color = Color(0xFFE91E63),
            fontWeight = FontWeight.Medium
        )
    }
}

private fun createHeartPath(size: Size, morphProgress: Float): Path {
    val path = Path()
    val width = size.width
    val height = size.height
    val centerX = width / 2
    val centerY = height / 2

    val safeMorph = morphProgress.coerceIn(0f, 1f)

    if (safeMorph < 0.01f) {
        // 완전한 원 - drawCircle 사용 권장
        return path
    }

    // 하트 파라미터
    val heartBottomY = height * 0.85f
    val heartTopY = height * 0.22f
    val heartDipY = height * 0.35f

    // 원 파라미터 (베지어로 원 근사)
    val radius = width * 0.4f
    val circleTop = centerY - radius
    val circleBottom = centerY + radius
    val circleLeft = centerX - radius
    val circleRight = centerX + radius
    val controlOffset = radius * 0.552f // 베지어로 원을 그리기 위한 매직 넘버

    // 보간된 값들
    val bottomY = lerp(circleBottom, heartBottomY, safeMorph)
    val topY = lerp(circleTop, heartTopY, safeMorph)
    val dipY = lerp(circleTop, heartDipY, safeMorph)

    // 시작점 (하단 중앙)
    path.moveTo(centerX, bottomY)

    // 왼쪽 하단 곡선
    path.cubicTo(
        lerp(centerX - controlOffset, width * 0.1f, safeMorph),
        lerp(circleBottom, height * 0.7f, safeMorph),
        lerp(circleLeft, width * 0.0f, safeMorph),
        lerp(centerY + controlOffset, height * 0.45f, safeMorph),
        lerp(circleLeft, width * 0.12f, safeMorph),
        lerp(centerY, height * 0.32f, safeMorph)
    )

    // 왼쪽 상단 곡선
    path.cubicTo(
        lerp(circleLeft, width * 0.12f, safeMorph),
        lerp(centerY - controlOffset, height * 0.18f, safeMorph),
        lerp(centerX - controlOffset, width * 0.28f, safeMorph),
        lerp(circleTop, topY, safeMorph),
        centerX,
        lerp(circleTop, dipY, safeMorph)
    )

    // 오른쪽 상단 곡선
    path.cubicTo(
        lerp(centerX + controlOffset, width * 0.72f, safeMorph),
        lerp(circleTop, topY, safeMorph),
        lerp(circleRight, width * 0.88f, safeMorph),
        lerp(centerY - controlOffset, height * 0.18f, safeMorph),
        lerp(circleRight, width * 0.88f, safeMorph),
        lerp(centerY, height * 0.32f, safeMorph)
    )

    // 오른쪽 하단 곡선
    path.cubicTo(
        lerp(circleRight, width * 1.0f, safeMorph),
        lerp(centerY + controlOffset, height * 0.45f, safeMorph),
        lerp(centerX + controlOffset, width * 0.9f, safeMorph),
        lerp(circleBottom, height * 0.7f, safeMorph),
        centerX,
        bottomY
    )

    path.close()
    return path
}

private fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start + (end - start) * fraction
}

// ============================================
// 로딩 도형 변환
// ============================================
@Composable
fun LoadingShapeMorph(modifier: Modifier = Modifier) {
    var shapeIndex by remember { mutableIntStateOf(0) }

    val cornerPercent by animateFloatAsState(
        targetValue = when (shapeIndex % 3) {
            0 -> 50f
            1 -> 15f
            else -> 0f
        },
        animationSpec = tween(400),
        label = "loadingCorner"
    )

    val rotation by animateFloatAsState(
        targetValue = shapeIndex * 120f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "loadingRotation"
    )

    val colors = listOf(
        Color(0xFFE91E63),
        Color(0xFF9C27B0),
        Color(0xFF2196F3)
    )

    val color by animateColorAsState(
        targetValue = colors[shapeIndex % 3],
        animationSpec = tween(400),
        label = "loadingColor"
    )

    val safeCornerPercent = cornerPercent.coerceIn(0f, 50f).toInt()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .graphicsLayer { rotationZ = rotation }
                .clip(RoundedCornerShape(safeCornerPercent))
                .background(color)
                .clickable { shapeIndex++ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "↻",
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier.graphicsLayer { rotationZ = -rotation }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "탭하여 변환",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

// ============================================
// 프로그레스 도형 변환
// ============================================
@Composable
fun ProgressShapeMorph(modifier: Modifier = Modifier) {
    var progress by remember { mutableFloatStateOf(0f) }

    val cornerRadius by animateFloatAsState(
        targetValue = 50f * (1f - progress),
        animationSpec = tween(100),
        label = "progressCorner"
    )

    val boxSize by animateDpAsState(
        targetValue = (60 + 60 * progress).dp,
        animationSpec = tween(100),
        label = "progressSize"
    )

    val color by animateColorAsState(
        targetValue = when {
            progress < 0.33f -> Color(0xFFF44336)
            progress < 0.66f -> Color(0xFFFF9800)
            else -> Color(0xFF4CAF50)
        },
        animationSpec = tween(200),
        label = "progressColor"
    )

    val safeCornerPercent = cornerRadius.coerceIn(0f, 50f).toInt()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(boxSize)
                .clip(RoundedCornerShape(safeCornerPercent))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 슬라이더
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 20.dp)
        ) {
            // 트랙 배경
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )

            // 채워진 트랙
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0.02f, 1f))
                    .height(8.dp)
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )

            // 터치영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, _ ->
                            change.consume()
                            val newProgress = (change.position.x / size.width).coerceIn(0f, 1f)
                            progress = newProgress
                        }
                    }
            )
        }

        Text(
            text = "드래그하여 진행률 조절",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun MorphingShapeDemo() {
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
            text = "Morphing Shape",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSectionWithBox(title = "원 ↔ 사각형 (Corner Radius)") {
            CircleToSquareMorph()
        }

        DemoSectionWithBox(title = "다중 도형 변환") {
            MultiShapeMorph()
        }

        DemoSectionWithBox(title = "FAB 확장 모핑") {
            ExpandableFabMorph()
        }

        DemoSectionWithBox(title = "별 모양 변환 (Path)") {
            StarMorph()
        }

        DemoSectionWithBox(title = "하트 ↔ 원 (Cubic Bezier)") {
            HeartCircleMorph()
        }

        DemoSectionWithBox(title = "로딩 도형 변환") {
            LoadingShapeMorph()
        }

        DemoSection(title = "프로그레스 도형 변환") {
            ProgressShapeMorph()
        }

        MorphingShapeGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MorphingShapeGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Morphing Shape 가이드")


            CodeSection(
                title = "방법 1: Corner Radius 변경",
                code = """
                    val corner by animateFloatAsState(
                        targetValue = if (isCircle) 50f else 0f
                    )
                    // 범위 제한 필수!
                    val safe = corner.coerceIn(0f, 50f).toInt()
                    Modifier.clip(RoundedCornerShape(safe))
                """.trimIndent()
            )

            CodeSection(
                title = "방법 2: Path 정점 보간",
                code = """
                    val progress by animateFloatAsState(...)
                    // 범위 제한!
                    val safe = progress.coerceIn(0f, 1f)
                    val path = createMorphPath(safe)
                    drawPath(path, color)
                """.trimIndent()
            )

            FeatureSection(
                type = FeatureTextType.CAUTION,
                features = """
                    • spring 바운스로 범위 초과 가능 → coerceIn() 필수
                    • RoundedCornerShape: 0~100% 범위
                    • Path points: 최소 3개 필요
                """.trimIndent()
            )

            FeatureSection(
                type = FeatureTextType.TIP,
                features = """
                    • 50% corner = 정원
                    • graphicsLayer로 회전 추가
                    • spring으로 탄성 효과
                    • Path는 같은 수의 정점 필요
                """.trimIndent()
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
private fun MorphingShapeDemoPreview() {
    MorphingShapeDemo()
}
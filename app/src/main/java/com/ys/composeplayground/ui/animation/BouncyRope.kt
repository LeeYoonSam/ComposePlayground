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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sqrt

/**
 * 🎯 Bonus #22: Bouncy Rope (출렁이는 줄)
 *
 * 📖 핵심 개념
 *
 * 두 점 사이에 물리 기반으로 출렁이는 줄을 구현합니다. 줄의 각 세그먼트가 중력과 탄성의 영향을 받아 자연스럽게 흔들립니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * Canvas | 줄 그리기
 * Path + quadraticTo | 부드러운 곡선
 * withFrameMillis | 물리 시뮬레이션 루프
 * pointerInput | 드래그로 끝점 이동
 * Verlet Integration | 물리 엔진
 *
 * 💡 동작 원리
 *
 * ```
 * [초기화] N개의 점을 직선으로 배치
 *        ↓ 매 프레임
 * [물리 시뮬레이션]
 *   1. 각 점에 중력 적용: velocity.y += gravity
 *   2. 위치 업데이트: position += velocity
 *   3. 제약 조건 적용: 점 사이 거리 유지
 *   4. 감쇠 적용: velocity *= damping
 *        ↓
 * [렌더링] quadraticTo 부드러운 곡선
 * ```
 *
 * 학습 목표:
 * 1. Verlet Integration 물리 시뮬레이션
 * 2. 제약 조건 기반 줄 시뮬레이션
 * 3. 드래그로 끝점 제어
 * 4. 부드러운 곡선 렌더링
 */

// ============================================
// 줄의 점 (노드) 클래스
// ============================================
class RopePoint(
    var x: Float,
    var y: Float,
    val isFixed: Boolean = false
) {
    var prevX: Float = x
    var prevY: Float = y

    fun update(gravity: Float, damping: Float) {
        if (isFixed) return

        val velocityX = (x - prevX) * damping
        val velocityY = (y - prevY) * damping

        prevX = x
        prevY = y

        x += velocityX
        y += velocityY + gravity
    }

    // 위치 설정 (속도 유지)
    fun setPosition(newX: Float, newY: Float) {
        prevX = x
        prevY = y
        x = newX
        y = newY
    }

    // 위치 설정 (속도 리셋)
    fun setPositionWithoutVelocity(newX: Float, newY: Float) {
        x = newX
        y = newY
        prevX = newX
        prevY = newY
    }
}

// ============================================
// 기본 Bouncy Rope (수정됨)
// ============================================
@Composable
fun BasicBouncyRope(modifier: Modifier = Modifier) {
    val pointCount = 15
    val segmentLength = 25f
    val gravity = 0.5f
    val damping = 0.98f
    val iterations = 5

    var canvasSize by remember { mutableStateOf(Offset.Zero) }

    // 줄의 점들 초기화
    val points = remember {
        List(pointCount) { i ->
            RopePoint(
                x = 100f + i * segmentLength,
                y = 150f,
                isFixed = i == 0
            )
        }
    }

    // 드래그 상태
    var isDragging by remember { mutableStateOf(false) }
    var dragTarget by remember { mutableStateOf(Offset.Zero) }

    // 물리 시뮬레이션
    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis {
                val lastPoint = points.last()

                // 드래그 중이면 끝점이 드래그 위치를 부드럽게 따라감
                if (isDragging) {
                    val dx = dragTarget.x - lastPoint.x
                    val dy = dragTarget.y - lastPoint.y
                    // 부드러운 추적 (lerp)
                    lastPoint.setPosition(
                        newX = lastPoint.x + dx * 0.3f,
                        newY = lastPoint.y + dy * 0.3f
                    )
                }

                // 1. 각 점 업데이트 (중력 + 속도)
                points.forEach { point ->
                    if (!isDragging || point != lastPoint) {
                        point.update(gravity, damping)
                    }
                }

                // 2. 제약 조건 적용
                repeat(iterations) {
                    for (i in 0 until points.size - 1) {
                        val p1 = points[i]
                        val p2 = points[i + 1]

                        val dx = p2.x - p1.x
                        val dy = p2.y - p1.y
                        val distance = sqrt(dx * dx + dy * dy)
                        if (distance == 0f) return@repeat

                        val difference = segmentLength - distance
                        val percent = difference / distance / 2

                        val offsetX = dx * percent
                        val offsetY = dy * percent

                        if (!p1.isFixed) {
                            p1.x -= offsetX
                            p1.y -= offsetY
                        }
                        // 드래그 중이 아닐 때만 끝점도 조정
                        if (!isDragging || i < points.size - 2) {
                            p2.x += offsetX
                            p2.y += offsetY
                        }
                    }
                }

                // 경계 처리
                points.forEach { point ->
                    if (!point.isFixed) {
                        point.x = point.x.coerceIn(10f, canvasSize.x - 10f)
                        point.y = point.y.coerceIn(10f, canvasSize.y - 10f)
                    }
                }
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        dragTarget = offset
                    },
                    onDragEnd = {
                        isDragging = false
                        // 드래그 종료 시 자연스럽게 속도 유지됨
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        dragTarget = change.position
                    }
                )
            }
    ) {
        canvasSize = Offset(size.width, size.height)

        // 줄 그리기
        val path = Path()
        if (points.isNotEmpty()) {
            path.moveTo(points[0].x, points[0].y)

            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val curr = points[i]

                val midX = (prev.x + curr.x) / 2
                val midY = (prev.y + curr.y) / 2

                path.quadraticTo(prev.x, prev.y, midX, midY)
            }

            val last = points.last()
            path.lineTo(last.x, last.y)
        }

        // 줄 그리기
        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFFF6B6B),
                    Color(0xFFFFE66D),
                    Color(0xFF4ECDC4)
                )
            ),
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )

        // 고정점 표시
        drawCircle(
            color = Color(0xFFFF6B6B),
            radius = 12f,
            center = Offset(points[0].x, points[0].y)
        )

        // 드래그 가능한 끝점 표시
        val lastPoint = points.last()
        drawCircle(
            color = if (isDragging) Color(0xFFFFE66D) else Color(0xFF4ECDC4),
            radius = if (isDragging) 18f else 14f,
            center = Offset(lastPoint.x, lastPoint.y)
        )

        // 드래그 중일 때 타겟 위치 표시
        if (isDragging) {
            drawCircle(
                color = Color.White.copy(alpha = 0.3f),
                radius = 25f,
                center = dragTarget
            )
        }

        // 안내 텍스트
        if (!isDragging) {
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "끝점을 드래그하세요",
                    size.width / 2,
                    size.height - 20,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.argb(150, 255, 255, 255)
                        textSize = 32f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

// ============================================
// 양쪽 고정 줄 (가운데가 출렁임)
// ============================================
@Composable
fun BothEndFixedRope(modifier: Modifier = Modifier) {
    val pointCount = 20
    val gravity = 0.4f
    val damping = 0.97f
    val iterations = 8

    var touchPoint by remember { mutableStateOf<Offset?>(null) }

    // 직접 초기값 설정 (나중에 캔버스 크기에 맞게 조정)
    val points = remember {
        mutableListOf<RopePoint>().apply {
            repeat(pointCount) { i ->
                add(
                    RopePoint(
                        x = 50f + i * 20f,
                        y = 80f,
                        isFixed = i == 0 || i == pointCount - 1
                    )
                )
            }
        }
    }

    var canvasWidth by remember { mutableFloatStateOf(0f) }
    var canvasHeight by remember { mutableFloatStateOf(0f) }

    // 캔버스 크기가 변경되면 점 위치 재조정
    LaunchedEffect(canvasWidth) {
        if (canvasWidth > 0) {
            val segmentLength = (canvasWidth - 100) / (pointCount - 1)
            points.forEachIndexed { i, point ->
                point.x = 50f + i * segmentLength
                point.prevX = point.x
            }
        }
    }

    // 물리 시뮬레이션
    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis {
                if (canvasWidth <= 0) return@withFrameMillis

                val segmentLength = (canvasWidth - 100) / (pointCount - 1)

                // 터치 영향 적용
                touchPoint?.let { touch ->
                    points.forEach { point ->
                        if (!point.isFixed) {
                            val dx = point.x - touch.x
                            val dy = point.y - touch.y
                            val distance = sqrt(dx * dx + dy * dy)
                            if (distance < 100f && distance > 0f) {
                                val force = (100f - distance) / 100f * 8f
                                point.y += dy / distance * force
                            }
                        }
                    }
                }

                points.forEach { it.update(gravity, damping) }

                repeat(iterations) {
                    for (i in 0 until points.size - 1) {
                        val p1 = points[i]
                        val p2 = points[i + 1]

                        val dx = p2.x - p1.x
                        val dy = p2.y - p1.y
                        val distance = sqrt(dx * dx + dy * dy)
                        if (distance == 0f) continue

                        val difference = segmentLength - distance
                        val percent = difference / distance / 2

                        val offsetX = dx * percent
                        val offsetY = dy * percent

                        if (!p1.isFixed) {
                            p1.x -= offsetX
                            p1.y -= offsetY
                        }
                        if (!p2.isFixed) {
                            p2.x += offsetX
                            p2.y += offsetY
                        }
                    }
                }

                points.forEach { point ->
                    if (!point.isFixed) {
                        point.y = point.y.coerceIn(20f, canvasHeight - 20f)
                    }
                }
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2D3436))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> touchPoint = offset },
                    onDragEnd = { touchPoint = null },
                    onDrag = { change, _ ->
                        change.consume()
                        touchPoint = change.position
                    }
                )
            }
    ) {
        canvasWidth = size.width
        canvasHeight = size.height

        val path = Path()
        path.moveTo(points[0].x, points[0].y)

        for (i in 1 until points.size) {
            val prev = points[i - 1]
            val curr = points[i]
            val midX = (prev.x + curr.x) / 2
            val midY = (prev.y + curr.y) / 2
            path.quadraticTo(prev.x, prev.y, midX, midY)
        }
        path.lineTo(points.last().x, points.last().y)

        drawPath(
            path = path,
            color = Color(0xFF00CEC9),
            style = Stroke(width = 5f, cap = StrokeCap.Round)
        )

        listOf(points.first(), points.last()).forEach { point ->
            drawCircle(
                color = Color(0xFFFF7675),
                radius = 10f,
                center = Offset(point.x, point.y)
            )
        }

        touchPoint?.let { touch ->
            drawCircle(
                color = Color.White.copy(alpha = 0.2f),
                radius = 100f,
                center = touch
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.5f),
                radius = 8f,
                center = touch
            )
        }

        if (touchPoint == null) {
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "줄을 터치해서 튕겨보세요",
                    size.width / 2,
                    size.height - 20,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.argb(150, 255, 255, 255)
                        textSize = 32f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

// ============================================
// 기타 줄 (수정됨)
// ============================================
@Composable
fun GuitarStrings(modifier: Modifier = Modifier) {
    val stringCount = 6
    val pointsPerString = 15
    val gravity = 0.2f
    val damping = 0.95f
    val iterations = 5

    var touchX by remember { mutableFloatStateOf(-1f) }
    var touchY by remember { mutableFloatStateOf(-1f) }
    var lastTouchY by remember { mutableFloatStateOf(-1f) }

    var canvasWidth by remember { mutableFloatStateOf(0f) }
    var canvasHeight by remember { mutableFloatStateOf(0f) }

    // 초기값으로 생성
    val allStrings = remember {
        mutableListOf<MutableList<RopePoint>>().apply {
            repeat(stringCount) { stringIndex ->
                val stringY = 50f + (stringIndex + 1) * 35f
                add(
                    MutableList(pointsPerString) { i ->
                        RopePoint(
                            x = 30f + i * 20f,
                            y = stringY,
                            isFixed = i == 0 || i == pointsPerString - 1
                        )
                    }
                )
            }
        }
    }

    // 캔버스 크기 변경 시 재조정
    LaunchedEffect(canvasWidth, canvasHeight) {
        if (canvasWidth > 0 && canvasHeight > 0) {
            val stringSpacing = (canvasHeight - 60) / (stringCount + 1)
            val segmentLength = (canvasWidth - 60) / (pointsPerString - 1)

            allStrings.forEachIndexed { stringIndex, points ->
                val baseY = 40f + (stringIndex + 1) * stringSpacing
                points.forEachIndexed { i, point ->
                    point.x = 30f + i * segmentLength
                    point.y = baseY
                    point.prevX = point.x
                    point.prevY = point.y
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis {
                if (canvasWidth <= 0 || canvasHeight <= 0) return@withFrameMillis

                val segmentLength = (canvasWidth - 60) / (pointsPerString - 1)
                val stringSpacing = (canvasHeight - 60) / (stringCount + 1)

                allStrings.forEachIndexed { stringIndex, points ->
                    val baseY = 40f + (stringIndex + 1) * stringSpacing

                    // 터치 영향
                    if (touchX > 0 && touchY > 0) {
                        if (kotlin.math.abs(touchY - baseY) < 25f) {
                            points.forEach { point ->
                                if (!point.isFixed) {
                                    val dx = kotlin.math.abs(point.x - touchX)
                                    if (dx < 40f) {
                                        val direction = if (lastTouchY > 0) {
                                            if (touchY > lastTouchY) 1f else -1f
                                        } else 0f
                                        val force = (40f - dx) / 40f * 25f * direction
                                        point.y += force
                                    }
                                }
                            }
                        }
                    }

                    points.forEach { it.update(gravity, damping) }

                    // 스프링 복원력
                    points.forEach { point ->
                        if (!point.isFixed) {
                            val restoreForce = (baseY - point.y) * 0.08f
                            point.y += restoreForce
                        }
                    }

                    repeat(iterations) {
                        for (i in 0 until points.size - 1) {
                            val p1 = points[i]
                            val p2 = points[i + 1]

                            val dx = p2.x - p1.x
                            val dy = p2.y - p1.y
                            val distance = sqrt(dx * dx + dy * dy)
                            if (distance == 0f) continue

                            val difference = segmentLength - distance
                            val percent = difference / distance / 2

                            if (!p1.isFixed) {
                                p1.x -= dx * percent
                                p1.y -= dy * percent
                            }
                            if (!p2.isFixed) {
                                p2.x += dx * percent
                                p2.y += dy * percent
                            }
                        }
                    }
                }

                lastTouchY = touchY
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF4A3728))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        touchX = offset.x
                        touchY = offset.y
                        lastTouchY = offset.y
                    },
                    onDragEnd = {
                        touchX = -1f
                        touchY = -1f
                        lastTouchY = -1f
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        touchX = change.position.x
                        touchY = change.position.y
                    }
                )
            }
    ) {
        canvasWidth = size.width
        canvasHeight = size.height

        drawRoundRect(
            color = Color(0xFF8B4513).copy(alpha = 0.3f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
        )

        val stringColors = listOf(
            Color(0xFFE5C07B),
            Color(0xFFD4A76A),
            Color(0xFFC49660),
            Color(0xFFB38556),
            Color(0xFFA3744C),
            Color(0xFF936342)
        )

        allStrings.forEachIndexed { index, points ->
            val path = Path()
            path.moveTo(points[0].x, points[0].y)

            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val curr = points[i]
                val midX = (prev.x + curr.x) / 2
                val midY = (prev.y + curr.y) / 2
                path.quadraticTo(prev.x, prev.y, midX, midY)
            }
            path.lineTo(points.last().x, points.last().y)

            val strokeWidth = 2f + index * 0.8f

            drawPath(
                path = path,
                color = stringColors[index],
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            listOf(points.first(), points.last()).forEach { point ->
                drawCircle(
                    color = Color(0xFFDDDDDD),
                    radius = 6f,
                    center = Offset(point.x, point.y)
                )
            }
        }

        drawLine(
            color = Color(0xFFDDDDDD),
            start = Offset(30f, 30f),
            end = Offset(30f, size.height - 30f),
            strokeWidth = 8f
        )
        drawLine(
            color = Color(0xFFDDDDDD),
            start = Offset(size.width - 30f, 30f),
            end = Offset(size.width - 30f, size.height - 30f),
            strokeWidth = 8f
        )

        if (touchX < 0) {
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "줄을 튕겨보세요",
                    size.width / 2,
                    size.height - 15,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.argb(120, 255, 255, 255)
                        textSize = 28f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

// ============================================
// 조절 가능한 줄
// ============================================
@Composable
fun AdjustableRope(modifier: Modifier = Modifier) {
    var pointCount by remember { mutableFloatStateOf(15f) }
    var gravity by remember { mutableFloatStateOf(0.4f) }
    var damping by remember { mutableFloatStateOf(0.97f) }
    var stiffness by remember { mutableFloatStateOf(0.05f) }

    val iterations = 5

    var canvasWidth by remember { mutableFloatStateOf(0f) }
    var canvasHeight by remember { mutableFloatStateOf(0f) }

    // 일반 mutableList 사용 (Compose 상태 아님)
    val points = remember { mutableListOf<RopePoint>() }

    // Canvas 강제 redraw용 (pointerInput에 영향 안줌)
    var invalidateCounter by remember { mutableLongStateOf(0L) }

    // 초기화
    if (points.isEmpty()) {
        repeat(15) { i ->
            points.add(
                RopePoint(
                    x = 50f + i * 20f,
                    y = 100f,
                    isFixed = i == 0 || i == 14
                )
            )
        }
    }

    // 점 개수 변경 시 재생성
    LaunchedEffect(pointCount.toInt()) {
        if (canvasWidth <= 0) return@LaunchedEffect

        val count = pointCount.toInt()
        val segmentLength = (canvasWidth - 100) / (count - 1).coerceAtLeast(1)

        points.clear()
        repeat(count) { i ->
            points.add(
                RopePoint(
                    x = 50f + i * segmentLength,
                    y = 100f,
                    isFixed = i == 0 || i == count - 1
                )
            )
        }
    }

    // 물리 시뮬레이션
    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { frameTime ->
                if (points.isEmpty() || canvasWidth <= 0) return@withFrameMillis

                val count = points.size
                val segmentLength = (canvasWidth - 100) / (count - 1).coerceAtLeast(1)
                val baseY = 100f

                val currentGravity = gravity
                val currentDamping = damping
                val currentStiffness = stiffness

                points.forEach { it.update(currentGravity, currentDamping) }

                points.forEach { point ->
                    if (!point.isFixed) {
                        val restoreForce = (baseY - point.y) * currentStiffness
                        point.y += restoreForce
                    }
                }

                repeat(iterations) {
                    for (i in 0 until points.size - 1) {
                        val p1 = points[i]
                        val p2 = points[i + 1]

                        val dx = p2.x - p1.x
                        val dy = p2.y - p1.y
                        val distance = sqrt(dx * dx + dy * dy)
                        if (distance == 0f) continue

                        val difference = segmentLength - distance
                        val percent = difference / distance / 2

                        if (!p1.isFixed) {
                            p1.x -= dx * percent
                            p1.y -= dy * percent
                        }
                        if (!p2.isFixed) {
                            p2.x += dx * percent
                            p2.y += dy * percent
                        }
                    }
                }

                points.forEach { point ->
                    if (!point.isFixed) {
                        point.y = point.y.coerceIn(20f, canvasHeight - 20f)
                    }
                }

                // Canvas redraw 트리거 (Canvas 재생성 아님)
                invalidateCounter = frameTime
            }
        }
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF2C3E50))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { },
                        onDragEnd = { },
                        onDrag = { change, _ ->
                            change.consume()
                            points.forEach { point ->
                                if (!point.isFixed) {
                                    val dx = point.x - change.position.x
                                    val dy = point.y - change.position.y
                                    val distance = sqrt(dx * dx + dy * dy)
                                    if (distance < 80f && distance > 0f) {
                                        val force = (80f - distance) / 80f * 15f
                                        point.y += dy / distance * force
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            // invalidateCounter를 읽어서 recomposition 트리거
            // Canvas는 Box 안에 있어서 pointerInput은 Box에서 처리
            Canvas(modifier = Modifier.fillMaxSize()) {
                // 이 값을 읽어야 recomposition 발생
                invalidateCounter.let { }

                if (canvasWidth == 0f) {
                    canvasWidth = size.width
                    canvasHeight = size.height
                }

                if (points.isEmpty()) return@Canvas

                val path = Path()
                path.moveTo(points[0].x, points[0].y)

                for (i in 1 until points.size) {
                    val prev = points[i - 1]
                    val curr = points[i]
                    val midX = (prev.x + curr.x) / 2
                    val midY = (prev.y + curr.y) / 2
                    path.quadraticTo(prev.x, prev.y, midX, midY)
                }
                path.lineTo(points.last().x, points.last().y)

                drawPath(
                    path = path,
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF74B9FF), Color(0xFFA29BFE))
                    ),
                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                )

                listOf(points.first(), points.last()).forEach { point ->
                    drawCircle(
                        color = Color(0xFFFF7675),
                        radius = 10f,
                        center = Offset(point.x, point.y)
                    )
                }

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "줄을 터치해서 튕겨보세요",
                        size.width / 2,
                        size.height - 15,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.argb(120, 255, 255, 255)
                            textSize = 28f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF34495E), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            SliderControl(
                label = "점 개수",
                value = pointCount,
                range = 5f..30f,
                onValueChange = { pointCount = it },
                valueDisplay = "${pointCount.toInt()}"
            )

            SliderControl(
                label = "중력",
                value = gravity,
                range = 0.1f..1.0f,
                onValueChange = { gravity = it },
                valueDisplay = "%.2f".format(gravity)
            )

            SliderControl(
                label = "감쇠",
                value = damping,
                range = 0.90f..0.99f,
                onValueChange = { damping = it },
                valueDisplay = "%.3f".format(damping)
            )

            SliderControl(
                label = "복원력",
                value = stiffness,
                range = 0.01f..0.15f,
                onValueChange = { stiffness = it },
                valueDisplay = "%.3f".format(stiffness)
            )
        }
    }
}

@Composable
private fun SliderControl(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    valueDisplay: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.weight(0.25f)
        )

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            modifier = Modifier.weight(0.55f),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF74B9FF),
                activeTrackColor = Color(0xFF74B9FF)
            )
        )

        Text(
            text = valueDisplay,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 11.sp,
            modifier = Modifier.weight(0.2f)
        )
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun BouncyRopeDemo() {
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
            text = "Bouncy Rope",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "기본 줄 (끝점 드래그)") {
            BasicBouncyRope()
        }

        DemoSection(title = "양쪽 고정 줄 (터치로 튕기기)") {
            BothEndFixedRope()
        }

        DemoSection(title = "기타 줄 (스와이프로 연주)") {
            GuitarStrings()
        }

        DemoSection(title = "조절 가능한 줄") {
            AdjustableRope()
        }

        BouncyRopeGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun BouncyRopeGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Bouncy Rope 가이드")

            CodeSection(
                title = "Verlet Integration",
                code = """
// 속도 = 현재위치 - 이전위치
val velocityX = (x - prevX) * damping
val velocityY = (y - prevY) * damping

prevX = x
prevY = y

x += velocityX
y += velocityY + gravity
                """.trimIndent()
            )

            CodeSection(
                title = "거리 제약 조건",
                code = """
val dx = p2.x - p1.x
val dy = p2.y - p1.y
val distance = sqrt(dx * dx + dy * dy)
val difference = segmentLength - distance
val percent = difference / distance / 2

// 양쪽 점을 균등하게 이동
p1.x -= dx * percent
p1.y -= dy * percent
p2.x += dx * percent
p2.y += dy * percent
                """.trimIndent()
            )

            FeatureSection(
                features = """
- Verlet Integration: 속도를 명시적으로 저장하지 않음
- 제약 조건 반복: 여러 번 적용하여 안정화
- quadraticBezierTo: 부드러운 곡선 렌더링
- isFixed: 고정점 설정
                """.trimIndent(),
                type = FeatureTextType.TIP
            )

            FeatureSection(
                features = """
- iterations가 낮으면 줄이 늘어남
- damping이 1에 가까우면 영원히 흔들림
- gravity가 너무 크면 불안정
                """.trimIndent(),
                type = FeatureTextType.CAUTION
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
private fun BouncyRopeDemoPreview() {
    BouncyRopeDemo()
}
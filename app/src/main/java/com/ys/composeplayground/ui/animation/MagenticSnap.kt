package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * 🎯 Bonus #23: Magnetic Snap (자석 붙기)
 *
 * 📖 핵심 개념
 *
 * 드래그하던 요소가 특정 영역 근처에서 자석처럼 끌려가 붙는 효과입니다. 스냅 포인트에 가까워지면 가속되어 붙고, 떨어지면 원래대로 돌아옵니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * Animatable | 부드러운 스냅 애니메이션
 * spring | 자석 붙는 탄성 효과
 * pointerInput | 드래그 제스처
 * detectDragGestures | 드래그 시작/종료 감지
 *
 * 💡 동작 원리
 *
 * ```
 * [드래그 중]
 *   ↓ 스냅 포인트와 거리 계산
 *   distance < threshold?
 *     → Yes: 자석 힘 적용 (가까울수록 강하게)
 *     → No: 드래그 위치 그대로
 *   ↓ 드래그 종료
 * [스냅 판정]
 *   distance < snapDistance?
 *     → Yes: spring 애니메이션으로 스냅 포인트에 붙기
 *     → No: spring 애니메이션으로 원위치 복귀
 * ```
 *
 * 학습 목표:
 * 1. 거리 기반 자석 힘 계산
 * 2. spring 애니메이션으로 스냅
 * 3. 드래그 중 실시간 자석 효과
 * 4. 멀티 스냅 포인트
 */
// ============================================
// 기본 Magnetic Snap
// ============================================
@Composable
fun BasicMagneticSnap(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E))
    ) {
        val boxWidthPx = with(density) { maxWidth.toPx() }
        val boxHeightPx = with(density) { maxHeight.toPx() }

        val dragRadius = 30.dp
        val dragRadiusPx = with(density) { dragRadius.toPx() }

        val snapRadius = 25.dp
        val snapRadiusPx = with(density) { snapRadius.toPx() }

        // 스냅 포인트들 (화면 전체에 균등 배치)
        val padding = 60f
        val snapPoints = remember(boxWidthPx, boxHeightPx) {
            listOf(
                Offset(padding, padding),                                    // 좌상
                Offset(boxWidthPx / 2, padding),                             // 중상
                Offset(boxWidthPx - padding, padding),                       // 우상
                Offset(padding, boxHeightPx / 2),                            // 좌중
                Offset(boxWidthPx / 2, boxHeightPx / 2),                     // 정중앙
                Offset(boxWidthPx - padding, boxHeightPx / 2),               // 우중
                Offset(padding, boxHeightPx - padding),                      // 좌하
                Offset(boxWidthPx / 2, boxHeightPx - padding),               // 중하
                Offset(boxWidthPx - padding, boxHeightPx - padding)          // 우하
            )
        }

        val snapThresholdPx = with(density) { 80.dp.toPx() }
        val snapDistancePx = with(density) { 50.dp.toPx() }

        val offsetX = remember { Animatable(boxWidthPx / 2) }
        val offsetY = remember { Animatable(boxHeightPx / 2) }

        var isDragging by remember { mutableStateOf(false) }
        var currentSnapIndex by remember { mutableStateOf(4) } // 중앙 시작

        // 스냅 포인트 표시
        snapPoints.forEachIndexed { index, point ->
            val isActive = currentSnapIndex == index
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (point.x - snapRadiusPx).roundToInt(),
                            (point.y - snapRadiusPx).roundToInt()
                        )
                    }
                    .size(snapRadius * 2)
                    .background(
                        color = if (isActive) Color(0xFF4ECDC4).copy(alpha = 0.5f)
                        else Color(0xFF4ECDC4).copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = if (isActive) Color(0xFF4ECDC4) else Color(0xFF4ECDC4).copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            )
        }

        // 드래그 가능한 원
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        (offsetX.value - dragRadiusPx).roundToInt(),
                        (offsetY.value - dragRadiusPx).roundToInt()
                    )
                }
                .size(dragRadius * 2)
                .shadow(
                    elevation = if (isDragging) 16.dp else 8.dp,
                    shape = CircleShape
                )
                .background(
                    color = if (isDragging) Color(0xFFFF6B6B) else Color(0xFFFFE66D),
                    shape = CircleShape
                )
                .pointerInput(snapPoints) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                            currentSnapIndex = -1
                        },
                        onDragEnd = {
                            isDragging = false
                            scope.launch {
                                val current = Offset(offsetX.value, offsetY.value)

                                val closestIndex = snapPoints.indices.minByOrNull { index ->
                                    (current - snapPoints[index]).getDistance()
                                } ?: -1

                                if (closestIndex >= 0) {
                                    val snapPoint = snapPoints[closestIndex]
                                    val distance = (current - snapPoint).getDistance()

                                    if (distance < snapDistancePx) {
                                        currentSnapIndex = closestIndex
                                        launch {
                                            offsetX.animateTo(
                                                snapPoint.x,
                                                spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessMedium
                                                )
                                            )
                                        }
                                        launch {
                                            offsetY.animateTo(
                                                snapPoint.y,
                                                spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessMedium
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            scope.launch {
                                var newX = offsetX.value + dragAmount.x
                                var newY = offsetY.value + dragAmount.y

                                val current = Offset(newX, newY)
                                snapPoints.forEach { snapPoint ->
                                    val distance = (current - snapPoint).getDistance()
                                    if (distance < snapThresholdPx && distance > 0) {
                                        val force = (1 - distance / snapThresholdPx).pow(2) * 0.3f
                                        newX += (snapPoint.x - newX) * force
                                        newY += (snapPoint.y - newY) * force
                                    }
                                }

                                newX = newX.coerceIn(dragRadiusPx, boxWidthPx - dragRadiusPx)
                                newY = newY.coerceIn(dragRadiusPx, boxHeightPx - dragRadiusPx)

                                offsetX.snapTo(newX)
                                offsetY.snapTo(newY)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "●",
                color = Color.White,
                fontSize = 20.sp
            )
        }

        Text(
            text = if (isDragging) "놓으면 가까운 점에 붙어요"
            else if (currentSnapIndex >= 0) "포인트 ${currentSnapIndex + 1}에 스냅됨"
            else "원을 드래그하세요",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}

// ============================================
// 그리드 스냅
// ============================================
@Composable
fun GridMagneticSnap(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2D3436))
    ) {
        val boxWidthPx = with(density) { maxWidth.toPx() }
        val boxHeightPx = with(density) { maxHeight.toPx() }

        val gridSize = 4
        val paddingPx = with(density) { 40.dp.toPx() }
        val cellWidthPx = (boxWidthPx - paddingPx * 2) / (gridSize - 1)
        val cellHeightPx = (boxHeightPx - paddingPx * 2) / (gridSize - 1)

        val snapPoints = remember(boxWidthPx, boxHeightPx) {
            buildList {
                for (row in 0 until gridSize) {
                    for (col in 0 until gridSize) {
                        add(Offset(paddingPx + col * cellWidthPx, paddingPx + row * cellHeightPx))
                    }
                }
            }
        }

        val snapThresholdPx = with(density) { 60.dp.toPx() }
        val snapDistancePx = with(density) { 40.dp.toPx() }

        val dragSize = 50.dp
        val dragSizePx = with(density) { dragSize.toPx() }
        val snapPointSize = 35.dp

        val offsetX = remember { Animatable(boxWidthPx / 2) }
        val offsetY = remember { Animatable(boxHeightPx / 2) }

        var isDragging by remember { mutableStateOf(false) }
        var snappedIndex by remember { mutableStateOf(-1) }

        // 그리드 라인
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize - 1) {
                val startX = paddingPx + col * cellWidthPx
                val y = paddingPx + row * cellHeightPx
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(startX.roundToInt(), y.roundToInt())
                        }
                        .size(with(density) { cellWidthPx.toDp() }, 1.dp)
                        .background(Color.White.copy(alpha = 0.1f))
                )
            }
        }
        for (col in 0 until gridSize) {
            for (row in 0 until gridSize - 1) {
                val x = paddingPx + col * cellWidthPx
                val startY = paddingPx + row * cellHeightPx
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(x.roundToInt(), startY.roundToInt())
                        }
                        .size(1.dp, with(density) { cellHeightPx.toDp() })
                        .background(Color.White.copy(alpha = 0.1f))
                )
            }
        }

        // 스냅 포인트
        snapPoints.forEachIndexed { index, point ->
            val isSnapped = snappedIndex == index
            val halfSize = with(density) { (snapPointSize / 2).toPx() }
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (point.x - halfSize).roundToInt(),
                            (point.y - halfSize).roundToInt()
                        )
                    }
                    .size(snapPointSize)
                    .background(
                        color = if (isSnapped) Color(0xFF00CEC9).copy(alpha = 0.6f)
                        else Color(0xFF636E72).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = if (isSnapped) 2.dp else 1.dp,
                        color = if (isSnapped) Color(0xFF00CEC9) else Color(0xFF636E72),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        }

        // 드래그 가능한 사각형
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        (offsetX.value - dragSizePx / 2).roundToInt(),
                        (offsetY.value - dragSizePx / 2).roundToInt()
                    )
                }
                .size(dragSize)
                .shadow(
                    elevation = if (isDragging) 12.dp else 6.dp,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = if (isDragging) Color(0xFFFF7675) else Color(0xFFFDCB6E),
                    shape = RoundedCornerShape(12.dp)
                )
                .pointerInput(snapPoints) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                            snappedIndex = -1
                        },
                        onDragEnd = {
                            isDragging = false
                            scope.launch {
                                val current = Offset(offsetX.value, offsetY.value)
                                val closestIndex = snapPoints.indices.minByOrNull { index ->
                                    (current - snapPoints[index]).getDistance()
                                } ?: -1

                                if (closestIndex >= 0) {
                                    val snapPoint = snapPoints[closestIndex]
                                    val distance = (current - snapPoint).getDistance()

                                    if (distance < snapDistancePx) {
                                        snappedIndex = closestIndex
                                        launch {
                                            offsetX.animateTo(
                                                snapPoint.x,
                                                spring(
                                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                                    stiffness = Spring.StiffnessHigh
                                                )
                                            )
                                        }
                                        launch {
                                            offsetY.animateTo(
                                                snapPoint.y,
                                                spring(
                                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                                    stiffness = Spring.StiffnessHigh
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            scope.launch {
                                var newX = offsetX.value + dragAmount.x
                                var newY = offsetY.value + dragAmount.y

                                val current = Offset(newX, newY)
                                snapPoints.forEach { snapPoint ->
                                    val distance = (current - snapPoint).getDistance()
                                    if (distance < snapThresholdPx && distance > 0) {
                                        val force = (1 - distance / snapThresholdPx).pow(3) * 0.5f
                                        newX += (snapPoint.x - newX) * force
                                        newY += (snapPoint.y - newY) * force
                                    }
                                }

                                newX = newX.coerceIn(dragSizePx / 2, boxWidthPx - dragSizePx / 2)
                                newY = newY.coerceIn(dragSizePx / 2, boxHeightPx - dragSizePx / 2)

                                offsetX.snapTo(newX)
                                offsetY.snapTo(newY)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (snappedIndex >= 0) "${snappedIndex + 1}" else "?",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Text(
            text = if (snappedIndex >= 0) "셀 ${snappedIndex + 1}에 스냅됨"
            else "4x4 그리드에 맞춰 스냅됩니다",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}

// ============================================
// 슬롯 스냅 (카드 슬롯)
// ============================================
@Composable
fun SlotMagneticSnap(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    data class CardData(
        val id: Int,
        val color: Color,
        val label: String
    )

    val cards = remember {
        listOf(
            CardData(0, Color(0xFFFF6B6B), "A"),
            CardData(1, Color(0xFFFFE66D), "B"),
            CardData(2, Color(0xFF4ECDC4), "C"),
            CardData(3, Color(0xFFA29BFE), "D")
        )
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E272E))
    ) {
        val boxWidthPx = with(density) { maxWidth.toPx() }
        val boxHeightPx = with(density) { maxHeight.toPx() }

        val cardWidth = 70.dp
        val cardHeight = 50.dp
        val cardWidthPx = with(density) { cardWidth.toPx() }
        val cardHeightPx = with(density) { cardHeight.toPx() }

        val slotCount = 4
        val slotSpacing = (boxWidthPx - cardWidthPx * slotCount) / (slotCount + 1)

        // 슬롯 위치 (하단)
        val slots = remember(boxWidthPx, boxHeightPx) {
            List(slotCount) { index ->
                Offset(
                    slotSpacing + index * (cardWidthPx + slotSpacing) + cardWidthPx / 2,
                    boxHeightPx - 80f
                )
            }
        }

        // 카드 시작 위치 (상단)
        val startPositions = remember(boxWidthPx) {
            List(cards.size) { index ->
                Offset(
                    slotSpacing + index * (cardWidthPx + slotSpacing) + cardWidthPx / 2,
                    60f
                )
            }
        }

        val cardOffsets = remember {
            cards.map { card ->
                Animatable(startPositions[card.id], Offset.VectorConverter)
            }
        }

        var slotAssignments by remember { mutableStateOf(List(slotCount) { -1 }) }
        var draggingCardIndex by remember { mutableStateOf(-1) }

        val snapThresholdPx = with(density) { 70.dp.toPx() }
        val snapDistancePx = with(density) { 50.dp.toPx() }

        // 카드 시작 영역 표시
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        with(density) { 16.dp.toPx() }.roundToInt(),
                        with(density) { 25.dp.toPx() }.roundToInt()
                    )
                }
                .size(maxWidth - 32.dp, 70.dp)
                .background(
                    Color(0xFF2C3E50).copy(alpha = 0.5f),
                    RoundedCornerShape(12.dp)
                )
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "카드를 아래 슬롯으로 드래그",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }

        // 슬롯들
        slots.forEachIndexed { index, slotPos ->
            val hasCard = slotAssignments[index] >= 0
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (slotPos.x - cardWidthPx / 2).roundToInt(),
                            (slotPos.y - cardHeightPx / 2).roundToInt()
                        )
                    }
                    .size(cardWidth, cardHeight)
                    .background(
                        color = if (hasCard) Color(0xFF00B894).copy(alpha = 0.3f)
                        else Color(0xFF636E72).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = if (hasCard) Color(0xFF00B894) else Color(0xFF636E72),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (!hasCard) {
                    Text(
                        text = "슬롯 ${index + 1}",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 10.sp
                    )
                }
            }
        }

        // 카드들
        cards.forEachIndexed { cardIndex, card ->
            val offset = cardOffsets[cardIndex]
            val isDragging = draggingCardIndex == cardIndex

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (offset.value.x - cardWidthPx / 2).roundToInt(),
                            (offset.value.y - cardHeightPx / 2).roundToInt()
                        )
                    }
                    .size(cardWidth, cardHeight)
                    .shadow(
                        elevation = if (isDragging) 16.dp else 6.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = card.color,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .pointerInput(cardIndex, slots, startPositions) {
                        detectDragGestures(
                            onDragStart = {
                                draggingCardIndex = cardIndex
                                slotAssignments = slotAssignments.map {
                                    if (it == cardIndex) -1 else it
                                }
                            },
                            onDragEnd = {
                                draggingCardIndex = -1
                                scope.launch {
                                    val current = offset.value
                                    var snapped = false

                                    slots.forEachIndexed { slotIndex, slotPos ->
                                        if (slotAssignments[slotIndex] < 0) {
                                            val distance = (current - slotPos).getDistance()
                                            if (distance < snapDistancePx && !snapped) {
                                                snapped = true
                                                slotAssignments = slotAssignments.toMutableList().apply {
                                                    this[slotIndex] = cardIndex
                                                }
                                                offset.animateTo(
                                                    slotPos,
                                                    spring(
                                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                                        stiffness = Spring.StiffnessMedium
                                                    )
                                                )
                                            }
                                        }
                                    }

                                    if (!snapped) {
                                        offset.animateTo(
                                            startPositions[cardIndex],
                                            spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessMedium
                                            )
                                        )
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                scope.launch {
                                    var newPos = offset.value + dragAmount

                                    slots.forEachIndexed { slotIndex, slotPos ->
                                        if (slotAssignments[slotIndex] < 0) {
                                            val distance = (newPos - slotPos).getDistance()
                                            if (distance < snapThresholdPx && distance > 0) {
                                                val force = (1 - distance / snapThresholdPx).pow(2) * 0.4f
                                                newPos = Offset(
                                                    newPos.x + (slotPos.x - newPos.x) * force,
                                                    newPos.y + (slotPos.y - newPos.y) * force
                                                )
                                            }
                                        }
                                    }

                                    newPos = Offset(
                                        newPos.x.coerceIn(cardWidthPx / 2, boxWidthPx - cardWidthPx / 2),
                                        newPos.y.coerceIn(cardHeightPx / 2, boxHeightPx - cardHeightPx / 2)
                                    )

                                    offset.snapTo(newPos)
                                }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = card.label,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        val filledCount = slotAssignments.count { it >= 0 }
        Text(
            text = "$filledCount / $slotCount 슬롯 채워짐",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}

// ============================================
// 자석 강도 조절
// ============================================
@Composable
fun AdjustableMagneticSnap(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    var magnetStrength by remember { mutableFloatStateOf(0.5f) }
    var snapThresholdDp by remember { mutableFloatStateOf(100f) }

    var isDragging by remember { mutableStateOf(false) }
    var isSnapped by remember { mutableStateOf(false) }
    var currentDistance by remember { mutableFloatStateOf(0f) }

    Column(modifier = modifier) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF0F0F23))
        ) {
            val boxWidthPx = with(density) { maxWidth.toPx() }
            val boxHeightPx = with(density) { maxHeight.toPx() }

            val snapPoint = Offset(boxWidthPx / 2, boxHeightPx / 2)
            val snapThresholdPx = with(density) { snapThresholdDp.dp.toPx() }

            val dragRadius = 25.dp
            val dragRadiusPx = with(density) { dragRadius.toPx() }

            val magnetRadius = 30.dp

            val offsetX = remember { Animatable(boxWidthPx * 0.25f) }
            val offsetY = remember { Animatable(boxHeightPx * 0.3f) }

            LaunchedEffect(Unit) {
                while (true) {
                    withFrameMillis {
                        currentDistance = sqrt(
                            (offsetX.value - snapPoint.x).pow(2) +
                                    (offsetY.value - snapPoint.y).pow(2)
                        )
                    }
                }
            }

            // 자석 영역 표시
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (snapPoint.x - snapThresholdPx).roundToInt(),
                            (snapPoint.y - snapThresholdPx).roundToInt()
                        )
                    }
                    .size(with(density) { (snapThresholdPx * 2).toDp() })
                    .background(
                        color = Color(0xFF6C5CE7).copy(
                            alpha = (0.1f + magnetStrength * 0.2f).coerceAtMost(0.3f)
                        ),
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFF6C5CE7).copy(alpha = 0.3f),
                        shape = CircleShape
                    )
            )

            // 스냅 포인트
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (snapPoint.x - with(density) { magnetRadius.toPx() }).roundToInt(),
                            (snapPoint.y - with(density) { magnetRadius.toPx() }).roundToInt()
                        )
                    }
                    .size(magnetRadius * 2)
                    .background(
                        color = if (isSnapped) Color(0xFF6C5CE7) else Color(0xFF6C5CE7).copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .border(
                        width = 3.dp,
                        color = Color(0xFFA29BFE),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🧲",
                    fontSize = 24.sp
                )
            }

            // 드래그 가능한 원
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (offsetX.value - dragRadiusPx).roundToInt(),
                            (offsetY.value - dragRadiusPx).roundToInt()
                        )
                    }
                    .size(dragRadius * 2)
                    .shadow(
                        elevation = if (isDragging) 16.dp else 8.dp,
                        shape = CircleShape
                    )
                    .background(
                        color = if (isDragging) Color(0xFFFF6B6B)
                        else if (isSnapped) Color(0xFF00B894)
                        else Color(0xFFFFE66D),
                        shape = CircleShape
                    )
                    .pointerInput(magnetStrength, snapThresholdPx) {
                        detectDragGestures(
                            onDragStart = {
                                isDragging = true
                                isSnapped = false
                            },
                            onDragEnd = {
                                isDragging = false
                                scope.launch {
                                    val distance = sqrt(
                                        (offsetX.value - snapPoint.x).pow(2) +
                                                (offsetY.value - snapPoint.y).pow(2)
                                    )

                                    if (distance < snapThresholdPx * 0.5f) {
                                        isSnapped = true
                                        launch {
                                            offsetX.animateTo(
                                                snapPoint.x,
                                                spring(
                                                    dampingRatio = 0.5f - magnetStrength * 0.3f,
                                                    stiffness = 200f + magnetStrength * 400f
                                                )
                                            )
                                        }
                                        launch {
                                            offsetY.animateTo(
                                                snapPoint.y,
                                                spring(
                                                    dampingRatio = 0.5f - magnetStrength * 0.3f,
                                                    stiffness = 200f + magnetStrength * 400f
                                                )
                                            )
                                        }
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                scope.launch {
                                    var newX = offsetX.value + dragAmount.x
                                    var newY = offsetY.value + dragAmount.y

                                    val distance = sqrt(
                                        (newX - snapPoint.x).pow(2) +
                                                (newY - snapPoint.y).pow(2)
                                    )

                                    if (distance < snapThresholdPx && distance > 0) {
                                        val force = (1 - distance / snapThresholdPx).pow(2) * magnetStrength
                                        newX += (snapPoint.x - newX) * force
                                        newY += (snapPoint.y - newY) * force
                                    }

                                    newX = newX.coerceIn(dragRadiusPx, boxWidthPx - dragRadiusPx)
                                    newY = newY.coerceIn(dragRadiusPx, boxHeightPx - dragRadiusPx)

                                    offsetX.snapTo(newX)
                                    offsetY.snapTo(newY)
                                }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚡",
                    fontSize = 18.sp
                )
            }

            Text(
                text = "거리: ${currentDistance.roundToInt()}px",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            )

            Text(
                text = if (isSnapped) "스냅됨!" else "자석 영역으로 드래그",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E3F), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "자석 강도",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.25f)
                )
                Slider(
                    value = magnetStrength,
                    onValueChange = { magnetStrength = it },
                    valueRange = 0.1f..1f,
                    modifier = Modifier.weight(0.55f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF6C5CE7),
                        activeTrackColor = Color(0xFF6C5CE7)
                    )
                )
                Text(
                    text = "%.0f%%".format(magnetStrength * 100),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    modifier = Modifier.weight(0.2f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "영향 범위",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.25f)
                )
                Slider(
                    value = snapThresholdDp,
                    onValueChange = { snapThresholdDp = it },
                    valueRange = 50f..150f,
                    modifier = Modifier.weight(0.55f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF6C5CE7),
                        activeTrackColor = Color(0xFF6C5CE7)
                    )
                )
                Text(
                    text = "${snapThresholdDp.roundToInt()}dp",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    modifier = Modifier.weight(0.2f)
                )
            }
        }
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun MagneticSnapDemo() {
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
            text = "Magnetic Snap",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "기본 자석 스냅") {
            BasicMagneticSnap()
        }

        DemoSection(title = "그리드 스냅") {
            GridMagneticSnap()
        }

        DemoSection(title = "카드 슬롯 스냅") {
            SlotMagneticSnap()
        }

        DemoSection(title = "자석 강도 조절") {
            AdjustableMagneticSnap()
        }

        MagneticSnapGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MagneticSnapGuide() {
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
                text = "📚 Magnetic Snap 가이드",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = """
                    핵심 원리:
                    
                    • 거리 기반 자석 힘
                      distance = (current - snapPoint).getDistance()
                      force = (1 - distance / threshold)² * strength
                    
                    • 드래그 중 자석 효과
                      newPos += (snapPoint - newPos) * force
                    
                    • 스냅 애니메이션
                      spring(dampingRatio, stiffness)로 붙는 느낌
                    
                    💡 팁:
                    • pow(2~3): 가까울수록 급격히 강해짐
                    • strength 0.3~0.5: 자연스러운 끌림
                    • StiffnessHigh: 빠르게 붙는 느낌
                """.trimIndent(),
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
private fun MagneticSnapDemoPreview() {
    MagneticSnapDemo()
}
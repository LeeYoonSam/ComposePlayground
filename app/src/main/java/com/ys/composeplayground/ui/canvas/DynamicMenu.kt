package com.ys.composeplayground.ui.canvas

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun MovingCircleMenuCanvas() {
    var showSubMenu by remember { mutableStateOf(false) }
    val mainCircleRadius = 50.dp
    val subCircleRadius = 20.dp
    val distanceFromCenter = 100.dp
    val subCircles = remember {
        List(6) {
            mutableStateOf(Offset.Zero)
        }
    }
    val density = LocalDensity.current

    val animationProgress by animateFloatAsState(
        targetValue = if (showSubMenu) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val circleColors = remember {
        List(6) {
            Brush.linearGradient(
                colors = listOf(
                    Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f),
                    Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
                )
            )
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        val center = remember {
            mutableStateOf(Offset.Zero)
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            center.value = Offset(size.width / 2, size.height / 2)

            // 버튼 구성 - 제일 아래 있는 Main Menu 의 배경이 될 파란색 원
            drawCircle(color = Color.Blue, radius = mainCircleRadius.toPx(), center = center.value)

            if (showSubMenu) {
                subCircles.forEachIndexed { index, offsetState ->
                    val current = offsetState.value
                    val next = subCircles[(index + 1) % subCircles.size].value

                    // 라인 그리기
                    drawLine(
                        color = Color.Gray,
                        start = current,
                        end = next,
                        strokeWidth = 5f
                    )

                    val movingCirclePos = Offset(
                        lerpAction(current.x, next.x, animationProgress),
                        lerpAction(current.y, next.y, animationProgress),
                    )

                    // 움직이는 작은 동그라미 그리기
                    drawCircle(
                        color = Color.Black,
                        radius = 10f,
                        center = movingCirclePos,
                    )

                    // 원래 자리에 브러시 원 그리기
                    drawCircle(
                        brush = circleColors[index],
                        radius = subCircleRadius.toPx(),
                        center = current,
                    )
                }
            }
        }

        if (showSubMenu) {
            subCircles.forEachIndexed { index, offsetState ->
                val boxSizeDp = subCircleRadius * 2

                Box(modifier = Modifier
                    .offset {
                        IntOffset(
                            x = (offsetState.value.x - with(density) { boxSizeDp.toPx() } * 5).roundToInt(),
                            y = (offsetState.value.y - with(density) { boxSizeDp.toPx() } * 10).roundToInt(),
                        )
                    }
                    .size(boxSizeDp)
                    .background(Color.Gray)
                    .pointerInput(index) {
                        detectDragGestures { _, dragAmount ->
                            val newX = (offsetState.value.x + dragAmount.x).coerceIn(
                                minimumValue = 0f,
                                maximumValue = center.value.x * 2 - subCircleRadius.toPx()
                            )
                            val newY = (offsetState.value.y + dragAmount.y).coerceIn(
                                minimumValue = 0f,
                                maximumValue = center.value.y * 2 - subCircleRadius.toPx()
                            )
                            offsetState.value = Offset(newX, newY)
                        }
                    }
                )
            }
        }

        // 버튼 구성 - 파란색 원 위에 표시될 텍스트
        Text(text = "Main Menu", color = Color.White, modifier = Modifier.align(Alignment.Center))

        // 버튼 구성 - 최상단 버튼 역할을 해줄 투명 Box
        Box(
            modifier = Modifier
                .size(mainCircleRadius * 2)
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    // 버튼 탭
                    detectTapGestures {
                        // 서브 메뉴 가시성 값을 변경 - 리컴포지션(화면을 다시 구성)
                        showSubMenu = !showSubMenu

                        // 서브 메뉴 초기 위치 설정
                        if (showSubMenu) {
                            (1..6).forEach {
                                val angle = 60 * it
                                val x =
                                    center.value.x + cos(Math.toRadians(angle.toDouble())) * distanceFromCenter.toPx()
                                val y =
                                    center.value.y + sin(Math.toRadians(angle.toDouble())) * distanceFromCenter.toPx()
                                subCircles[it - 1].value = Offset(x.toFloat(), y.toFloat())
                            }
                        }
                    }
                }
        )
    }
}

fun lerpAction(start: Float, stop: Float, fraction: Float): Float =
    (1 - fraction) * start + fraction * stop


@Preview
@Composable
fun PreviewMainMenuCanvas() {
    ComposePlaygroundTheme {
        MovingCircleMenuCanvas()
    }
}
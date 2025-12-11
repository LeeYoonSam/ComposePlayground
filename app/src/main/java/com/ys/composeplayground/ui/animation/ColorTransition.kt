package com.ys.composeplayground.ui.animation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 🟢 Beginner #2: 색상 전환 애니메이션
 *
 * 학습 목표:
 * 1. animateColorAsState 사용법
 * 2. tween vs spring animationSpec
 * 3. Easing 함수 이해
 * 4. 그라데이션 애니메이션
 */
// ============================================
// 기본 버전: 배경색 전환
// ============================================
@Composable
fun ColorTransitionBasic(
    modifier: Modifier = Modifier
) {
    var isSelected by remember { mutableStateOf(false) }

    // 1️⃣ 배경색 애니메이션
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF6200EE) else Color(0xFFE0E0E0),
        animationSpec = tween(durationMillis = 300),
        label = "bgColor"
    )

    // 2️⃣ 텍스트 색상도 함께 애니메이션
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Gray,
        animationSpec = tween(durationMillis = 300),
        label = "textColor"
    )

    Box(
        modifier = modifier
            .size(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { isSelected = !isSelected },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isSelected) "✓" else "Tap",
            color = textColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ============================================
// 다중 색상 순환
// ============================================
@Composable
fun MultiColorTransition(
    modifier: Modifier = Modifier
) {
    var colorIndex by remember { mutableIntStateOf(0) }
    val colors = listOf(
        Color(0xFFE91E63),  // Pink
        Color(0xFF9C27B0),  // Purple
        Color(0xFF2196F3),  // Blue
        Color(0xFF4CAF50),  // Green
        Color(0xFFFF9800)   // Orange
    )
    val colorNames = listOf("Pink", "Purple", "Blue", "Green", "Orange")

    val animatedColor by animateColorAsState(
        targetValue = colors[colorIndex],
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "multiColor"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(animatedColor)
                .clickable {
                    colorIndex = (colorIndex + 1) % colors.size
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${colorIndex + 1}",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = colorNames[colorIndex],
                fontSize = 14.sp,
                color = animatedColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ============================================
// Spring 애니메이션 색상 전환
// ============================================
@Composable
fun SpringColorTransition(
    modifier: Modifier = Modifier
) {
    var isActive by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (isActive) Color(0xFF00C853) else Color(0xFFFF5252),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "springColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isActive) Color(0xFF00E676) else Color(0xFFFF8A80),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "borderColor"
    )

    Box(
        modifier = modifier
            .size(120.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(4.dp, borderColor, RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable { isActive = !isActive },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isActive) "ON" else "OFF",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// ============================================
// 그라데이션 색상 전환
// ============================================
@Composable
fun GradientColorTransition(
    modifier: Modifier = Modifier
) {
    var isActive by remember { mutableStateOf(false) }

    // 그라데이션의 두 색상을 각각 애니메이션
    val color1 by animateColorAsState(
        targetValue = if (isActive) Color(0xFF667eea) else Color(0xFFee9ca7),
        animationSpec = tween(600, easing = EaseInOutCubic),
        label = "gradient1"
    )

    val color2 by animateColorAsState(
        targetValue = if (isActive) Color(0xFF764ba2) else Color(0xFFffdde1),
        animationSpec = tween(600, easing = EaseInOutCubic),
        label = "gradient1"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(listOf(color1, color2))
            )
            .clickable { isActive = !isActive },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isActive) "Active Gradient" else "Tap to Change",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ============================================
// 상태 표시 카드
// ============================================
enum class Status { SUCCESS, WARNING, ERROR }

@Composable
fun StatusCard(
    modifier: Modifier = Modifier
) {
    var status by remember { mutableStateOf(Status.SUCCESS) }

    val statusColor by animateColorAsState(
        targetValue = when (status) {
            Status.SUCCESS -> Color(0xFF4CAF50)
            Status.WARNING -> Color(0xFFFF9800)
            Status.ERROR -> Color(0xFFF44336)
        },
        animationSpec = tween(400),
        label = "statusColor"
    )

    val statusBgColor by animateColorAsState(
        targetValue = when (status) {
            Status.SUCCESS -> Color(0xFFE8F5E9)
            Status.WARNING -> Color(0xFFFFF3E0)
            Status.ERROR -> Color(0xFFFFEBEE)
        },
        animationSpec = tween(400),
        label = "statusBgColor"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 상태 카드
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(statusBgColor)
                .border(2.dp, statusColor, RoundedCornerShape(12.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (status) {
                    Status.SUCCESS -> "✓ Success"
                    Status.WARNING -> "⚠ Warning"
                    Status.ERROR -> "✕ Error"
                },
                color = statusColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 상태 변경 버튼들
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Status.entries.forEach { s ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when (s) {
                                Status.SUCCESS -> Color(0xFF4CAF50)
                                Status.WARNING -> Color(0xFFFF9800)
                                Status.ERROR -> Color(0xFFF44336)
                            }
                        )
                        .then(
                            if (s == status) {
                                Modifier.border(3.dp, Color.Black, CircleShape)
                            } else Modifier
                        )
                        .clickable { status = s }
                )
            }
        }
    }
}

// ============================================
// 토글 스위치 with 색상 애니메이션
// ============================================
@Composable
fun AnimatedToggle(
    modifier: Modifier = Modifier
) {
    var isOn by remember { mutableStateOf(false) }

    val trackColor by animateColorAsState(
        targetValue = if (isOn) Color(0xFF6200EE) else Color(0xFFB0B0B0),
        animationSpec = tween(300),
        label = "trackColor"
    )

    val labelColor by animateColorAsState(
        targetValue = if (isOn) Color(0xFF6200EE) else Color.Gray,
        animationSpec = tween(300),
        label = "labelColor"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Enable Feature",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = labelColor
        )

        Switch(
            checked = isOn,
            onCheckedChange = { isOn = it },
            colors = SwitchDefaults.colors(
                checkedTrackColor = trackColor,
                uncheckedTrackColor = trackColor
            )
        )
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun ColorTransitionDemo() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text(
            text = "Color Transition Animation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        // 기본 버전
        DemoSection(title = "기본 - 배경색 전환") {
            ColorTransitionBasic()
        }

        // 다중 색상
        DemoSection(title = "다중 색상 순환 (tween + Easing)") {
            MultiColorTransition()
        }
        // Spring 색상
        DemoSection(title = "Spring 색상 전환") {
            SpringColorTransition()
        }

        // 그라데이션
        DemoSection(title = "그라데이션 전환") {
            GradientColorTransition(
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 상태 카드
        DemoSection(title = "상태 표시 카드") {
            StatusCard(
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 토글
        DemoSection(title = "토글 스위치") {
            AnimatedToggle(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(16.dp)
            )
        }

        // AnimationSpec 가이드
        AnimationSpecGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DemoSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        content()
    }
}

@Composable
fun AnimationSpecGuide() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            "📚 AnimationSpec 가이드",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            """
            tween (시간 기반):
            • durationMillis: 애니메이션 시간
            • easing: 속도 곡선
              - LinearEasing: 일정 속도
              - FastOutSlowInEasing: 빠르게 시작, 천천히 끝
              - EaseInOutCubic: 부드러운 시작과 끝
            
            spring (물리 기반):
            • 색상도 탄성있게 전환 가능!
            • dampingRatio로 바운스 조절
            
            snap:
            • 즉시 변경 (애니메이션 없음)
            """.trimIndent(),
            fontSize = 12.sp,
            color = Color.Gray,
            lineHeight = 18.sp
        )
    }
}
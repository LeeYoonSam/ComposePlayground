package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 🎯 Bonus #24: Breathing Button (숨쉬는 버튼)
 *
 * 📖 핵심 개념
 *
 * 버튼이 숨쉬듯 부드럽게 팽창/수축하는 효과입니다. 사용자의 주의를 끌거나, 로딩/대기 상태를 표현할 때 유용합니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * rememberInfiniteTransition | 무한 반복 애니메이션
 * animateFloat | scale, alpha 등 연속 값
 * graphicsLayer | 성능 좋은 변환
 * EaseInOutSine | 자연스러운 숨쉬기 곡선
 *
 * 💡 동작 원리
 *
 * ```
 * [수축] scale: 1.0, alpha: 0.8
 *    ↓ EaseInOutSine (부드러운 전환)
 * [팽창] scale: 1.1, alpha: 1.0
 *    ↓ EaseInOutSine
 * [수축] scale: 1.0, alpha: 0.8
 *    ... 반복
 * ```
 *
 * 학습 목표:
 * 1. rememberInfiniteTransition으로 무한 애니메이션
 * 2. EaseInOutSine으로 자연스러운 숨쉬기
 * 3. scale + alpha + glow 조합
 * 4. 클릭 시 애니메이션 중단/재개
 */

// ============================================
// 기본 숨쉬는 버튼
// ============================================
@Composable
fun BasicBreathingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
                .shadow(
                    elevation = (8 * scale).dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color(0xFF6C5CE7),
                    spotColor = Color(0xFF6C5CE7)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF6C5CE7), Color(0xFFA29BFE))
                    )
                )
                .clickable { onClick() }
                .padding(horizontal = 32.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "숨쉬는 버튼",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

// ============================================
// 하트비트 버튼
// ============================================
@Composable
fun HeartbeatButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "heartbeat")

    // 심장박동 패턴: 빠르게 두 번 뛰고 쉬기
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 150,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .size(80.dp)
            .shadow(
                elevation = (12 * (scale - 0.9f) * 10).dp,
                shape = CircleShape,
                ambientColor = Color(0xFFFF6B6B),
                spotColor = Color(0xFFFF6B6B)
            )
            .clip(CircleShape)
            .background(Color(0xFF6B6B))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Heart",
            tint = Color.White,
            modifier = Modifier.size(40.dp)
        )
    }
}

// ============================================
// 글로우 펄스 버튼
// ============================================
@Composable
fun GlowPulseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val glowSize by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowSize"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F0F23)),
        contentAlignment = Alignment.Center
    ) {
        // 글로우 레이어 (뒤)
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = glowSize
                    scaleY = glowSize
                    alpha = glowAlpha
                }
                .size(90.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF00CEC9),
                            Color(0xFF00CEC9).copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // 버튼 (앞)
        Box(
            modifier = Modifier
                .size(70.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF00CEC9), Color(0xFF00CEC9))
                    )
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

// ============================================
// 링 펄스 버튼
// ============================================
@Composable
fun RingPulseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ring")

    val ring1Scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring1Scale"
    )

    val ring1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring1Alpha"
    )

    val ring2Scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                delayMillis = 500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring2Scale"
    )

    val ring2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                delayMillis = 500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring2Alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E272E)),
        contentAlignment = Alignment.Center
    ) {
        // 링 1
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = ring1Scale
                    scaleY = ring1Scale
                    alpha = ring1Alpha
                }
                .size(70.dp)
                .border(
                    width = 3.dp,
                    color = Color(0xFFFDCB6E),
                    shape = CircleShape
                )
        )

        // 링 2
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = ring2Scale
                    scaleY = ring2Scale
                    alpha = ring2Alpha
                }
                .size(70.dp)
                .border(
                    width = 3.dp,
                    color = Color(0xFFFDCB6E),
                    shape = CircleShape
                )
        )

        // 중앙 버튼
        Box(
            modifier = Modifier
                .size(70.dp)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFFFDCB6E))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notification",
                tint = Color(0xFF2D3436),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

// ============================================
// 토글 숨쉬기 버튼
// ============================================
@Composable
fun ToggleBreathingButton(
    modifier: Modifier = Modifier
) {
    var isBreathing by remember {  mutableStateOf(true) }

    val infiniteTransition = rememberInfiniteTransition(label = "toggle")

    val animatedScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val scale = if (isBreathing) animatedScale else 1f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2C3E50)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .size(80.dp)
                    .shadow(
                        elevation = if (isBreathing) (10 * scale).dp else 4.dp,
                        shape = CircleShape,
                        ambientColor = Color(0xFFA29BFE),
                        spotColor = Color(0xFFA29BFE)
                    )
                    .clip(CircleShape)
                    .background(
                        if (isBreathing) Color(0xFF6C5CE7)
                        else Color(0xFF636E72)
                    )
                    .clickable { isBreathing = !isBreathing },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Text(
                text = if (isBreathing) "탭하여 멈추기" else "탭하여 시작",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}

// ============================================
// 누르면 멈추는 버튼
// ============================================
@Composable
fun PressToStopButton(
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "pressStop")

    val animatedScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    // 누르면 애니메이션 멈춤
    val scale = if (isPressed) 0.95f else animatedScale
    val alpha = if (isPressed) 1f else animatedAlpha

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F0F23)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = if (isPressed) {
                            listOf(Color(0xFF636E72), Color(0xFF2D3436))
                        } else {
                            listOf(Color(0xFFFF6B6B), Color(0xFFFF8E8E))
                        }
                    )
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { }
                .padding(horizontal = 40.dp, vertical = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isPressed) "누르는 중..." else "꾹 눌러보세요",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

// ============================================
// 조절 가능한 숨쉬기 버튼
// ============================================
@Composable
fun AdjustableBreathingButton(modifier: Modifier = Modifier) {
    var speed by remember { mutableFloatStateOf(1f) }
    var intensity by remember { mutableFloatStateOf(0.1f) }

    val durationMillis = (2000 / speed).toInt()

    val infiniteTransition = rememberInfiniteTransition(label = "adjustable")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f + intensity,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.3f + intensity * 5,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1A1A2E)),
            contentAlignment = Alignment.Center
        ) {
            // 글로우
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale * 1.2f
                        scaleY = scale * 1.2f
                        alpha = glowAlpha
                    }
                    .size(100.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF74B9FF),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // 버튼
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .size(80.dp)
                    .shadow(
                        elevation = (6 + intensity * 40).dp,
                        shape = CircleShape,
                        ambientColor = Color(0xFF74B9FF),
                        spotColor = Color(0xFF74B9FF)
                    )
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF74B9FF), Color(0xFF0984E3))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "●",
                    color = Color.White,
                    fontSize = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2D3436), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "속도",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.2f)
                )
                Slider(
                    value = speed,
                    onValueChange = { speed = it },
                    valueRange = 0.3f..3f,
                    modifier = Modifier.weight(0.6f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF74B9FF),
                        activeTrackColor = Color(0xFF74B9FF)
                    )
                )
                Text(
                    text = "%.1fx".format(speed),
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
                    text = "강도",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(0.2f)
                )
                Slider(
                    value = intensity,
                    onValueChange = { intensity = it },
                    valueRange = 0.05f..0.25f,
                    modifier = Modifier.weight(0.6f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF74B9FF),
                        activeTrackColor = Color(0xFF74B9FF)
                    )
                )
                Text(
                    text = "${(intensity * 100).toInt()}%",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    modifier = Modifier.weight(0.2f)
                )
            }
        }
    }
}

// ============================================
// 아이콘 버튼 그룹
// ============================================
@Composable
fun BreathingIconButtons(modifier: Modifier = Modifier) {
    data class ButtonData(
        val icon: ImageVector,
        val color: Color,
        val label: String,
        val delay: Int
    )

    val buttons = listOf(
        ButtonData(Icons.Default.Favorite, Color(0xFFFF6B6B), "좋아요", 0),
        ButtonData(Icons.Default.Add, Color(0xFF00CEC9), "추가", 200),
        ButtonData(Icons.Default.PlayArrow, Color(0xFFFDCB6E), "재생", 400),
        ButtonData(Icons.Default.Notifications, Color(0xFFA29BFE), "알림", 600)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E272E)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            buttons.forEach { button ->
                BreathingIconButton(
                    icon = button.icon,
                    color = button.color,
                    label = button.label,
                    delayMillis = button.delay
                )
            }
        }
    }
}

@Composable
private fun BreathingIconButton(
    icon: ImageVector,
    color: Color,
    label: String,
    delayMillis: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "icon_$label")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                delayMillis = delayMillis,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_$label"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .size(56.dp)
                .shadow(
                    elevation = (4 + (scale - 1) * 40).dp,
                    shape = CircleShape,
                    ambientColor = color,
                    spotColor = color
                )
                .clip(CircleShape)
                .background(color)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 10.sp
        )
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun BreathingButtonDemo() {
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
            text = "Breathing Button",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "기본 숨쉬기") {
            BasicBreathingButton()
        }

        DemoSection(title = "하트비트") {
            HeartbeatButton()
        }

        DemoSection(title = "글로우 펄스") {
            GlowPulseButton()
        }

        DemoSection(title = "링 펄스") {
            RingPulseButton()
        }

        DemoSection(title = "토글 숨쉬기") {
            ToggleBreathingButton()
        }

        DemoSection(title = "누르면 멈추기") {
            PressToStopButton()
        }

        DemoSection(title = "속도/강도 조절") {
            AdjustableBreathingButton()
        }

        DemoSection(title = "아이콘 버튼 그룹") {
            BreathingIconButtons()
        }

        BreathingButtonGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun BreathingButtonGuide() {
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
                text = "📚 Breathing Button 가이드",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = """
                    핵심 원리:
                    
                    • rememberInfiniteTransition
                      무한 반복 애니메이션 컨테이너
                    
                    • animateFloat + infiniteRepeatable
                      scale, alpha 등 연속 값 애니메이션
                    
                    • EaseInOutSine
                      자연스러운 숨쉬기 곡선
                    
                    • RepeatMode.Reverse
                      왕복 애니메이션
                    
                    💡 팁:
                    • delayMillis로 시차 효과
                    • graphicsLayer로 성능 최적화
                    • shadow + ambientColor로 글로우
                    • collectIsPressedAsState로 누름 감지
                """.trimIndent(),
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 2200)
@Composable
private fun BreathingButtonDemoPreview() {
    BreathingButtonDemo()
}
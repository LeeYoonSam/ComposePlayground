package com.ys.composeplayground.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * 🟢 Beginner #5: 슬라이드 진입 애니메이션
 *
 * 📖 핵심 개념
 *
 * slideIn/slideOut 계열 API를 사용하여 요소가 화면 밖에서 원하는 방향으로 등장하거나 퇴장합니다. offset을 직접 지정하거나 요소 크기 기반으로 계산할 수 있어요.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * slideInHorizontally | 좌/우에서 슬라이드 진입
 * slideOutHorizontally | 좌/우로 슬라이드 퇴장
 * slideInVertically | 위/아래에서 슬라이드 진입
 * slideOutVertically | 위/아래로 슬라이드 퇴장
 * initialOffsetX/Y | 시작 위치 지정 (람다)
 * targetOffsetX/Y | 도착 위치 지정 (람다)
 *
 * 💡 동작 원리
 *
 * ```
 * [왼쪽에서 진입]
 * initialOffsetX = { -it }  // -전체너비 (왼쪽 밖)
 *        ↓ slideInHorizontally
 * targetOffsetX = 0         // 원래 위치
 *
 * [오른쪽으로 퇴장]
 * initialOffsetX = 0
 *        ↓ slideOutHorizontally
 * targetOffsetX = { it }    // +전체너비 (오른쪽 밖)
 * ```
 *
 * 학습 목표:
 * 1. slideInHorizontally/Vertically 사용법
 * 2. initialOffset/targetOffset 람다 이해
 * 3. 방향별 슬라이드 구현
 * 4. Fade와 조합하여 부드러운 효과
 */

// ============================================
// 기본 버전: 수평 슬라이드 (좌→우)
// ============================================
@Composable
fun SlideHorizontalBasic(
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { visible = !visible },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            )
        ) {
            Text(if (visible) "Hide" else "Show")
        }

        AnimatedVisibility(
            visible = visible,
            // ✨ 왼쪽에서 슬라이드 인
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth ->  -fullWidth }, // 왼쪽 밖에서
                animationSpec = tween(500)
            ),
            // ✨ 오른쪽으로 슬라이드 아웃
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }, // 오른쪽 밖으로
                animationSpec = tween(500)
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "← 왼쪽에서 등장 →",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ============================================
// 수직 슬라이드 (위/아래)
// ============================================
@Composable
fun SlideVerticalExample(
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    var fromTop by remember { mutableStateOf(true) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { visible = !visible },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text(if (visible) "Hide" else "Show")
            }

            OutlinedButton(
                onClick = { fromTop = !fromTop }
            ) {
                Text(if (fromTop) "↓ 위에서" else "↑ 아래에서")
            }
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                // fromTop에 따라 방향 변경
                initialOffsetY = { fullHeight ->
                    if (fromTop) -fullHeight else fullHeight
                },
                animationSpec = tween(400)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight ->
                    if (fromTop) -fullHeight else fullHeight
                },
                animationSpec = tween(400)
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (fromTop) "↓ 위에서 등장" else "↑ 아래에서 등장",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ============================================
// Slide + Fade 조합
// ============================================
@Composable
fun SlideWithFade(
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { visible = !visible },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color(0xFF9C27B0)
            )
        ) {
            Text(if (visible) "Hide" else "Show")
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { -it / 2 } // 절반만 이동
            ) + fadeIn(
                initialAlpha = 0.3f
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it / 2 }
            ) + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    contentColor = Color(0xFF9C27B0)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Slide + Fade 조합",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


// ============================================
// Spring 기반 슬라이드 (바운스)
// ============================================
@Composable
fun SlideWithSpring(
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { visible = !visible },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color(0xFFFF5722)
            )
        ) {
            Text(if (visible) "Hide" else "Show")
        }

        AnimatedVisibility(
            visible = visible,
            // ✨ Spring으로 바운스 효과
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(300)
            ) + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    contentColor = Color(0xFFFF5722)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🏀 Spring 바운스!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


// ============================================
// 4방향 슬라이드 데모
// ============================================
@Composable
fun FourDirectionSlide(
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    var direction by remember { mutableStateOf("left") }

    val directions = listOf(
        "left" to "← 좌",
        "right" to "우 →",
        "top" to "↑ 상",
        "bottom" to "하 ↓"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 방향 선택
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            directions.forEach { (dir, label) ->
                FilterChip(
                    onClick = { direction = dir },
                    label = { Text(label, fontSize = 12.sp) },
                    selected = direction == dir,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF673AB7)
                    )
                )
            }
        }

        Button(
            onClick = { visible = !visible },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF673AB7)
            )
        ) {
            Text(if (visible) "Hide" else "Show")
        }

        AnimatedVisibility(
            visible = visible,
            enter = when (direction) {
                "left" -> slideInHorizontally { -it }
                "right" -> slideInHorizontally { it }
                "top" -> slideInVertically { -it }
                else -> slideInVertically { it }
            } + fadeIn(),
            exit =  when (direction) {
                "left" -> slideOutHorizontally { -it }
                "right" -> slideOutHorizontally { it }
                "top" -> slideOutVertically { -it }
                else -> slideOutVertically { it }
            } + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(80.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF673AB7)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "방향: $direction",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ============================================
// 드로어 메뉴 스타일
// ============================================
@Composable
fun DrawerStyleSlide(
    modifier: Modifier = Modifier
) {
    var isOpen by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        // 메인 콘텐츠
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFEEEEEE)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("메인 콘텐츠", color = Color.Gray)
                    Button(
                        onClick = { isOpen = !isOpen },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF607D8B)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isOpen) "Close" else "Open Drawer")
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isOpen,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp),
                shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF607D8B)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "📋 메뉴",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.3f))

                    listOf("🏠 홈", "👤 프로필", "⚙️ 설정", "❓ 도움말").forEach { item ->
                        Text(
                            text = item,
                            color = Color.White,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .clickable { isOpen = false}
                        )
                    }
                }
            }
        }
    }
}

// ============================================
// 바텀시트 스타일
// ============================================
@Composable
fun BottomSheetStyleSlide(
    modifier: Modifier = Modifier
) {
    var isOpen by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        // 메인 콘텐츠
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                contentColor = Color(0xFFF5F5F5)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { isOpen = !isOpen },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BCD4)
                    )
                ) {
                    Text(if (isOpen) "Close Sheet" else " Open Bottom Sheet")
                }
            }
        }

        // 바텀시트
        AnimatedVisibility(
            visible = isOpen,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(
                initialOffsetY = { it }, // 아래에서
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // 아래로
                animationSpec = tween(300)
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF00BCD4)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 핸들 바
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White.copy(alpha = 0.5f))
                            .clickable { isOpen = false }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "바텀시트 콘텐츠",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "아래에서 Spring으로 올라옵니다!",
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

// ============================================
// 스낵바 스타일
// ============================================
@Composable
fun SnackbarStyleSlide(
    modifier: Modifier = Modifier
) {
    var showSnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            delay(2500)
            showSnackbar = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Button(
            onClick = { showSnackbar = true },
            modifier = Modifier.align(Alignment.Center),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF795548)
            )
        ) {
            Text("Show Snackbar")
        }

        AnimatedVisibility(
            visible = showSnackbar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            enter = slideInVertically(
                initialOffsetY = { it }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it }
            ) + fadeOut()
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF323232)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "메시지가 저장되었습니다",
                        color = Color.White
                    )
                    TextButton(
                        onClick = { showSnackbar = false }
                    ) {
                        Text("닫기", color = Color(0xFF00BCD4))
                    }
                }
            }
        }
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun SlideAnimationDemo() {
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
            text = "Slide Animation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        // 기본 수평
        DemoSection(title = "기본 - 수평 슬라이드 (좌↔우)") {
            SlideHorizontalBasic()
        }

        // 수직
        DemoSection(title = "수직 슬라이드 (상↔하)") {
            SlideVerticalExample()
        }

        // Slide + Fade
        DemoSection(title = "Slide + Fade 조합") {
            SlideWithFade()
        }

        // Spring
        DemoSection(title = "Spring 바운스 슬라이드") {
            SlideWithSpring()
        }

        // 4방향
        DemoSection(title = "4방향 슬라이드") {
            FourDirectionSlide()
        }

        // 드로어
        DemoSection(title = "드로어 메뉴 스타일") {
            DrawerStyleSlide()
        }

        // 바텀시트
        DemoSection(title = "바텀시트 스타일") {
            BottomSheetStyleSlide()
        }

        // 스낵바
        DemoSection(title = "스낵바 스타일") {
            SnackbarStyleSlide()
        }

        // 가이드
        OffsetGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun OffsetGuide() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        FeatureSection(
            features = """
                initialOffsetX/Y 람다 파라미터:
                • it = 요소의 전체 너비/높이
                
                수평 슬라이드:
                • { -it } = 왼쪽 밖에서 시작
                • { it } = 오른쪽 밖에서 시작
                • { -it / 2 } = 왼쪽 절반에서 시작
                
                수직 슬라이드:
                • { -it } = 위쪽 밖에서 시작
                • { it } = 아래쪽 밖에서 시작
                
                💡 Fade와 조합하면 더 부드러움:
                slideIn() + fadeIn()
                
                💡 Spring으로 바운스 효과:
                animationSpec = spring(DampingRatioMediumBouncy)
            """.trimIndent(),
            type = FeatureTextType.TIP
        )
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun SlideAnimationDemoPreview() {
    SlideAnimationDemo()
}
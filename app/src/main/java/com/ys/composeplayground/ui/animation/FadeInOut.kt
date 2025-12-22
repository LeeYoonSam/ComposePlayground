package com.ys.composeplayground.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * 🟢 Beginner #4: 페이드 인/아웃 애니메이션
 *
 * 📖 핵심 개념
 *
 * AnimatedVisibility는 Composable의 등장/퇴장을 애니메이션하는 컨테이너예요. fadeIn/fadeOut을 사용하면 투명도 변화로 부드럽게 나타나고 사라집니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * AnimatedVisibility | 등장/퇴장 애니메이션 컨테이너
 * fadeIn | 투명 → 불투명 전환
 * fadeOut | 불투명 → 투명 전환
 * `+` 연산자 | 여러 애니메이션 조합
 * scaleIn/Out | 크기 변화 효과
 *
 * 💡 동작 원리
 *
 * ```
 * [숨김] visible = false, alpha = 0f
 *        ↓ fadeIn (alpha 보간)
 * [보임] visible = true, alpha = 1f
 *
 * EnterTransition: fadeIn()
 * ExitTransition: fadeOut()
 * ```
 *
 * 학습 목표:
 * 1. AnimatedVisibility 기본 사용법
 * 2. fadeIn/fadeOut 커스터마이징
 * 3. 애니메이션 조합 (+)
 * 4. 다양한 EnterTransition/ExitTransition
 */

// ============================================
// 기본 버전: 단순 페이드
// ============================================
@Composable
fun FadeInOutBasic(
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Button은 interactionSource라는 파라미터를 제공합니다. 이를 통해 버튼의 상태(눌림, 포커스 등)를 관찰할 수 있습니다. (pointerInput 이 안먹힘)
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.9f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "button scale"
        )

        Button(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            interactionSource = interactionSource,
            onClick = { visible = !visible },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE)
            )
        ) {
            Text(if (visible) "Hide" else "Show")
        }

        // ✨ 기본 페이드 인/아웃
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF6200EE)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👋",
                    fontSize = 48.sp
                )
            }
        }
    }
}

// ============================================
// Fade + Scale 조합
// ============================================
@Composable
fun FadeWithScale(
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { visible = !visible },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE)
            )
        ) {
            Text(if (visible) "Hide" else "Show")
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                animationSpec = tween(400)
            ) + scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(400)
            ),
            exit = fadeOut(
                animationSpec = tween(400)
            ) + scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(400)
            )
        ) {
            Card(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF03DAC5)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🚀",
                        fontSize = 48.sp
                    )
                }
            }
        }
    }
}

// ============================================
// Spring 기반 페이드
// ============================================
@Composable
fun FadeWithSpring(
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { visible = !visible },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF5722)
            )
        ) {
            Text(if (visible) "Hide" else "Show")
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + scaleIn(
                initialScale = 0.5f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
            exit = fadeOut() + scaleOut(
                targetScale = 0.5f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy
                )
            )
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF5722)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔥",
                    fontSize = 48.sp
                )
            }
        }
    }
}

// ============================================
// 토스트 스타일 메시지
// ============================================
@Composable
fun ToastMessage(
    modifier: Modifier = Modifier
) {
    var showToast by remember { mutableStateOf(false) }

    // 자동으로 사라지게 하기
    LaunchedEffect(showToast) {
        if (showToast) {
            delay(2000)
            showToast = false
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Button(
            onClick = { showToast = true },
            modifier = Modifier.align(Alignment.Center),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Text("Show Toast")
        }

        // 토스트 메시지
        AnimatedVisibility(
            visible = showToast,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            enter = fadeIn(tween(200)) + slideInVertically(
                initialOffsetY = { it }, // 아래에서 올라옴
                animationSpec = tween(300)
            ),
            exit = fadeOut(tween(200)) + slideOutVertically(
                targetOffsetY = { it }, // 아래로 내려감
                animationSpec = tween(300)
            )
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF323232)
                )
            ) {
                Text(
                    text = "✓ 저장되었습니다!",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// ============================================
// 모달/오버레이 스타일
// ============================================
@Composable
fun ModalOverlay(
    modifier: Modifier = Modifier
) {
    var showModal by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        Button(
            onClick = { showModal = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9C27B0)
            )
        ) {
            Text("Show Modal")
        }

        // 배경 디밍
        AnimatedVisibility(
            visible = showModal,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
        }

        // 모달 콘텐츠
        AnimatedVisibility(
            visible = showModal,
            modifier = Modifier.align(Alignment.Center),
            enter = fadeIn(tween(300)) + scaleIn(
                initialScale = 0.9f,
                animationSpec = tween(300)
            ),
            exit = fadeOut(tween(200)) + scaleOut(
                targetScale = 0.9f,
                animationSpec = tween(200)
            )
        ) {
            Card(
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "🎉",
                        fontSize = 48.sp
                    )
                    Text(
                        text = "모달 다이얼로그",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "배경 디밍과 콘텐츠가\n각각 페이드 애니메이션됩니다.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { showModal = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF9C27B0)
                        )
                    ) {
                        Text("닫기")
                    }
                }
            }
        }
    }
}

// ============================================
// 크로스페이드 (콘텐츠 전환)
// ============================================
@Composable
fun CrossfadeExample(
    modifier: Modifier = Modifier
) {
    var currentPage by remember { mutableIntStateOf(0) }
    val pages = listOf("🏠 Home", "🔍 Search", "👤 Profile")
    val colors = listOf(
        Color(0xFF2196F3),
        Color(0xFF4CAF50),
        Color(0xFFFF9800)
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 탭 버튼들
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pages.forEachIndexed { index, _ ->
                Button(
                    onClick = { currentPage = index },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentPage == index)
                            colors[index]
                        else
                            Color.LightGray
                    ),
                    modifier = Modifier.size(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("${index + 1}")
                }
            }
        }

        // ✨ Crossfade로 콘텐츠 전환
        Crossfade(
            targetState =  currentPage,
            animationSpec = tween(500),
            label = "crossfade"
        ) { page ->
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors[page]),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = pages[page],
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ============================================
// 순차적 페이드 인
// ============================================
@Composable
fun SequentialFadeIn(
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { visible = !visible },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF607D8B)
            )
        ) {
            Text(if (visible) "Reset" else "Animate")
        }

        // 순차적으로 나타나는 아이템들
        listOf(
            "First Item" to 0,
            "Second Item" to 100,
            "Third Item" to 200,
            "Fourth Item" to 300
        ).forEach { (text, delay) ->
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = delay
                    )
                ) + slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = delay
                    )
                ),
                exit = fadeOut(tween(200))
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF607D8B)
                    )
                ) {
                    Text(
                        text = text,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White
                    )
                }
            }
        }
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun FadeInOutDemo() {
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
            text = "Fade In/Out Animation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        // 기본 페이드
        DemoSectionWithBox(title = "기본 - fadeIn/fadeOut") {
            FadeInOutBasic()
        }

        // 페이드 + 스케일
        DemoSectionWithBox(title = "Fade + Scale 조합") {
            FadeWithScale()
        }

        // Spring 기반
        DemoSectionWithBox(title = "Spring 기반 (바운스)") {
            FadeWithSpring()
        }

        // 토스트
        DemoSectionWithBox(title = "토스트 메시지") {
            ToastMessage(modifier = Modifier.height(120.dp))
        }

        // 모달
        DemoSectionWithBox(title = "모달 오버레이") {
            ModalOverlay(modifier = Modifier.height(200.dp))
        }

        // Crossfade
        DemoSectionWithBox(title = "Crossfade (콘텐츠 전환)") {
            CrossfadeExample()
        }

        // 순차적 페이드
        DemoSectionWithBox(title = "순차적 페이드 인 (Staggered)") {
            SequentialFadeIn()
        }

        // 가이드
        TransitionGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TransitionGuide() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        FeatureSection(
            customTitle = "📚 EnterTransition / ExitTransition 종류",
            features = """
                EnterTransition:
                • fadeIn() - 투명 → 불투명
                • slideIn() - 지정 위치에서 슬라이드
                • slideInHorizontally() - 좌/우에서
                • slideInVertically() - 위/아래에서
                • scaleIn() - 작은 크기에서 확대
                • expandIn() - 크기 확장
                • expandHorizontally() - 가로 확장
                • expandVertically() - 세로 확장
                
                ExitTransition:
                • fadeOut(), slideOut(), scaleOut()...
                
                💡 조합: fadeIn() + scaleIn() + slideIn()
            """.trimIndent(),
        )
    }
}

@Preview(showBackground = true, heightDp = 1600)
@Composable
fun FadeInOutDemoPreview() {
    FadeInOutDemo()
}
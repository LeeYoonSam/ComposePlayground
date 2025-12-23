package com.ys.composeplayground.ui.animation

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 🟠 Advanced #15: Parallax Scroll (시차 스크롤)
 *
 * 📖 핵심 개념
 *
 * 스크롤 오프셋에 다른 계수를 곱하여 각 레이어가 다른 속도로 이동하게 만듭니다. 배경은 느리게, 전경은 빠르게 움직여 깊이감을 표현합니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * rememberScrollState | 스크롤 상태 관리
 * scrollState.value | 현재 스크롤 오프셋 (픽셀)
 * graphicsLayer | 효율적인 변환 (하드웨어 가속)
 * translationY/X | 위치 이동
 * alpha, scale | 투명도/크기 변환
 *
 * 💡 동작 원리
 *
 * ```
 * [스크롤 100px]
 * 배경 레이어: 100 * 0.3 = 30px 이동 (느림)
 * 중간 레이어: 100 * 0.6 = 60px 이동
 * 전경 레이어: 100 * 1.0 = 100px 이동 (빠름)
 *
 * → 레이어별 속도 차이로 깊이감 표현
 *
 * 계수 가이드:
 * - 0.1 ~ 0.3 = 먼 배경 (느림)
 * - 0.5 ~ 0.7 = 중간 레이어
 * - 1.0+ = 전경 (빠름/오버레이)
 * - 음수 = 반대 방향 이동
 * ```
 *
 * 학습 목표:
 * 1. 스크롤 오프셋에 다른 계수를 적용하여 시차 효과
 * 2. 헤더 이미지 패럴랙스
 * 3. 다층 배경 효과
 * 4. 카드 리스트 시차 효과
 */

// ============================================
// 기본 패럴랙스 헤더
// ============================================
@Composable
fun BasicParallaxHeader(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxWidth()) {
        // 배경 이미지 영역 (느리게 스크롤)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .graphicsLayer {
                    // 스크롤의 50%만 적용 → 느리게 움직임
                    translationY = scrollState.value * 0.5f
                }
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1976D2),
                            Color(0xFF64B5F6),
                            Color(0xFFBBDEFB)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // 배경 장식 요소들
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier
                    .size(120.dp)
                    .graphicsLayer {
                        translationY = -scrollState.value * 0.2f
                    }
            )
        }

        // 스크롤 가능한 콘텐츠
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            // 헤더 공간 확보
            Spacer(modifier = Modifier.height(200.dp))

            // 콘텐츠 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Parallax Header",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    repeat(8) { index ->
                        ContentItem(index = index)
                    }
                }
            }
        }

        // 스크롤 정보 표시
        Text(
            text = "Scroll: ${scrollState.value}",
            fontSize = 12.sp,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun ContentItem(index: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Column {
                Text(
                    text = "Item ${index + 1}",
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "패럴랙스 스크롤 예제 아이템입니다",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// ============================================
// 다층 패럴랙스 배경
// ============================================
@Composable
fun MultiLayerParallax(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        // 레이어 1: 가장 먼 배경 (가장 느림)
        Box(
            modifier = Modifier
                .offset()
                .fillMaxSize()
                .graphicsLayer {
                    translationY = scrollState.value * 0.1f
                }
                .background(Color(0xFF0D47A1))
        ) {
            // 별들 (가장 느림)
            repeat(5) { i ->
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier
                        .size(16.dp)
                        .offset(
                            x = (30 + i * 60).dp,
                            y = (20 + i * 30).dp
                        )
                )
            }
        }

        // 레이어 2: 중간 배경
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    translationY = scrollState.value * 0.3f
                }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF1565C0)
                        )
                    )
                )
        )

        // 레이어 3: 산 (중간 속도)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    translationY = scrollState.value * 0.5f
                }
        ) {
            // 산 모양 시뮬레이션
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                Mountain(height = 100, color = Color(0xFF1976D2))
                Mountain(height = 130, color = Color(0xFF1976D2))
                Mountain(height = 90, color = Color(0xFF1976D2))
            }
        }

        // 레이어 4: 앞쪽 산 (빠른 속도)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    translationY = scrollState.value * 0.7f
                }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                Mountain(height = 70, color = Color(0xFF2196F3))
                Mountain(height = 90, color = Color(0xFF2196F3))
                Mountain(height = 60, color = Color(0xFF2196F3))
                Mountain(height = 80, color = Color(0xFF2196F3))
            }
        }

        // 스크롤 가능 영역
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(300.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Multi-Layer Parallax",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "스크롤하여 레이어별 속도 차이를 확인하세요",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}

@Composable
private fun Mountain(height: Int, color: Color) {
    Box(
        modifier = Modifier
            .size(width = 80.dp, height = height.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 40.dp,
                    topEnd = 40.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
            .background(color)
    )
}

// ============================================
// 카드 리스트 시차 효과
// ============================================
@Composable
fun ParallaxCardList(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(10) { index ->
                ParallaxCard(
                    index = index,
                    scrollOffset = scrollState.value,
                    parallaxFactor = 0.1f + index * 0.05f
                )
            }
        }
    }
}

@Composable
private fun ParallaxCard(
    index: Int,
    scrollOffset: Int,
    parallaxFactor: Float
) {
    val colors = listOf(
        Color(0xFFE91E63),
        Color(0xFF9C27B0),
        Color(0xFF673AB7),
        Color(0xFF3F51B5),
        Color(0xFF2196F3),
        Color(0xFF00BCD4),
        Color(0xFF009688),
        Color(0xFF4CAF50),
        Color(0xFFFF9800),
        Color(0xFFFF5722)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                // 각 카드마다 다른 시차 적용
                translationX = scrollOffset * parallaxFactor * 0.3f
                // 스크롤에 따른 투명도 변화 (선택적)
                alpha = (1f - scrollOffset * 0.0005f).coerceIn(0.5f, 1f)
            },
        colors = CardDefaults.cardColors(containerColor = colors[index % colors.size])
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Card ${index + 1}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Parallax: ${String.format("%.2f", parallaxFactor)}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ============================================
// 페이드 + 스케일 패럴랙스 헤더
// ============================================
@Composable
fun FadeScaleParallaxHeader(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val maxScroll = 300f

    // 스크롤에 따른 진행률 (0~1)
    val scrollProgress = (scrollState.value / maxScroll).coerceIn(0f, 1f)

    Box(modifier = modifier.fillMaxWidth()) {
        // 헤더 배경
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .graphicsLayer {
                    // 스케일 증가
                    val scale = 1f + scrollProgress * 0.3f
                    scaleX = scale
                    scaleY = scale
                    // 투명도 감소
                    alpha = 1f - scrollProgress * 0.7f
                    // 위로 이동
                    translationY = scrollState.value * 0.5f
                }
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF6B6B),
                            Color(0xFFEE5A24),
                            Color(0xFFFF9F43)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(64.dp)
                        .graphicsLayer {
                            rotationZ = scrollProgress * 180f
                        }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Fade & Scale",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // 콘텐츠
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(230.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "스크롤에 따른 효과",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Progress: ${String.format("%.1f", scrollProgress * 100)}%",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    repeat(6) { index ->
                        ContentItem(index = index)
                    }
                }
            }
        }
    }
}

// ============================================
// 수평 패럴랙스
// ============================================
@Composable
fun HorizontalParallax(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF263238))
    ) {
        // 배경 레이어 (느림)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = -scrollState.value * 0.3f
                },
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(10) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // 중간 레이어
        Row(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = -scrollState.value * 0.6f
                },
            horizontalArrangement = Arrangement.spacedBy(80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(8) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                )
            }
        }

        // 스크롤 콘텐츠
        Row(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(scrollState)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(10) { index ->
                Card(
                    modifier = Modifier.size(150.dp, 160.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF37474F)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Card",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
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
fun ParallaxScrollDemo() {
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
            text = "Parallax Scroll",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "기본 패럴랙스 헤더") {
            BasicParallaxHeader(modifier = Modifier.height(450.dp))
        }

        DemoSection(title = "다층 패럴랙스 배경") {
            MultiLayerParallax()
        }

        DemoSection(title = "카드 리스트 시차 효과") {
            ParallaxCardList()
        }

        DemoSection(title = "페이드 + 스케일 헤더") {
            FadeScaleParallaxHeader(modifier = Modifier.height(500.dp))
        }

        DemoSection(title = "수평 패럴랙스") {
            HorizontalParallax()
        }

        ParallaxScrollGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ParallaxScrollGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Parallax Scroll 가이드")

            CodeSection(
                title = "기본 패럴랙스",
                code = """
val scrollState = rememberScrollState()

Box(
    modifier = Modifier.graphicsLayer {
        translationY = scrollState.value * 0.5f
    }
)
                """.trimIndent()
            )

            FeatureSection(
                customTitle = "계수 가이드",
                features = """
- 0.1 ~ 0.3 = 먼 배경 (느림)
- 0.5 ~ 0.7 = 중간 레이어
- 1.0+ = 전경 (빠름/오버레이)
- 음수 = 반대 방향 이동
                """.trimIndent()
            )

            FeatureSection(
                features = """
- graphicsLayer는 하드웨어 가속
- 투명도/스케일 결합 가능
- 수평/수직 모두 적용 가능
                """.trimIndent(),
                type = FeatureTextType.TIP
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 2200)
@Composable
private fun ParallaxScrollDemoPreview() {
    ParallaxScrollDemo()
}
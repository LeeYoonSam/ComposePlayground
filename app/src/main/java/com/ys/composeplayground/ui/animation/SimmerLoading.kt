package com.ys.composeplayground.ui.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ys.composeplayground.ModifierDemos


/**
 * 🟡 Intermediate #6: Shimmer 로딩 애니메이션
 *
 * 📖 핵심 개념
 *
 * rememberInfiniteTransition을 사용하여 무한 반복되는 애니메이션을 만들고, 그라데이션 위치를 이동시켜 Shimmer 효과를 구현합니다. 콘텐츠 로딩 중 스켈레톤 UI에 적용하면 앱이 훨씬 세련되어 보여요!
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * rememberInfiniteTransition | 무한 반복 애니메이션
 * animateFloat | Float 값 무한 애니메이션
 * infiniteRepeatable | 반복 설정 (Restart/Reverse)
 * Brush.linearGradient | 그라데이션 생성
 * composed | 재사용 가능한 Modifier
 *
 * 💡 동작 원리
 *
 * ```
 * [그라데이션 위치 이동]
 * offset: -width → +width (무한 반복)
 *        ↓
 * Brush.linearGradient(
 *     colors = [Gray, LightGray, Gray],
 *     start = Offset(animatedX, 0),
 *     end = Offset(animatedX + shimmerWidth, 0)
 * )
 * ```
 *
 * 학습 목표:
 * 1. rememberInfiniteTransition 사용법
 * 2. infiniteRepeatable 설정
 * 3. Brush.linearGradient로 그라데이션 생성
 * 4. 재사용 가능한 shimmer Modifier 만들기
 */

// ============================================
// 기본 Shimmer Modifier
// ============================================
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    val shimmerColors = listOf(
        Color(0xFFE0E0E0),
        Color(0xFFF5F5F5),
        Color(0xFFE0E0E0)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(startOffsetX, 0f),
        end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
    )

    background(brush)
        .onGloballyPositioned { size = it.size }
}

// ============================================
// 커스터마이징 가능한 Shimmer Modifier
// ============================================
fun Modifier.shimmerEffect(
    colors: List<Color> = listOf(
        Color(0xFFE0E0E0),
        Color(0xFFF5F5F5),
        Color(0xFFE0E0E0)
    ),
    durationMillis: Int = 1000,
    angle: Float = 20f
): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )

    val width = size.width.toFloat()
    val height = size.height.toFloat()

    // 대각선 이동을 위한 계산
    val offset = (width + height) * progress

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(offset - width, 0f),
        end = Offset(offset, height)
    )

    background(brush)
        .onGloballyPositioned { size = it.size }
}

// ============================================
// 기본 스켈레톤 카드
// ============================================
@Composable
fun ShimmerCardBasic(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지 스켈레톤
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 제목 스켈레톤
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                // 부제목 스켈레톤
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

// ============================================
// 소셜 미디어 포스트 스켈레톤
// ============================================
@Composable
fun SocialPostSkeleton(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 헤더: 프로필 + 이름
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )

                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(10.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }
            }

            // 본문 텍스트
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (it == 2) 0.6f else 1f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }
            }

            // 이미지 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )

            // 액션 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

// ============================================
// 상품 카드 스켈레톤
// ============================================
@Composable
fun ProductCardSkeleton(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(160.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // 상품 이미지
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .shimmerEffect()
            )

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 상품명
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                // 가격
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

// ============================================
// 다크 테마 Shimmer
// ============================================
@Composable
fun DarkShimmerCard(
    modifier: Modifier = Modifier
) {
    val darkShimmerColors = listOf(
        Color(0xFF2A2A2A),
        Color(0xFF3D3D3D),
        Color(0xFF2A2A2A)
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .shimmerEffect(colors = darkShimmerColors)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(colors = darkShimmerColors)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(colors = darkShimmerColors)
                )
            }
        }
    }
}

// ============================================
// 컬러풀 Shimmer (브랜드 컬러)
// ============================================
@Composable
fun ColorfulShimmerCard(
    modifier: Modifier = Modifier
) {
    val brandShimmerColors = listOf(
        Color(0xFF6200EE).copy(alpha = 0.3f),
        Color(0xFF6200EE).copy(alpha = 0.1f),
        Color(0xFF6200EE).copy(alpha = 0.3f)
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .shimmerEffect(colors = brandShimmerColors, durationMillis = 1500)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(colors = brandShimmerColors, durationMillis = 1500)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(colors = brandShimmerColors, durationMillis = 1500)
                )
            }
        }
    }
}

// ============================================
// 실제 콘텐츠와 전환
// ============================================
@Composable
fun ShimmerToContent(
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { isLoading = !isLoading },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE)
            )
        ) {
            Text(if (isLoading) "Load Content" else "Show Skeleton")
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isLoading) {
                    // 스켈레톤
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .shimmerEffect()
                    )
                } else {
                    // 실제 콘텐츠
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6200EE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🧑", fontSize = 28.sp)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(18.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .shimmerEffect()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(14.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .shimmerEffect()
                        )
                    } else {
                        Text(
                            text = "John Doe",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Software Engineer",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

// ============================================
// 리스트 스켈레톤
// ============================================
@Composable
fun ShimmerList(
    itemCount: Int = 5,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(itemCount) {
            ShimmerCardBasic()
        }
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun ShimmerDemo() {
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
            text = "Shimmer Loading Animation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        // 기본 스켈레톤
        DemoSection(title = "기본 스켈레톤 카드") {
            ShimmerCardBasic()
        }

        // 소셜 포스트
        DemoSection(title = "소셜 미디어 포스트 스켈레톤") {
            SocialPostSkeleton()
        }

        // 상품 카드 (가로 스크롤)
        DemoSection(title = "상품 카드 스켈레톤") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(2) {
                    ProductCardSkeleton()
                }
            }
        }

        // 다크 테마
        DemoSection(title = "다크 테마 Shimmer") {
            DarkShimmerCard()
        }

        // 컬러풀 (브랜드)
        DemoSection(title = "브랜드 컬러 Shimmer") {
            ColorfulShimmerCard()
        }

        // 실제 전환
        DemoSection(title = "스켈레톤 ↔ 콘텐츠 전환") {
            ShimmerToContent()
        }

        // 가이드
        ShimmerGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ShimmerGuide() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            "📚 Shimmer 구현 가이드",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            """
            핵심 구성요소:
            
            1. rememberInfiniteTransition
               → 무한 반복 애니메이션 생성
            
            2. animateFloat + infiniteRepeatable
               → 0 → 1000 반복 이동값 생성
            
            3. Brush.linearGradient
               → 이동하는 그라데이션 생성
            
            4. composed { } 
               → 재사용 가능한 Modifier
            
            💡 커스터마이징 포인트:
            • colors: 그라데이션 색상
            • durationMillis: 애니메이션 속도
            • 대각선 방향: start/end Offset 조절
            
            💡 성능 팁:
            • 스켈레톤 개수 제한 (5-10개)
            • 복잡한 형태는 단순화
            """.trimIndent(),
            fontSize = 12.sp,
            color = Color.Gray,
            lineHeight = 18.sp
        )
    }
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
fun ShimmerDemoPreview() {
    ShimmerDemo()
}
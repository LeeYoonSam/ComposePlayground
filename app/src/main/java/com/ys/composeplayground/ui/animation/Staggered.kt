package com.ys.composeplayground.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 🟡 Intermediate #8: Staggered 리스트 애니메이션
 *
 * 📖 핵심 개념
 *
 * LaunchedEffect와 delay를 사용하여 각 아이템의 애니메이션 시작 시점을 엇갈리게(stagger) 설정합니다. 리스트가 순차적으로 나타나면서 시각적으로 훨씬 세련된 느낌을 줘요!
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * LaunchedEffect | 코루틴 시작
 * delay | 지연 실행
 * Animatable | 애니메이션 상태
 * animateTo | 목표값으로 애니메이션
 * forEachIndexed | 인덱스 기반 지연 계산
 *
 * 💡 동작 원리
 *
 * ```
 * 아이템 0: delay(0ms)   → 애니메이션 시작
 * 아이템 1: delay(50ms)  → 애니메이션 시작
 * 아이템 2: delay(100ms) → 애니메이션 시작
 * 아이템 3: delay(150ms) → 애니메이션 시작
 * ...
 *
 * delay = index * staggerDelay
 * ```
 *
 * 학습 목표:
 * 1. LaunchedEffect + delay로 순차적 애니메이션
 * 2. Animatable을 사용한 커스텀 stagger 효과
 * 3. AnimatedVisibility의 delayMillis 활용
 * 4. 다양한 stagger 패턴 구현
 */

// ============================================
// 데이터 클래스
// ============================================
data class ListItemData(
    val id: Int,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color
)

val sampleItems = listOf(
    ListItemData(1, "Messages", "3 unread", Icons.Default.Email, Color(0xFF2196F3)),
    ListItemData(2, "Calendar", "2 events today", Icons.Default.DateRange, Color(0xFF4CAF50)),
    ListItemData(3, "Photos", "1,234 photos", Icons.Default.Photo, Color(0xFFFF9800)),
    ListItemData(4, "Music", "Now playing", Icons.Default.MusicNote, Color(0xFFE91E63)),
    ListItemData(5, "Settings", "App preferences", Icons.Default.Settings, Color(0xFF607D8B)),
    ListItemData(6, "Profile", "Edit profile", Icons.Default.Person, Color(0xFF9C27B0)),
)

// ============================================
// 기본 Staggered 아이템 (Animatable)
// ============================================
@Composable
fun StaggeredListItemBasic(
    item: ListItemData,
    index: Int,
    staggerDelay: Long = 50L,
    modifier: Modifier = Modifier
) {
    val alpha = remember { Animatable(0f) }
    val offsetY = remember { Animatable(30f) }

    LaunchedEffect(Unit) {
        delay(index * staggerDelay)
        // 병렬 실행
        launch { alpha.animateTo(1f, tween(300)) }
        launch {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha.value
                translationY = offsetY.value
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(item.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = item.subtitle,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }

}

// ============================================
// Staggered 리스트 (Column)
// ============================================
@Composable
fun StaggeredListBasic(
    modifier: Modifier = Modifier
) {
    var show by remember { mutableStateOf(false) }
    var key by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = {
                show = !show
                if (show) key++  // 리셋을 위한 key 변경
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(if (show) "Reset" else "Show List")
        }

        if (show) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sampleItems.forEachIndexed { index, item ->
                    StaggeredListItemBasic(
                        item = item,
                        index = index,
                        staggerDelay = 80L
                    )
                }
            }
        }
    }
}

// ============================================
// Slide 방향별 Stagger
// ============================================
@Composable
fun StaggeredSlideItem(
    item: ListItemData,
    index: Int,
    direction: SlideDirection,
    staggerDelay: Long = 60L,
    modifier: Modifier = Modifier
) {
    val alpha = remember { Animatable(0f) }
    val offset = remember { Animatable(100f) }

    LaunchedEffect(Unit) {
        delay(index * staggerDelay)
        launch { alpha.animateTo(1f, tween(300)) }
        launch {
            offset.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha.value
                when (direction) {
                    SlideDirection.Left -> translationX = -offset.value
                    SlideDirection.Right -> translationX = offset.value
                    SlideDirection.Top -> translationY = -offset.value
                    SlideDirection.Bottom -> translationY = offset.value
                    SlideDirection.Alternate -> {
                        translationX = if (index % 2 == 0) -offset.value else offset.value
                    }
                }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(item.color),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(item.title, fontWeight = FontWeight.Medium)
        }
    }
}

enum class SlideDirection {
    Left, Right, Top, Bottom, Alternate
}

@Composable
fun StaggeredSlideDemo(
    modifier: Modifier = Modifier
) {
    var show by remember { mutableStateOf(false) }
    var direction by remember { mutableStateOf(SlideDirection.Left) }
    var key by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 방향 선택
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            SlideDirection.entries.forEach { dir ->
                FilterChip(
                    onClick = {
                        direction = dir
                        show = false
                    },
                    label = {
                        Text(
                            when (dir) {
                                SlideDirection.Left -> "←"
                                SlideDirection.Right -> "→"
                                SlideDirection.Top -> "↑"
                                SlideDirection.Bottom -> "↓"
                                SlideDirection.Alternate -> "↔"
                            },
                            fontSize = 14.sp
                        )
                    },
                    selected = direction == dir,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF4CAF50)
                    )
                )
            }
        }

        Button(
            onClick = {
                show = !show
                if (show) key++
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(if (show) "Reset" else "Animate")
        }

        if (show) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sampleItems.take(4).forEachIndexed { index, item ->
                    StaggeredSlideItem(
                        item = item,
                        index = index,
                        direction = direction
                    )
                }
            }
        }
    }
}

// ============================================
// Scale + Fade Stagger
// ============================================
@Composable
fun StaggeredScaleItem(
    item: ListItemData,
    index: Int,
    staggerDelay: Long = 70L,
    modifier: Modifier = Modifier
) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        delay(index * staggerDelay)
        alpha.animateTo(1f, tween(250))
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha.value
                scaleX = scale.value
                scaleY = scale.value
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = item.color)
    ) {
        Column(
            modifier = Modifier
                .size(100.dp)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun StaggeredGridDemo(
    modifier: Modifier = Modifier
) {
    var show by remember { mutableStateOf(false) }
    var key by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                show = !show
                if (show) key++
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF5722)
            )
        ) {
            Text(if (show) "Reset" else "Show Grid")
        }

        if (show) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 2열 그리드
                for (rowIndex in 0 until 3) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (colIndex in 0 until 2) {
                            val itemIndex = rowIndex * 2 + colIndex
                            if (itemIndex < sampleItems.size) {
                                StaggeredScaleItem(
                                    item = sampleItems[itemIndex],
                                    index = itemIndex,
                                    staggerDelay = 80L
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// ============================================
// AnimatedVisibility 방식
// ============================================
@Composable
fun StaggeredAnimatedVisibility(
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { visible = !visible },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9C27B0)
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(if (visible) "Hide" else "Show")
        }

        sampleItems.take(4).forEachIndexed { index, item ->
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = index * 100
                    )
                ) + slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(
                        durationMillis = 400,
                        delayMillis = index * 100,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(200)
                ) + slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            ) {
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
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(item.color),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(item.title, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

// ============================================
// 웨이브 효과 Stagger
// ============================================
@Composable
fun WaveStaggerItem(
    index: Int,
    totalItems: Int,
    staggerDelay: Long = 50L,
    modifier: Modifier = Modifier
) {
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(index * staggerDelay)
        while (true) {
            offsetY.animateTo(
                targetValue = -15f,
                animationSpec = spring(dampingRatio = 0.3f, stiffness = 500f)
            )
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = spring(dampingRatio = 0.3f, stiffness = 500f)
            )
            delay(totalItems * staggerDelay + 500L)
        }
    }

    val colors = listOf(
        Color(0xFFE91E63),
        Color(0xFF9C27B0),
        Color(0xFF673AB7),
        Color(0xFF3F51B5),
        Color(0xFF2196F3),
        Color(0xFF00BCD4),
        Color(0xFF009688),
        Color(0xFF4CAF50)
    )

    Box(
        modifier = modifier
            .offset(y = offsetY.value.dp)
            .size(30.dp)
            .clip(CircleShape)
            .background(colors[index % colors.size])
    )
}

@Composable
fun WaveStaggerDemo(
    modifier: Modifier = Modifier
) {
    val itemCount = 8

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Wave Animation",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(60.dp)
        ) {
            repeat(itemCount) { index ->
                WaveStaggerItem(
                    index = index,
                    totalItems = itemCount,
                    staggerDelay = 80L
                )
            }
        }
    }
}

// ============================================
// 타이핑 효과 Stagger
// ============================================
@Composable
fun TypingChar(
    char: Char,
    index: Int,
    staggerDelay: Long
) {
    val alpha = remember { Animatable(0f) }
    val offsetY = remember { Animatable(-20f) }

    LaunchedEffect(Unit) {
        delay(index * staggerDelay)
        alpha.animateTo(1f, tween(150))
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = spring(dampingRatio = 0.5f, stiffness = 500f)
        )
    }

    Text(
        text = char.toString(),
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF00BCD4),
        modifier = Modifier.graphicsLayer {
            this.alpha = alpha.value
            translationY = offsetY.value
        }
    )
}

@Composable
fun TypingStaggerDemo(
    modifier: Modifier = Modifier
) {
    val text = "Hello, Compose!"
    var show by remember { mutableStateOf(false) }
    var key by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                show = !show
                if (show) key++
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00BCD4)
            )
        ) {
            Text(if (show) "Reset" else "Type!")
        }

        if (show) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                text.forEachIndexed { index, char ->
                    TypingChar(
                        char = char,
                        index = index,
                        staggerDelay = 80L
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
fun StaggeredListDemo() {
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
            text = "Staggered List Animation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        // 기본 리스트
        DemoSectionWithBox(title = "기본 - Fade + Slide Up") {
            StaggeredListBasic()
        }

        // 방향별 슬라이드
        DemoSectionWithBox(title = "방향별 슬라이드") {
            StaggeredSlideDemo()
        }

        // 그리드
        DemoSectionWithBox(title = "Scale + Fade 그리드") {
            StaggeredGridDemo()
        }

        // AnimatedVisibility
        DemoSectionWithBox(title = "AnimatedVisibility 방식") {
            StaggeredAnimatedVisibility()
        }

        // 웨이브
        DemoSectionWithBox(title = "웨이브 효과 (무한 반복)") {
            WaveStaggerDemo()
        }

        // 타이핑
        DemoSectionWithBox(title = "타이핑 효과") {
            TypingStaggerDemo()
        }

        // 가이드
        StaggerGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun StaggerGuide() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TitleSection("📚 Stagger 구현 가이드")

        CodeSection(
            title = "방법 1: Animatable + LaunchedEffect",
            code = """
                LaunchedEffect(Unit) {
                    delay(index * staggerDelay)
                    animatable.animateTo(targetValue)
                }    
            """.trimIndent()
        )

        CodeSection(
            title = "방법 2: AnimatedVisibility + delayMillis",
            code = """
                enter = fadeIn(
                    animationSpec = tween(
                        delayMillis = index * 100
                    )
                )    
            """.trimIndent()
        )

        FeatureSection(
            features = """
                • staggerDelay: 50~100ms가 적당
                • 아이템 수가 많으면 delay 줄이기
                • spring으로 바운스 효과 추가
                • launch로 병렬 애니메이션
            """.trimIndent(),
            type = FeatureTextType.TIP
        )
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun StaggeredListDemoPreview() {
    StaggeredListDemo()
}
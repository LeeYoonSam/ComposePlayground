package com.ys.composeplayground.ui.animation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * 🟡 Intermediate #9: Swipe to Dismiss 애니메이션
 *
 * 📖 핵심 개념
 *
 * Material3의 SwipeToDismissBox를 사용하거나, Animatable + pointerInput으로 직접 구현할 수 있어요. 스와이프 거리에 따라 배경 액션을 보여주고, 임계값을 넘으면 dismiss 처리합니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * SwipeToDismissBox | Material3 스와이프 컴포넌트
 * SwipeToDismissBoxState | 스와이프 상태 관리
 * Animatable | 커스텀 드래그 구현
 * detectHorizontalDragGestures | 수평 드래그 감지
 * animateTo | 스냅/dismiss 애니메이션
 *
 * 💡 동작 원리
 *
 * ```
 * [드래그 시작] offsetX = 0
 *        ↓ detectHorizontalDragGestures
 * [드래그 중] offsetX 업데이트, 배경 노출
 *        ↓ onDragEnd
 * [판정] |offsetX| > threshold?
 *        ↓ Yes: animateTo(fullWidth) → onDismiss
 *        ↓ No: animateTo(0) → 원위치 (spring)
 * ```
 *
 * 학습 목표:
 *  * 1. Material3 SwipeToDismissBox 사용법
 *  * 2. 커스텀 스와이프 구현 (Animatable + pointerInput)
 *  * 3. 배경 액션 표시
 *  * 4. 임계값 기반 dismiss 판정
 */

// ============================================
// 데이터 클래스
// ============================================
data class EmailItem(
    val id: Int,
    val sender: String,
    val subject: String,
    val preview: String,
    val time: String,
    val isRead: Boolean = false
)

data class TodoItem(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
)

// ============================================
// Material3 SwipeToDismissBox 버전
// ============================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissEmailItem(
    email: EmailItem,
    onDelete: () -> Unit,
    onArchive: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    true
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onArchive()
                    true
                }
                SwipeToDismissBoxValue.Settled -> false
            }
        },
        positionalThreshold = { it * 0.4f }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            SwipeBackground(dismissState)
        },
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true
    ) {
        EmailCard(email = email)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackground(
    dismissState: SwipeToDismissBoxState
) {
    val direction = dismissState.dismissDirection

    val color by animateColorAsState(
        targetValue = when (direction) {
            SwipeToDismissBoxValue.StartToEnd -> Color(0xFF4CAF50) // Archive
            SwipeToDismissBoxValue.EndToStart -> Color(0xFFF44336) // Delete
            else -> Color.Transparent
        },
        label = "backgroundColor"
    )

    val icon = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Archive
        SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
        else -> Icons.Default.Delete
    }

    val alignment = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        else -> Alignment.CenterEnd
    }

    val scale by animateFloatAsState(
        targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.8f else 1.2f,
        label = "iconScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .padding(horizontal = 24.dp),
        contentAlignment = alignment
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
fun EmailCard(
    email: EmailItem,
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
            verticalAlignment = Alignment.Top
        ) {
            // 아바타
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = email.sender.first().toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = email.sender,
                        fontWeight = if (email.isRead) FontWeight.Normal else FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = email.time,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = email.subject,
                    fontWeight = if (email.isRead) FontWeight.Normal else FontWeight.Medium,
                    fontSize = 14.sp,
                    maxLines = 1
                )

                Text(
                    text = email.preview,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    maxLines = 1
                )
            }
        }
    }
}

// ============================================
// 커스텀 Swipe 구현
// ============================================
@Composable
fun CustomSwipeToDeleteItem(
    item: TodoItem,
    onDelete: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var cardWidth by remember { mutableFloatStateOf(0f) }
    val threshold = 0.4f

    // 삭제 진행률 (0~1)
    val dismissProgress = (abs(offsetX.value) / cardWidth).coerceIn(0f, 1f)

    // 배경 색상
    val backgroundColor by animateColorAsState(
        targetValue = when {
            offsetX.value > 0 -> Color(0xFF4CAF50).copy(alpha = dismissProgress)
            offsetX.value < 0 -> Color(0xFFF44336).copy(alpha = dismissProgress)
            else -> Color.Transparent
        },
        label = "bgColor"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
    ) {
        // 배경 (액션 표시)
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (offsetX.value > 0) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Complete",
                        tint = Color.White,
                        modifier = Modifier.scale(0.8f + dismissProgress * 0.4f)
                    )
                    Text("완료", color = Color.White, fontWeight = FontWeight.Bold)
                } else if (offsetX.value < 0) {
                    Text("삭제", color = Color.White, fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.scale(0.8f + dismissProgress * 0.4f)
                    )
                }
            }
        }

        // 전경 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    cardWidth = size.width.toFloat()
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                when {
                                    offsetX.value > cardWidth * threshold -> {
                                        // 오른쪽으로 충분히 스와이프 → 완료
                                        offsetX.animateTo(
                                            cardWidth,
                                            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                                        )
                                        onComplete()
                                    }
                                    offsetX.value < -cardWidth * threshold -> {
                                        // 왼쪽으로 충분히 스와이프 → 삭제
                                        offsetX.animateTo(
                                            -cardWidth,
                                            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                                        )
                                        onDelete()
                                    }
                                    else -> {
                                        // 원위치로 복귀
                                        offsetX.animateTo(
                                            0f,
                                            spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessMedium
                                            )
                                        )
                                    }
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = offsetX.value + dragAmount
                                offsetX.snapTo(newOffset)
                            }
                        }
                    )
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.isCompleted,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF4CAF50)
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null,
                    color = if (item.isCompleted) Color.Gray else Color.Black
                )
            }
        }
    }
}

// ============================================
// 단방향 스와이프 (삭제만)
// ============================================
@Composable
fun SimpleSwipeToDelete(
    text: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var cardWidth by remember { mutableFloatStateOf(0f) }

    val progress = (abs(offsetX.value) / cardWidth).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        // 배경
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0xFFF44336)),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(end = 24.dp)
                    .scale(0.8f + progress * 0.4f)
            )
        }

        // 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    cardWidth = size.width.toFloat()
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                if (offsetX.value < -cardWidth * 0.4f) {
                                    offsetX.animateTo(-cardWidth)
                                    onDelete()
                                } else {
                                    offsetX.animateTo(0f, spring())
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                // 왼쪽으로만 스와이프 허용
                                val newOffset = (offsetX.value + dragAmount).coerceAtMost(0f)
                                offsetX.snapTo(newOffset)
                            }
                        }
                    )
                },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp
            )
        }
    }
}

// ============================================
// 스와이프로 액션 버튼 노출
// ============================================
@Composable
fun SwipeToRevealActions(
    title: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val actionWidth = 160f // 액션 버튼 영역 너비

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
    ) {
        // 액션 버튼들
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(with(LocalDensity.current) { actionWidth.toDp() })
                .fillMaxHeight()
        ) {
            // 편집 버튼
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFF9800)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            offsetX.animateTo(0f, spring())
                        }
                        onEdit()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            // 삭제 버튼
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFF44336)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            offsetX.animateTo(0f, spring())
                        }
                        onDelete()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }

        // 메인 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                if (offsetX.value < -actionWidth * 0.5f) {
                                    // 액션 버튼 노출
                                    offsetX.animateTo(-actionWidth, spring())
                                } else {
                                    // 닫기
                                    offsetX.animateTo(0f, spring())
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offsetX.value + dragAmount)
                                    .coerceIn(-actionWidth, 0f)
                                offsetX.snapTo(newOffset)
                            }
                        }
                    )
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = null,
                    tint = Color(0xFF2196F3)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ============================================
// 데모 화면들
// ============================================
@Composable
fun SwipeToDismissDemo() {
    var emails by remember {
        mutableStateOf(
            listOf(
                EmailItem(1, "Google", "보안 알림", "새 기기에서 로그인이 감지되었습니다...", "오전 9:30"),
                EmailItem(2, "GitHub", "Pull Request", "Your PR has been merged...", "오전 10:15", true),
                EmailItem(3, "Slack", "새 메시지", "팀 채널에 새 메시지가 도착했습니다...", "오전 11:00"),
                EmailItem(4, "Netflix", "새로운 콘텐츠", "회원님을 위한 추천 콘텐츠가 있습니다...", "오후 1:30", true),
            )
        )
    }

    var todos by remember {
        mutableStateOf(
            listOf(
                TodoItem(11, "Compose 애니메이션 학습하기"),
                TodoItem(12, "운동 30분", isCompleted = true),
                TodoItem(13, "책 읽기"),
                TodoItem(14, "코드 리뷰하기"),
            )
        )
    }

    var files by remember {
        mutableStateOf(
            listOf("프로젝트 문서", "디자인 에셋", "회의록", "참고 자료")
        )
    }

    var simpleItems by remember {
        mutableStateOf(listOf("Item 1", "Item 2", "Item 3", "Item 4"))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 24.dp)
    ) {
        item {
            Text(
                text = "Swipe to Dismiss",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Material3 SwipeToDismissBox
        item {
            SectionHeader("Material3 SwipeToDismissBox")
            Text(
                "← 삭제 | 보관 →",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(
            items = emails,
            key = { it.id }
        ) { email ->
            SwipeToDismissEmailItem(
                email = email,
                onDelete = {
                    emails = emails.filter { it.id != email.id }
                },
                onArchive = {
                    emails = emails.filter { it.id != email.id }
                }
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // 커스텀 양방향 스와이프
        item {
            SectionHeader("커스텀 양방향 스와이프")
            Text(
                "← 삭제 | 완료 →",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(
            items = todos,
            key = { it.id }
        ) { todo ->
            CustomSwipeToDeleteItem(
                item = todo,
                onDelete = {
                    todos = todos.filter { it.id != todo.id }
                },
                onComplete = {
                    todos = todos.map {
                        if (it.id == todo.id) it.copy(isCompleted = !it.isCompleted)
                        else it
                    }
                }
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // 단방향 스와이프
        item {
            SectionHeader("단방향 스와이프 (삭제만)")
            Text(
                "← 삭제",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(
            items = simpleItems,
            key = { it }
        ) { item ->
            SimpleSwipeToDelete(
                text = item,
                onDelete = {
                    simpleItems = simpleItems.filter { it != item }
                }
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // 액션 버튼 노출
        item {
            SectionHeader("스와이프로 액션 버튼 노출")
            Text(
                "← 스와이프하여 버튼 노출",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(
            items = files,
            key = { it }
        ) { file ->
            SwipeToRevealActions(
                title = file,
                onEdit = { /* 편집 */ },
                onDelete = {
                    files = files.filter { it != file }
                },
                modifier = Modifier.height(56.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // 가이드
        item {
            SwipeGuide()
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = Color.Gray,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SwipeGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TitleSection("📚 Swipe to Dismiss 구현 가이드")

            CodeSection(
                title = "방법 1: Material3 SwipeToDismissBox",
                code = """
                    SwipeToDismissBox(
                        state = rememberSwipeToDismissBoxState(),
                        backgroundContent = { Background() }
                    ) { Content() }    
                """.trimIndent()
            )

            CodeSection(
                title = "방법 2: 커스텀 구현",
                code = """
                    val offsetX = remember { Animatable(0f) }
                
                    Modifier
                        .offset { IntOffset(offsetX.value, 0) }
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = { /* 판정 */ },
                                onHorizontalDrag = { _, drag ->
                                    offsetX.snapTo(offsetX.value + drag)
                                }
                            )
                        }    
                """.trimIndent()
            )

            FeatureSection(
                features = """
                    • threshold: 보통 40% 정도
                    • spring()으로 자연스러운 복귀
                    • 배경 아이콘 scale 애니메이션
                    • coerceIn()으로 스와이프 방향 제한
                """.trimIndent(),
                type = FeatureTextType.TIP
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1400)
@Composable
fun SwipeToDismissDemoPreview() {
    SwipeToDismissDemo()
}
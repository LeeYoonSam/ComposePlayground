package com.ys.composeplayground.ui.animation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 🟢 Beginner #3: 확장/축소 콘텐츠 애니메이션
 *
 * 📖 핵심 개념
 *
 * animateContentSize는 Composable의 콘텐츠 크기가 변경될 때 자동으로 애니메이션을 적용하는 Modifier예요. 별도의 상태 관리 없이 Modifier만 추가하면 됩니다!
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * animateContentSize | 콘텐츠 크기 변화 자동 애니메이션
 * spring | 물리 기반 탄성 애니메이션
 * tween | 시간 기반 애니메이션
 * finishedListener | 애니메이션 완료 콜백
 *
 * 💡 동작 원리
 * ```
 * [접힌 상태] height: 60dp
 *        ↓ animateContentSize (자동 감지!)
 * [펼쳐진 상태] height: 200dp
 *
 * Modifier가 콘텐츠 크기 변화를 감지하고
 * 자동으로 보간 애니메이션 적용
 * ```
 *
 * 학습 목표:
 * 1. animateContentSize 기본 사용법
 * 2. Modifier 순서의 중요성
 * 3. spring vs tween 차이
 * 4. finishedListener 활용
 */

// ============================================
// 기본 버전: 확장 카드
// ============================================
@Composable
fun ExpandableCardBasic(
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            // ✨ 핵심: animateContentSize만 추가하면 끝!
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📦 확장 카드",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (expanded)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle",
                    tint = Color.Gray
                )
            }

            // 확장 시 나타나는 콘텐츠
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "이것은 확장된 콘텐츠입니다. animateContentSize modifier를 " +
                            "사용하면 별도의 애니메이션 코드 없이도 크기 변화가 부드럽게 " +
                            "애니메이션됩니다. 정말 간편하죠?",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

// ============================================
// FAQ 아코디언 스타일
// ============================================
@Composable
fun FAQItem(
    question: String,
    answer: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            // tween 사용 - 일정 속도로 부드럽게
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
    ) {
        // 질문 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = question,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded)
                    Icons.Default.KeyboardArrowUp
                else
                    Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF6200EE)
            )
        }

        // 답변 (확장 시)
        if (expanded) {
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Text(
                text = answer,
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )
        }
    }
}


// ============================================
// 더보기/접기 텍스트
// ============================================
@Composable
fun ExpandableText(
    text: String,
    collapsedMaxLines: Int = 2,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .padding(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            maxLines = if (expanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (expanded) "접기 ▲" else "더보기 ▼",
            fontSize = 13.sp,
            color = Color(0xFF6200EE),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { expanded = !expanded }
        )
    }
}

// ============================================
// 설정 섹션 (중첩 확장)
// ============================================
@Composable
fun SettingsSection(
    title: String,
    items: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
    ) {
        // 섹션 헤더
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = if (expanded) "−" else "+",
                fontSize = 20.sp,
                color = Color(0xFF6200EE)
            )
        }

        // 설정 항목들 (확장 시)
        if (expanded) {
            HorizontalDivider(color = Color(0xFFEEEEEE))
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// ============================================
// finishedListener 활용 예제
// ============================================
@Composable
fun ExpandableCardWithCallback(
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var animationState by remember { mutableStateOf("대기중") }

    Column(modifier = modifier) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(500),
                    finishedListener = { initialSize, targetSize ->
                        // 애니메이션 완료 시 콜백
                        animationState = if (targetSize.height > initialSize.height) {
                            "확장 완료! (${initialSize.height} -> ${targetSize.height})"
                        } else {
                            "축소 완료! (${initialSize.height} -> ${targetSize.height})"
                        }
                    }
                )
                .clickable {
                    animationState = "애니메이션 중..."
                    expanded = !expanded
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📊 애니메이션 콜백 테스트", fontWeight = FontWeight.Bold)

                if (expanded) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "finishedListener를 사용하면 애니메이션이 완료된 시점을 " +
                                "감지할 수 있습니다. 초기 크기와 최종 크기 정보도 제공됩니다.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "상태: $animationState",
            fontSize = 12.sp,
            color = Color(0xFF6200EE),
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun ExpandableContentDemo() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Expandable Content Animation",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        // 기본 확장 카드
        DemoSection(title = "기본 - Spring 바운스") {
            ExpandableCardBasic()
        }

        // FAQ 스타일
        DemoSection(title = "FAQ 아코디언 (tween)") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FAQItem(
                    question = "animateContentSize는 어떻게 작동하나요?",
                    answer = "Modifier가 콘텐츠의 크기 변화를 감지하고, 변경 전후 크기 사이를 " +
                            "자동으로 보간하여 애니메이션합니다."
                )
                FAQItem(
                    question = "언제 사용하면 좋을까요?",
                    answer = "확장/축소 카드, 아코디언 메뉴, 더보기 텍스트 등 콘텐츠 크기가 " +
                            "동적으로 변하는 모든 UI에 적합합니다."
                )
            }
        }

        // 더보기 텍스트
        DemoSection(title = "더보기/접기 텍스트") {
            ExpandableText(
                text = "Jetpack Compose의 animateContentSize는 정말 편리한 Modifier입니다. " +
                        "별도의 애니메이션 상태 관리 없이도 콘텐츠 크기 변화를 부드럽게 " +
                        "애니메이션할 수 있습니다. spring이나 tween 등 다양한 AnimationSpec을 " +
                        "사용하여 원하는 느낌을 만들 수 있고, finishedListener를 통해 " +
                        "애니메이션 완료 시점도 감지할 수 있습니다."
            )
        }

        // 설정 섹션
        DemoSection(title = "설정 메뉴 스타일") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SettingsSection(
                    title = "🔔 알림 설정",
                    items = listOf("푸시 알림", "이메일 알림", "SMS 알림")
                )
                SettingsSection(
                    title = "🔒 보안 설정",
                    items = listOf("비밀번호 변경", "2단계 인증", "로그인 기록")
                )
            }
        }

        // 콜백 예제
        DemoSection(title = "finishedListener 활용") {
            ExpandableCardWithCallback()
        }

        // 가이드
        ModifierOrderGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun ModifierOrderGuide() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        FeatureSection(
            features = """
                ✅ 올바른 순서:
                Modifier
                    .animateContentSize()  // 먼저!
                    .padding(16.dp)
                    .background(Color.White)
                
                ❌ 잘못된 순서:
                Modifier
                    .padding(16.dp)
                    .animateContentSize()  // padding이 애니메이션 안됨!
                
                💡 Tip: animateContentSize는 이후에 오는 
                Modifier들의 크기 변화를 애니메이션합니다.
            """.trimIndent(),
            type = FeatureTextType.CAUTION
        )
    }
}

@Preview(showBackground = true, heightDp = 1400)
@Composable
fun ExpandableContentDemoPreview() {
    ExpandableContentDemo()
}
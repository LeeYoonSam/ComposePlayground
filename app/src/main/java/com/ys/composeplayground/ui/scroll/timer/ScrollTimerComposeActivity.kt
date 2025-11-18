package com.ys.composeplayground.ui.scroll.timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ys.composeplayground.ui.scroll.timer.model.ScrollTimerItem
import com.ys.composeplayground.ui.scroll.timer.model.ViewType
import com.ys.composeplayground.ui.scroll.timer.model.createDummyItems
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

/**
 * LazyColumn을 사용한 스크롤 기반 타이머 Activity
 */
class ScrollTimerComposeActivity : ComponentActivity() {
    private val viewModel: ScrollTimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                ScrollTimerScreen(viewModel = viewModel)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onActivityPaused()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onActivityResumed()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollTimerScreen(
    viewModel: ScrollTimerViewModel = viewModel()
) {
    val context = LocalContext.current
    val timerState by viewModel.timerState.collectAsState()
    val items = remember { createDummyItems() }
    val lazyListState = rememberLazyListState()

    // 완료 메시지 상태
    var showInAppMessage by remember { mutableStateOf(false) }
    var completionMessage by remember { mutableStateOf("") }
    var completionImageUrl by remember { mutableStateOf("") }

    // 타이머 배너 아이템 인덱스 찾기
    val timerBannerIndex = remember(items) {
        items.indexOfFirst { it.viewType == ViewType.TIMER_BANNER }
    }

    // 타이머 배너가 화면에 보이는지 확인
    val isTimerBannerVisible by remember {
        derivedStateOf {
            lazyListState.layoutInfo.visibleItemsInfo.any { it.index == timerBannerIndex }
        }
    }

    // 스크롤 상태 감지
    val isScrollInProgress by remember {
        derivedStateOf { lazyListState.isScrollInProgress }
    }

    // 스크롤 시작/정지 감지
    LaunchedEffect(isScrollInProgress) {
        if (isScrollInProgress) {
            viewModel.onScrollStarted()
        } else {
            viewModel.onScrollStopped()
        }
    }

    // 타이머 완료 이벤트 수신
    LaunchedEffect(Unit) {
        viewModel.completionEvent.collect { event ->
            completionMessage = event.message
            completionImageUrl = event.imageUrl
            showInAppMessage = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("스크롤 타이머 (LazyColumn)") },
                navigationIcon = {
                    IconButton(onClick = { (context as? ComponentActivity)?.finish() }) {
                        Icon(Icons.Default.ArrowBack, "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // 디버그 정보
                DebugInfo(timerState, viewModel)

                // 플로팅 타이머 배너 (디버그 정보 아래 고정)
                androidx.compose.animation.AnimatedVisibility(
                    visible = !isTimerBannerVisible && !timerState.isCompleted,
                    enter = androidx.compose.animation.slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = androidx.compose.animation.core.tween(300)
                    ) + androidx.compose.animation.fadeIn(),
                    exit = androidx.compose.animation.slideOutVertically(
                        targetOffsetY = { -it },
                        animationSpec = androidx.compose.animation.core.tween(300)
                    ) + androidx.compose.animation.fadeOut()
                ) {
                    TimerBannerView(timerState = timerState)
                }

                // 스크롤 가능한 콘텐츠
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                itemsIndexed(items) { _, item ->
                    when (item.viewType) {
                        ViewType.HEADER -> HeaderItem(item.data as? String)
                        ViewType.IMAGE_GALLERY -> ImageGalleryItem(item.data as? List<String>)
                        ViewType.BADGE -> BadgeItem(item.data as? List<*>)
                        ViewType.DESCRIPTION -> DescriptionItem(item.data as? String)
                        ViewType.TIMER_BANNER -> {
                            if (!timerState.isCompleted) {
                                TimerBannerView(timerState)
                            }
                            // 완료되면 아무것도 렌더링하지 않음
                        }
                        ViewType.PRODUCT_SPEC -> ProductSpecItem(item.data as? Map<*, *>)
                        ViewType.DIVIDER -> DividerItem(item.data as? String)
                        ViewType.REVIEW -> ReviewItem(item.data as? String)
                        ViewType.VIDEO_PLAYER -> VideoPlayerItem(item.data as? String)
                        ViewType.RELATED_PRODUCT -> RelatedProductItem(item.data as? List<*>)
                    }
                }
            }
            }

            // 타이머 완료 인앱 메시지
            if (showInAppMessage) {
                ToastStyleInAppMessage(
                    message = completionMessage,
                    imageUrl = completionImageUrl,
                    onDismiss = { showInAppMessage = false }
                )
            }
        }
    }
}

@Composable
fun DebugInfo(
    timerState: com.ys.composeplayground.ui.scroll.timer.model.TimerState,
    viewModel: ScrollTimerViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "디버그 정보",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("남은 시간: ${timerState.remainingSeconds}초", fontSize = 12.sp)
            Text("활성화: ${timerState.isActive}", fontSize = 12.sp)
            Text("카운트다운 중: ${timerState.isCountingDown}", fontSize = 12.sp)
            Text("완료: ${timerState.isCompleted}", fontSize = 12.sp)

            Row(modifier = Modifier.padding(top = 8.dp)) {
                Button(
                    onClick = { viewModel.resetTimer() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("리셋", fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun HeaderItem(title: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = title ?: "작품 제목",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "₩ 50,000",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFFF6B6B)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageGalleryItem(images: List<String>?) {
    val imageUrls = images ?: listOf(
        "https://picsum.photos/400/300?random=1",
        "https://picsum.photos/400/300?random=2",
        "https://picsum.photos/400/300?random=3"
    )

    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = imageUrls[page],
                contentDescription = "상품 이미지 ${page + 1}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Indicator
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            repeat(imageUrls.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(
                            color = if (pagerState.currentPage == index) Color.White else Color.White.copy(
                                alpha = 0.5f
                            ),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .width(if (pagerState.currentPage == index) 24.dp else 8.dp)
                        .height(8.dp)
                )
            }
        }
    }
}

@Composable
fun DescriptionItem(description: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "상품 설명",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description ?: "상품 설명이 없습니다.",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ReviewItem(review: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF9C4)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = review ?: "후기 내용이 없습니다.",
                fontSize = 13.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun BadgeItem(badges: List<*>?) {
    @Suppress("UNCHECKED_CAST")
    val badgeList = (badges as? List<String>) ?: emptyList()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        badgeList.forEach { badge ->
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFFF6B6B),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = badge,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProductSpecItem(specs: Map<*, *>?) {
    @Suppress("UNCHECKED_CAST")
    val specMap = (specs as? Map<String, String>) ?: emptyMap()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "상품 스펙",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            specMap.forEach { (key, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = key,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = value,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(2f)
                    )
                }
            }
        }
    }
}

@Composable
fun DividerItem(title: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFFF5F5F5))
        )
        if (title != null) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun VideoPlayerItem(title: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(20.dp)
                ) {
                    Text(
                        text = "▶",
                        fontSize = 32.sp,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title ?: "동영상",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun RelatedProductItem(products: List<*>?) {
    @Suppress("UNCHECKED_CAST")
    val productList = (products as? List<Pair<String, String>>) ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "함께 보면 좋은 상품",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            productList.forEach { (name, price) ->
                Card(
                    modifier = Modifier.weight(1f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .background(Color(0xFFF5F5F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📦", fontSize = 32.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = name,
                            fontSize = 12.sp,
                            maxLines = 2
                        )
                        Text(
                            text = price,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6B6B)
                        )
                    }
                }
            }
        }
    }
}

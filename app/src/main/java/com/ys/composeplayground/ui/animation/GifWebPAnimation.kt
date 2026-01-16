package com.ys.composeplayground.ui.animation

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.util.DebugLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 📱 실무 애니메이션 #3: GIF/WebP 애니메이션
 *
 * 📖 핵심 개념
 *
 * GIF와 WebP는 이미지 포맷 자체에 애니메이션이 포함된 형식입니다.
 * Compose에서는 Coil 라이브러리를 통해 간단하게 로드하고 재생할 수 있습니다.
 * Lottie보다 파일 크기가 클 수 있지만, 디자이너가 별도 도구 없이 만들 수 있어 실무에서 자주 사용됩니다.
 *
 * 🎯 학습 포인트
 *
 * API | 역할
 * --- | ---
 * AsyncImage | 기본 비동기 이미지 로드
 * SubcomposeAsyncImage | 로딩/에러 상태 커스터마이징
 * rememberAsyncImagePainter | 세밀한 로드 상태 제어
 * ImageRequest | 이미지 요청 설정
 * ImageLoader | GIF/WebP 디코더 설정
 *
 * 💡 동작 원리
 * ```
 * [ImageRequest] 이미지 URL/리소스 설정
 *       ↓
 * [ImageLoader] GIF 디코더로 디코딩
 *       ↓
 * [AsyncImage] 애니메이션 프레임 자동 재생
 * ```
 *
 * 학습 목표:
 * 1. Coil로 GIF/WebP 로드
 * 2. 로딩/에러 상태 처리
 * 3. 다양한 사용 패턴 (리스트, 그리드)
 * 4. 성능 최적화 고려사항
 */

// ============================================
// GIF 디코더가 포함된 ImageLoader 생성
// 실제 앱에서는 Application 클래스에서 설정 권장
// ============================================
@Composable
private fun rememberGifImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember {
        ImageLoader.Builder(context)
            .logger(DebugLogger())
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .crossfade(true)
            .build()
    }
}

// ============================================
// 샘플 GIF URL (실제 프로젝트에서는 res/raw/ 사용 권장)
// ============================================
private object SampleGifs {
    // 무료 GIF 샘플 URL (GIPHY 등에서 가져온 예시)
    val loading = "https://media.giphy.com/media/3oEjI6SIIHBdRxXI40/giphy.gif"
    val success = "https://media.giphy.com/media/XreQmk7ETCak0/giphy.gif"
    val cat = "https://media.giphy.com/media/JIX9t2j0ZTN9S/giphy.gif"
    val coding = "https://media.giphy.com/media/ZVik7pBtu9dNS/giphy.gif"
    val rocket = "https://media.giphy.com/media/mi6DsSSNKDbUY/giphy.gif"
    val heart = "https://media.giphy.com/media/l4Ki2obCyAQS5WhFe/giphy.gif"

    val allGifs = listOf(loading, success, cat, coding, rocket, heart)
}

// ============================================
// 1. 기본 GIF 로드 (AsyncImage)
// ============================================
@Composable
fun BasicGifAnimation(modifier: Modifier = Modifier) {
    val imageLoader = rememberGifImageLoader()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "AsyncImage로 GIF 로드",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 가장 간단한 방법
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(SampleGifs.cat)
                .crossfade(true)
                .build(),
            contentDescription = "고양이 GIF",
            imageLoader = imageLoader,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "GIF는 자동으로 반복 재생됩니다",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp
        )
    }
}

// ============================================
// 2. 로딩/에러 상태 처리 (SubcomposeAsyncImage)
// ============================================
@Composable
fun GifWithLoadingState(modifier: Modifier = Modifier) {
    val imageLoader = rememberGifImageLoader()
    var currentUrl by remember { mutableStateOf(SampleGifs.rocket) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2D3436))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1E272E)),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(if (showError) "invalid_url" else currentUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "로딩 상태 GIF",
                imageLoader = imageLoader,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        // 로딩 중
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF74B9FF),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "로딩 중...",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                        }
                    }
                    is AsyncImagePainter.State.Error -> {
                        // 에러 발생
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color(0xFFFF6B6B),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "로드 실패",
                                color = Color(0xFFFF6B6B),
                                fontSize = 12.sp
                            )
                        }
                    }
                    is AsyncImagePainter.State.Success -> {
                        // 성공
                        SubcomposeAsyncImageContent()
                    }
                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    showError = false
                    currentUrl = SampleGifs.allGifs.random()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00B894)
                )
            ) {
                Text("다른 GIF")
            }

            Button(
                onClick = { showError = !showError },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showError) Color(0xFF74B9FF) else Color(0xFFFF6B6B)
                )
            ) {
                Text(if (showError) "복구" else "에러 테스트")
            }
        }
    }
}

// ============================================
// 3. Placeholder와 Error 이미지 설정
// ============================================
@Composable
fun GifWithPlaceholder(modifier: Modifier = Modifier) {
    val imageLoader = rememberGifImageLoader()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F0F23))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Placeholder & Error 이미지",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 성공 케이스
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(SampleGifs.heart)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    imageLoader = imageLoader,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2D3436)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "정상 로드",
                    color = Color(0xFF00B894),
                    fontSize = 10.sp
                )
            }

            // 에러 케이스 - SubcomposeAsyncImage 사용
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("invalid_url_test")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    imageLoader = imageLoader,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF2D3436)),
                    contentScale = ContentScale.Crop,
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF2D3436)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color(0xFFFF6B6B),
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Error",
                                    color = Color(0xFFFF6B6B),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "에러 발생",
                    color = Color(0xFFFF6B6B),
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "💡 커스텀 에러 UI는 SubcomposeAsyncImage 사용",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp
        )
    }
}

// ============================================
// 4. GIF 갤러리 (LazyRow)
// ============================================
@Composable
fun GifGallery(modifier: Modifier = Modifier) {
    val imageLoader = rememberGifImageLoader()
    var selectedIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E272E))
            .padding(24.dp)
    ) {
        Text(
            text = "GIF 갤러리",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 선택된 GIF (크게 표시)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0F0F23)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(SampleGifs.allGifs[selectedIndex])
                    .crossfade(true)
                    .build(),
                contentDescription = "선택된 GIF",
                imageLoader = imageLoader,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 썸네일 리스트
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(SampleGifs.allGifs.size) { index ->
                val isSelected = index == selectedIndex
                val alpha by animateFloatAsState(
                    targetValue = if (isSelected) 1f else 0.5f,
                    label = "thumbnailAlpha"
                )

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .alpha(alpha)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = if (isSelected) 2.dp else 0.dp,
                            color = if (isSelected) Color(0xFF74B9FF) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedIndex = index }
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(SampleGifs.allGifs[index])
                            .crossfade(true)
                            .size(120) // 썸네일 크기 제한
                            .build(),
                        contentDescription = "썸네일 $index",
                        imageLoader = imageLoader,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

// ============================================
// 5. GIF 그리드 (LazyVerticalGrid)
// ============================================
@Composable
fun GifGrid(modifier: Modifier = Modifier) {
    val imageLoader = rememberGifImageLoader()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2C3E50))
            .padding(16.dp)
    ) {
        Text(
            text = "GIF 그리드",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(240.dp)
        ) {
            items(SampleGifs.allGifs) { gifUrl ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(gifUrl)
                        .crossfade(true)
                        .size(200) // 메모리 최적화
                        .build(),
                    contentDescription = null,
                    imageLoader = imageLoader,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "💡 그리드에서는 size()로 메모리 최적화 권장",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp
        )
    }
}

// ============================================
// 6. 로딩 인디케이터로 GIF 사용
// ============================================
@Composable
fun GifAsLoadingIndicator(modifier: Modifier = Modifier) {
    val imageLoader = rememberGifImageLoader()
    var isLoading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GIF를 로딩 인디케이터로 사용",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF2D3436)),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(SampleGifs.loading)
                                .crossfade(true)
                                .build(),
                            contentDescription = "로딩 중",
                            imageLoader = imageLoader,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "데이터 로딩 중...",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }
                isSuccess -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF00B894),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "로딩 완료!",
                            color = Color(0xFF00B894),
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    Text(
                        text = "버튼을 눌러 로딩 시작",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    isSuccess = false
                    isLoading = true
                    scope.launch {
                        delay(3000)
                        isLoading = false
                        isSuccess = true
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C5CE7)
                )
            ) {
                Text(if (isLoading) "로딩 중..." else "데이터 로드")
            }

            if (isSuccess) {
                Button(
                    onClick = {
                        isSuccess = false
                        isLoading = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF636E72)
                    )
                ) {
                    Text("리셋")
                }
            }
        }
    }
}

// ============================================
// 7. 클릭으로 GIF 전환
// ============================================
@Composable
fun ClickableGifSwitch(modifier: Modifier = Modifier) {
    val imageLoader = rememberGifImageLoader()
    var currentGifIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2D3436))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "클릭하여 GIF 변경",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    currentGifIndex = (currentGifIndex + 1) % SampleGifs.allGifs.size
                },
            contentAlignment = Alignment.Center
        ) {
            // key를 사용해 GIF 변경 시 완전히 새로 로드
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(SampleGifs.allGifs[currentGifIndex])
                    .crossfade(true)
                    .build(),
                contentDescription = "클릭 가능한 GIF",
                imageLoader = imageLoader,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "${currentGifIndex + 1} / ${SampleGifs.allGifs.size}",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp
        )
    }
}

// ============================================
// 8. 이모티콘/리액션 스타일
// ============================================
@Composable
fun GifReactionBar(modifier: Modifier = Modifier) {
    val imageLoader = rememberGifImageLoader()
    var selectedReaction by remember { mutableIntStateOf(-1) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E272E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "리액션 선택",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SampleGifs.allGifs.take(4).forEachIndexed { index, gifUrl ->
                val isSelected = selectedReaction == index
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.2f else 1f,
                    label = "reactionScale"
                )

                Box(
                    modifier = Modifier
                        .size((50 * scale).dp)
                        .clip(CircleShape)
                        .border(
                            width = if (isSelected) 2.dp else 0.dp,
                            color = if (isSelected) Color(0xFFFFE66D) else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable {
                            selectedReaction = if (selectedReaction == index) -1 else index
                        }
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(gifUrl)
                            .crossfade(true)
                            .size(100)
                            .build(),
                        contentDescription = "리액션 $index",
                        imageLoader = imageLoader,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(
            visible = selectedReaction >= 0,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "리액션 ${selectedReaction + 1} 선택됨!",
                color = Color(0xFFFFE66D),
                fontSize = 12.sp
            )
        }
    }
}

// ============================================
// 데모 화면
// ============================================
@Composable
fun GifWebPAnimationDemo() {
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
            text = "GIF/WebP 애니메이션",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        DemoSection(title = "1. 기본 GIF 로드") {
            BasicGifAnimation()
        }

        DemoSection(title = "2. 로딩/에러 상태 처리") {
            GifWithLoadingState()
        }

        DemoSection(title = "3. Placeholder & Error") {
            GifWithPlaceholder()
        }

        DemoSection(title = "4. GIF 갤러리") {
            GifGallery()
        }

        DemoSection(title = "5. GIF 그리드") {
            GifGrid()
        }

        DemoSection(title = "6. 로딩 인디케이터") {
            GifAsLoadingIndicator()
        }

        DemoSection(title = "7. 클릭으로 전환") {
            ClickableGifSwitch()
        }

        DemoSection(title = "8. 리액션 바") {
            GifReactionBar()
        }

        GifWebPGuide()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun GifWebPGuide() {
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
                text = "📚 GIF/WebP 애니메이션 가이드",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = """
                    핵심 API:
                    
                    • AsyncImage
                      가장 간단한 비동기 이미지 로드
                    
                    • SubcomposeAsyncImage
                      로딩/에러/성공 상태 커스터마이징
                    
                    • rememberAsyncImagePainter
                      더 세밀한 상태 제어 필요 시
                    
                    • ImageRequest.Builder
                      - data(): 이미지 소스
                      - crossfade(): 페이드 효과
                      - size(): 로드 크기 제한
                    
                    💡 실무 팁:
                    • res/raw/에 저장 권장 (네트워크 의존 X)
                    • 리스트에서는 size() 필수
                    • GIF보다 WebP가 용량 작음
                    • 복잡한 애니메이션은 Lottie 권장
                    
                    📊 포맷 선택:
                    • GIF: 호환성 최고, 256색 제한
                    • WebP: 품질+용량 균형
                    • Lottie: 벡터, 용량 최소
                """.trimIndent(),
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 2800)
@Composable
private fun GifWebPAnimationDemoPreview() {
    GifWebPAnimationDemo()
}
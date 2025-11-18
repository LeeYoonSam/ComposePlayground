package com.ys.composeplayground.ui.scroll.timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

/**
 * 토스트 스타일의 인앱 메시지
 * 상단에서 슬라이드 인되어 3초간 표시 후 자동으로 사라진다
 *
 * @param message 표시할 메시지
 * @param imageUrl 표시할 이미지 URL
 * @param onDismiss 메시지가 사라질 때 호출되는 콜백
 */
@Composable
fun ToastStyleInAppMessage(
    message: String,
    imageUrl: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }

    // 애니메이션 시작 및 3초 후 자동 사라짐
    LaunchedEffect(Unit) {
        visible = true
        delay(3000) // 3초 표시
        visible = false
        delay(300) // 애니메이션 완료 대기
        onDismiss()
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { -it }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { -it }
            ) + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF4CAF50))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 이미지
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "완료 이미지",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                // 메시지
                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

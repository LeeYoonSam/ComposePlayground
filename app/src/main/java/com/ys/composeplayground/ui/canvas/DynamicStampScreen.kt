package com.ys.composeplayground.ui.canvas

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.R
import com.ys.composeplayground.extensions.toast
import kotlin.math.sqrt

// 스탬프 상태 정의
enum class StampState {
    ON,      // 체크된 상태
    OFF,     // 체크되지 않은 상태 (클릭 가능)
    DISABLED // 비활성화된 상태
}

// 스탬프 데이터 클래스
data class StampData(
    val id: String,
    val label: String,
    val relativeX: Float, // 0.0 ~ 1.0 (스탬프 판 대비 상대적 위치)
    val relativeY: Float, // 0.0 ~ 1.0 (스탬프 판 대비 상대적 위치)
    val state: StampState,
    val navigationLink: String? = null
)

private const val STAMP_SIZE = 88f
private const val STAMP_STAGE_WIDTH = 358f
private const val STAMP_STAGE_HEIGHT = 384f

@Composable
fun DynamicStampScreen() {
    val context = LocalContext.current
    
    // Transform state for zoom/pan
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(0.5f, 3f)
        offset += offsetChange
    }
    
    // 스탬프 이미지 리소스
    val stampOnPainter = painterResource(id = R.drawable.stamp_artist_home_on)
    val stampOffPainter = painterResource(id = R.drawable.stamp_artist_home_off)
    val stampDisabledPainter = painterResource(id = R.drawable.stamp_artist_disable)
    val stampBoardPainter = painterResource(id = R.drawable.stamp_grape)
    
    // 실제 이미지 크기: 포도 716x768, 스탬프 177x177
    // 디자인 기준 좌표 (왼쪽 모서리 기준)를 중심점 기준으로 변환
    // 실제 스탬프 판: 358x384, 스탬프: 88x88
    // 여백: left 9, right 12.97, bottom 18.01
    val stampDataList = remember {
        listOf(
            // 0: (15, 30) -> 여백 고려: (15+9, 30) = (24, 30) -> 중심점: (68, 74)
            StampData(
                id = "stamp_0",
                label = "작가활동문",
                relativeX = 68f / STAMP_STAGE_WIDTH,  // ≈ 0.190
                relativeY = 74f / STAMP_STAGE_HEIGHT,  // ≈ 0.193
                state = StampState.ON // 원래 상태로 복원
            ),
            // 1: (88, 60) -> 여백 고려: (88+9, 60) = (97, 60) -> 중심점: (141, 104)
            StampData(
                id = "stamp_1",
                label = "작가활동문 진행",
                relativeX = 141f / STAMP_STAGE_WIDTH, // ≈ 0.394
                relativeY = 104f / STAMP_STAGE_HEIGHT, // ≈ 0.271
                state = StampState.OFF // 원래 상태로 복원
            ),
            // 2: (162, 90) -> 여백 고려: (162+9, 90) = (171, 90) -> 중심점: (215, 134)
            StampData(
                id = "stamp_2",
                label = "작품구매",
                relativeX = 215f / STAMP_STAGE_WIDTH, // ≈ 0.601
                relativeY = 134f / STAMP_STAGE_HEIGHT, // ≈ 0.349
                state = StampState.OFF // 원래 상태로 복원
            ),
            // 3: 안전한 위치에서 DISABLED 상태로 복원
            StampData(
                id = "stamp_3",
                label = "작품구매 진행",
                relativeX = 286f / STAMP_STAGE_WIDTH, // ≈ 0.698 (안전한 위치 유지)
                relativeY = 170f / STAMP_STAGE_HEIGHT, // ≈ 0.443
                state = StampState.ON // 원래 상태로 복원
            ),
            // 4: (165, 168) -> 여백 고려: (165+9, 168) = (174, 168) -> 중심점: (218, 212)
            StampData(
                id = "stamp_4",
                label = "피드댓글",
                relativeX = 218f / STAMP_STAGE_WIDTH, // ≈ 0.609
                relativeY = 212f / STAMP_STAGE_HEIGHT, // ≈ 0.552
                state = StampState.ON // 원래 상태로 복원
            ),
            // 5: (90, 141) -> 여백 고려: (90+9, 141) = (99, 141) -> 중심점: (143, 185)
            StampData(
                id = "stamp_5",
                label = "피드댓글 진행",
                relativeX = 143f / STAMP_STAGE_WIDTH, // ≈ 0.400
                relativeY = 185f / STAMP_STAGE_HEIGHT, // ≈ 0.482
                state = StampState.DISABLED // 원래 상태로 복원
            ),
            // 6: (20, 104) -> 여백 고려: (20+9, 104) = (29, 104) -> 중심점: (73, 148)
            StampData(
                id = "stamp_6",
                label = "작품집",
                relativeX = 73f / STAMP_STAGE_WIDTH,  // ≈ 0.204
                relativeY = 148f / STAMP_STAGE_HEIGHT, // ≈ 0.385
                state = StampState.ON // 원래 상태로 복원
            ),
            // 7: (28, 191) -> 여백 고려: (28+9, 191) = (37, 191) -> 중심점: (81, 235)
            StampData(
                id = "stamp_7",
                label = "작품집 진행",
                relativeX = 81f / STAMP_STAGE_WIDTH,  // ≈ 0.226
                relativeY = 235f / STAMP_STAGE_HEIGHT, // ≈ 0.612
                state = StampState.OFF // 원래 상태로 복원
            ),
            // 8: (108, 223) -> 여백 고려: (108+9, 223) = (117, 223) -> 중심점: (161, 267)
            StampData(
                id = "stamp_8",
                label = "후기작성",
                relativeX = 161f / STAMP_STAGE_WIDTH, // ≈ 0.450
                relativeY = 267f / STAMP_STAGE_HEIGHT, // ≈ 0.695
                state = StampState.ON // 원래 상태로 복원
            ),
            // 9: (39, 267) -> 여백 고려: (39+9, 267) = (48, 267) -> 중심점: (92, 311)
            StampData(
                id = "stamp_9",
                label = "후기작성 진행",
                relativeX = 92f / STAMP_STAGE_WIDTH,  // ≈ 0.257
                relativeY = 311f / STAMP_STAGE_HEIGHT, // ≈ 0.810
                state = StampState.OFF // 원래 상태로 복원
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp) // 기본 여백을 더 크게 설정
                .transformable(state = transformableState)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        ) {
            val screenWidth = maxWidth
            val screenHeight = maxHeight
            
            // 포도 이미지의 실제 비율 (716:768)
            val grapeAspectRatio = 716f / 768f
            
            // 스탬프 크기를 먼저 계산 (나중에 여백 계산에 사용)
            val baseStampSize = minOf(screenWidth, screenHeight) * 0.08f // 기본 스탬프 크기
            
            // 스탬프가 잘리지 않도록 추가 여백 확보
            val stampMargin = baseStampSize
            val availableWidth = screenWidth - stampMargin
            val availableHeight = screenHeight - stampMargin
            
            // 화면에 맞는 포도 이미지 크기 계산 (여백 고려)
            val grapeWidth = if (availableWidth / availableHeight < grapeAspectRatio) {
                availableWidth
            } else {
                availableHeight * grapeAspectRatio
            }
            val grapeHeight = grapeWidth / grapeAspectRatio
            
            // 스탬프 크기를 디자인 기준 비율로 계산 (88/358 ≈ 0.246)
            val stampSize = grapeWidth * STAMP_SIZE / STAMP_STAGE_WIDTH // 디자인 기준 비율 적용
            
            Box(
                modifier = Modifier
                    .size(grapeWidth, grapeHeight)
                    .align(Alignment.Center)
                                        .pointerInput(grapeWidth, grapeHeight, stampSize, scale, offset) {
                        detectTapGestures { tapOffset ->
                            Log.d("StampClick", "Raw tap at: (${tapOffset.x}, ${tapOffset.y})")
                            Log.d("StampClick", "Scale: $scale, Offset: (${offset.x}, ${offset.y})")
                            Log.d("StampClick", "Board size: ${grapeWidth.value} x ${grapeHeight.value}")
                            Log.d("StampClick", "Stamp size: ${stampSize.value}")
                            
                            // pointerInput이 적용된 Box의 크기가 grapeWidth x grapeHeight이므로
                            // tapOffset은 이미 보드 내부 좌표계입니다.
                            // scale과 offset만 고려하면 됩니다.
                            
                            // 클릭된 위치에서 스탬프 찾기
                            val clickedStamp = findStampAtPosition(
                                tapOffset = tapOffset,
                                stampDataList = stampDataList,
                                boardWidth = grapeWidth.value,
                                boardHeight = grapeHeight.value,
                                stampSize = stampSize.value
                            )
                            
                            clickedStamp?.let { stamp ->
                                if (stamp.state == StampState.OFF) {
                                    context.toast("${stamp.label} 스탬프가 클릭되었습니다!")
                                }
                            } ?: run {
                                Log.d("StampClick", "No stamp found at clicked position")
                            }
                        }
                    }
            ) {
                // 스탬프 판 배경 이미지
                Image(
                    painter = stampBoardPainter,
                    contentDescription = "스탬프 판",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                
                // 스탬프들 배치
                stampDataList.forEach { stampData ->
                    val (painter, alpha) = when (stampData.state) {
                        StampState.ON -> Pair(stampOnPainter, 1.0f)
                        StampState.OFF -> Pair(stampOffPainter, 1.0f)
                        StampState.DISABLED -> Pair(stampDisabledPainter, 1.0f)
                    }
                    
                    Image(
                        painter = painter,
                        contentDescription = stampData.label,
                        modifier = Modifier
                            .size(stampSize)
                            .alpha(alpha)
                            .offset(
                                x = grapeWidth * stampData.relativeX - stampSize / 2,
                                y = grapeHeight * stampData.relativeY - stampSize / 2
                            )
                    )
                }
            }
        }
    }
}

// 클릭된 위치에서 스탬프 찾기
private fun findStampAtPosition(
    tapOffset: Offset,
    stampDataList: List<StampData>,
    boardWidth: Float,
    boardHeight: Float,
    stampSize: Float
): StampData? {
    // 클릭 범위를 스탬프 크기에 맞춤 (스탬프 반지름)
    val clickRadius = stampSize / 2f
    
    Log.d("StampClick", "Click radius: $clickRadius")
    
    return stampDataList.find { stampData ->
        // 스탬프 실제 중심점 계산 (이미지 배치와 동일한 방식)
        val stampCenterX = boardWidth * stampData.relativeX
        val stampCenterY = boardHeight * stampData.relativeY
        
        val distance = sqrt(
            (tapOffset.x - stampCenterX) * (tapOffset.x - stampCenterX) + 
            (tapOffset.y - stampCenterY) * (tapOffset.y - stampCenterY)
        )
        
        Log.d("StampClick", 
            "Stamp ${stampData.id} (${stampData.label}): " +
            "center(${stampCenterX.toInt()}, ${stampCenterY.toInt()}), " +
            "distance: ${distance.toInt()}, " +
            "clickRadius: ${clickRadius.toInt()}, " +
            "state: ${stampData.state}"
        )
        
        val isWithinRange = distance <= clickRadius
        if (isWithinRange) {
            Log.d("StampClick", "Found stamp: ${stampData.label}")
        }
        
        isWithinRange
    }
}

@Preview(showBackground = true)
@Composable
fun DynamicStampScreenPreview() {
    MaterialTheme {
        DynamicStampScreen()
    }
}
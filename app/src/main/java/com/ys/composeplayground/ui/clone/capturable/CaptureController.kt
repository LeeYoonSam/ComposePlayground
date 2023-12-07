package com.ys.composeplayground.ui.clone.capturable

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * [Composable] 콘텐츠 캡처용 컨트롤러
 */
class CaptureController {
    /**
     * 캡처 요청을 제공하기 위한 매체
     */
    private val _captureRequests = MutableSharedFlow<Bitmap.Config>(extraBufferCapacity = 1)
    internal val captureRequests = _captureRequests.asSharedFlow()

    /**
     * 지정된 [config]로 비트맵 캡처 요청을 생성하고 전송합니다.
     *
     * 이 메서드는 콜백 함수의 일부로 호출해야 하며, [Composable] 함수 자체의 일부가 아닌 [Composable] 함수 자체의 일부로 호출해야 합니다.
     *
     * @param config 원하는 비트맵의 비트맵 구성, 기본값은 [Bitmap.Config.ARGB_8888]
     */
    fun capture(config: Bitmap.Config = Bitmap.Config.ARGB_8888) {
        _captureRequests.tryEmit(config)
    }
}

/**
 * [CaptureController]를 생성하고 기억
 */
@Composable
fun rememberCaptureController(): CaptureController {
    return remember { CaptureController() }
}
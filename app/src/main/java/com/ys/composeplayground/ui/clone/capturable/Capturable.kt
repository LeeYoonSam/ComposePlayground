package com.ys.composeplayground.ui.clone.capturable

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnLayout
import androidx.core.view.drawToBitmap
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * [content]에서 [ImageBitmap]을 캡처할 수 있도록 지원하는 [content]와 컴포지션할 수 있습니다.
 * @param controller [content]를 캡처할 수 있는 제어를 제공하는 [CaptureController].
 * @param modifier 레이아웃에 적용할 수정자입니다.
 * @param onCaptured 컴포저블이 캡처된 후 [ImageBitmap]을 반환하는 콜백입니다.
 * @param content 캡처할 컴포저블 콘텐츠입니다.
 *
 * Example usage:
 *
 * ```
 *  val captureController = rememberCaptureController()
 *  Capturable(
 *      controller = captureController,
 *      onCaptured = { bitmap ->
 *          // Do something with [bitmap]
 *      }
 *  ) {
 *      // Composable content
 *  }
 *
 *  Button(onClick = {
 *      // Capture content
 *      captureController.capture()
 *  }) { ... }
 * ```
 */
@Composable
fun Capturable(
    controller: CaptureController,
    modifier: Modifier = Modifier,
    onCaptured: (ImageBitmap) -> Unit,
    content: @Composable () -> Unit,
) {
    AndroidView(
        factory = { ComposeView(it).applyCapturability(controller, onCaptured, content) },
        modifier = modifier,
    )
}

/**
 * [ComposeView]에서 [content]를 설정하고 [content]의 캡처를 처리합니다.
 */
private inline fun ComposeView.applyCapturability(
    controller: CaptureController,
    noinline onCaptured: (ImageBitmap) -> Unit,
    crossinline content: @Composable () -> Unit,
) = apply {
    setContent {
        content()
        LaunchedEffect(controller, onCaptured) {
            controller.captureRequests
                .mapNotNull { config -> drawToBitmapPostLaidOut(config) }
                .onEach { bitmap -> onCaptured(bitmap.asImageBitmap()) }
                .catch { Log.e("Capturable", "Failed to capture composable", it) }
                .launchIn(this)
        }
    }
}

/**
 * 이 [View]가 해제될 때까지 기다린 다음 지정된 [config]을 사용하여 [Bitmap]에 그립니다.
 */
private suspend fun View.drawToBitmapPostLaidOut(config: Bitmap.Config): Bitmap {
    return suspendCoroutine { continuation ->
        doOnLayout { view ->
            continuation.resume(view.drawToBitmap(config))
        }
    }
}
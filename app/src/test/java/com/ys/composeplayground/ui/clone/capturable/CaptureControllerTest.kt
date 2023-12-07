package com.ys.composeplayground.ui.clone.capturable

import android.graphics.Bitmap
import androidx.compose.runtime.ExperimentalComposeApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CaptureControllerTest {

    private val controller = CaptureController()

    @Test
    fun testCapture_withNoParameters() = runTest {
        // 캡처하기 전에 flow 요청을 수집
        val captureRequestDeferred = asyncOnUnconfinedDispatcher { getRecentCaptureRequest() }

        // When: Captured
        controller.capture()

        val actualConfig = captureRequestDeferred.await()
        val expectedConfig = Bitmap.Config.ARGB_8888

        // Then: 캡처 요청은 기본 비트맵 구성으로 전송되어야 합니다.
        assertEquals(actualConfig, expectedConfig)
    }

    @Test
    fun testCapture_withCustomParameters() = runTest {
        // 캡처하기 전에 flow 요청을 수집
        val captureRequestDeferred = asyncOnUnconfinedDispatcher { getRecentCaptureRequest() }

        // Given: The customized config
        val expectedConfig = Bitmap.Config.RGB_565

        // When: Captured
        controller.capture(expectedConfig)

        val actualConfig = captureRequestDeferred.await()

        // Then: customized config 와 동일한 config 를 받아야 합니다.
        assertEquals(actualConfig, expectedConfig)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T> TestScope.asyncOnUnconfinedDispatcher(block: suspend CoroutineScope.() -> T) =
        async(UnconfinedTestDispatcher(testScheduler), block = block)

    private suspend fun getRecentCaptureRequest() = controller.captureRequests.take(1).first()
}
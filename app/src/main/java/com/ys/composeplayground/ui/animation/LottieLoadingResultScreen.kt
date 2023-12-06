package com.ys.composeplayground.ui.animation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ys.composeplayground.R

/**
 * Reference
 * https://proandroiddev.com/lottie-animation-and-jetpack-compose-b0a2c92d74e5
 */
@Composable
fun LottieLoadingResultScreen() {
    var isSuccess by remember { mutableStateOf(false) }
    var isFailed by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)
    ) {
        ComposeLottieAnimation(
            modifier = Modifier.align(alignment = Alignment.Center),
            isSuccess = isSuccess,
            isFailed = isFailed
        )

        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            // Successful button.
            Button(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(bottom = 15.dp),
                onClick = {
                    isSuccess = true
                    isFailed = false
                }
            ) {
                Text(text = "Successful")
            }

            // Failure button.
            Button(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(bottom = 15.dp),
                onClick = {
                    isSuccess = false
                    isFailed = true
                }
            ) {
                Text(text = "failure")
            }

            // Restart button.
            Button(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(bottom = 45.dp),
                onClick = {
                    isSuccess = false
                    isFailed = false
                }
            ) {
                Text(text = "Restart")
            }
        }
    }
}

@Composable
fun ComposeLottieAnimation(modifier: Modifier, isSuccess: Boolean, isFailed: Boolean) {
    val clipSpecs = LottieClipSpec.Progress(
        min = if (isFailed) 0.5f else 0.0f,
        max = if (isSuccess) 0.45f else 0.95f,
    )

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.anim_loading_success_failed))

    LottieAnimation(
        modifier = modifier,
        composition = composition,
        iterations = if (isSuccess || isFailed) 1 else LottieConstants.IterateForever,
        clipSpec = clipSpecs,
    )
}

@Preview
@Composable
fun LottieLoadingResultScreenPreview() {
    LottieLoadingResultScreen()
}
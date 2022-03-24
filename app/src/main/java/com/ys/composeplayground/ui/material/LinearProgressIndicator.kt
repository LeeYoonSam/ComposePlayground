package com.ys.composeplayground.ui.material

import android.widget.Space
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * LinearProgressIndicator
 *
 * LinearProgressIndicator는 진행률 표시줄이라고도 하는 선형 라인에 진행률을 표시하는 데 사용할 수 있습니다.
 *
 * 두 가지 종류가 있습니다.
 *
 * Indeterminate
 *
 * ```
 * LinearProgressIndicator()
 * ```
 * - Progress 매개변수 없이 LinearProgressIndicator를 사용하면 영원히 실행됩니다.
 *
 * Determinate
 *
 * ```
 * LinearProgressIndicator(progress = 0.5f)
 * ```
 * - 진행률 매개변수에 값을 설정하면 해당 진행률과 함께 표시기가 표시됩니다.
 * - 예를 들어 0.5f의 진행은 그것을 절반으로 채울 것입니다.
 */
@Composable
fun LinearProgressIndicatorDemo() {
    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text("LinearProgressIndicator with undefined progress")
        LinearProgressIndicator()
        Spacer(modifier = Modifier.height(30.dp))
        Text("LinearProgoressIndicator with progress set by buttons")
        LinearProgressIndicator(progress = animatedProgress)
        Spacer(modifier = Modifier.height(30.dp))

        Row{
            OutlinedButton(
                onClick = {
                    if (progress < 1f) progress += 0.1f
                }
            ) {
                Text("Increase")
            }
            Spacer(modifier = Modifier.width(10.dp))
            OutlinedButton(
                onClick = {
                    if (progress > 0f) progress -= 0.1f
                }
            ) {
                Text("Decrease")
            }
        }
    }
}

@Composable
fun PreviewLinearProgressIndicator() {
    LinearProgressIndicatorDemo()
}
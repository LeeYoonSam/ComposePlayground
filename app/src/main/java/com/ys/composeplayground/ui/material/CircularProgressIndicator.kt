package com.ys.composeplayground.ui.material

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * CircularProgressIndicator를 사용하여 진행 상황을 원형으로 표시할 수 있습니다.
 *
 * CircularProgressIndicator(progress = 0.5f)
 * 진행률 매개변수에 값을 설정하면 해당 진행률과 함께 표시기가 표시됩니다.
 * 예를 들어 0.5f의 진행은 원형의 절반으로 채울 것입니다.
 */

@Composable
fun CircularProgressIndicatorDemo() {
    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(30.dp))
        Text("CircularProgressIndicator with undefined progress")
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(30.dp))
        Text("CircularProgressIndicator with progress set by buttons")
        CircularProgressIndicator(progress = animatedProgress)
        Spacer(modifier = Modifier.height(30.dp))

        OutlinedButton(
            onClick = {
                if (progress < 1f) progress += 0.1f
            }
        ) {
            Text("Increase")
        }

        OutlinedButton(
            onClick = {
                if (progress > 0f) progress -= 0.1f
            }
        ) {
            Text("Decrease")
        }
    }
}

@Preview
@Composable
fun PreviewCircularProgressIndicator() {
    CircularProgressIndicatorDemo()
}
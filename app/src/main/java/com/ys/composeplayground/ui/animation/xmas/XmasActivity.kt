package com.ys.composeplayground.ui.animation.xmas

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.R
import com.ys.composeplayground.ui.animation.xmas.model.generateSnowflakes
import com.ys.composeplayground.ui.animation.xmas.utility.drawSnowflake
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class XmasActivity : AppCompatActivity() {
    lateinit var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mp = MediaPlayer.create(this, R.raw.jingle)

        setContent {
            ComposePlaygroundTheme {
                val showSnow by remember { mutableStateOf(true) }
                val alpha by animateFloatAsState(
                    targetValue = if (showSnow) 1f else 0f,
                    animationSpec = tween(durationMillis = 2000),
                    label = ""
                )

                if (alpha > 0f) {
                    SnowCanvas(alpha)
                }

                mp.start()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mp.stop()
    }
}

@Composable
fun SnowCanvas(alpha: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        val configuration = LocalConfiguration.current
        val screenHeight = with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }
        val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }

        val snowflakes = remember { generateSnowflakes(20, screenHeight, screenWidth) }
        val transition = rememberInfiniteTransition(label = "")
        var showXmasWish by remember { mutableStateOf(false) }

        val animatedOffsets = snowflakes.map { snowflake ->
            transition.animateFloat(
                initialValue = snowflake.startY,
                targetValue = snowflake.endY,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = snowflake.duration, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ), label = ""
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(Color.Black.copy(alpha = .8f))
                snowflakes.forEachIndexed { index, snowflake ->
                    val x = snowflake.x
                    val y = animatedOffsets[index].value
                    drawSnowflake(x, y, snowflake.size, alpha)
                }
            }
        }
    }
}
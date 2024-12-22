package com.ys.composeplayground.ui.animation.xmas

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.ys.composeplayground.R
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
//                    ShowCanvas(alpha)
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
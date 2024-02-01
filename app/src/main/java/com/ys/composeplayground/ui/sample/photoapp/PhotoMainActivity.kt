package com.ys.composeplayground.ui.sample.photoapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class PhotoMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposePlaygroundTheme {
                MainScreen()
            }
        }
    }
}
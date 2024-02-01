package com.ys.composeplayground.ui.sample.photoapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * 시스템 상태바 색상 변경
 */
@Composable
fun StatusBarColorProvider() {
    val systemUiController = rememberSystemUiController()
    val color = MaterialTheme.colorScheme.onSurface

    SideEffect {
        systemUiController.setSystemBarsColor(color = color)
    }
}
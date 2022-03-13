package com.ys.composeplayground

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

sealed class Demo(val title: String) {
    override fun toString() = title
}

class ActivityDemo<T : ComponentActivity>(
    title: String,
    val activityClass: KClass<T>
) : Demo(title)

class DemoCategory(
    title: String,
    val demos: List<Demo>
) : Demo(title)

class ComposableDemo(
    title: String,
    val content: @Composable () -> Unit
) : Demo(title)
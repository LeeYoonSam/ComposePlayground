package com.ys.composeplayground.ui.material

import com.ys.composeplayground.ComposableDemo
import com.ys.composeplayground.DemoCategory

val MaterialDemos = DemoCategory(
    "Materials",
    listOf(
        ComposableDemo("AlertDialog") { AlertDialogSample() },
    )
)
package com.ys.composeplayground

import com.ys.composeplayground.ui.album.AlbumActivity
import com.ys.composeplayground.ui.animation.CrossfadeDemo
import com.ys.composeplayground.ui.foundation.*
import com.ys.composeplayground.ui.material.AlertDialogSample
import com.ys.composeplayground.ui.material.ButtonDemos
import com.ys.composeplayground.ui.navigation.BottomNavigationAnimationActivity

val Animation = DemoCategory(
    "Animations",
    listOf(
        ComposableDemo("Crossfade") { CrossfadeDemo() },
    )
)

val FoundationDemos = DemoCategory(
    "Foundations",
    listOf(
        ComposableDemo("BaseTextField") { BaseTextField() },
        ComposableDemo("Canvas") { CanvasDrawExample() },
        ComposableDemo("Image") { ImageResourceDemo() },
        ComposableDemo("LazyColumn") { LazyColumnDemo() },
        ComposableDemo("LazyRow") { LazyRowDemo() },
        ComposableDemo("LazyVerticalGrid") { LazyVerticalGridDemo() },
        ComposableDemo("Shape") { ShapeDemo() },
        ComposableDemo("Text") { TextDemo() },
    )
)

val MaterialDemos = DemoCategory(
    "Materials",
    listOf(
        ComposableDemo("AlertDialog") { AlertDialogSample() },
        ComposableDemo("Button") { ButtonDemos() },
    )
)

val ActivityDemos = DemoCategory(
    "Activities",
    listOf(
        ActivityDemo("Album", AlbumActivity::class),
        ActivityDemo("BottomNavigationAnimationActivity", BottomNavigationAnimationActivity::class),
    )
)

val AllDemosCategory = DemoCategory(
    "Jetpack Compose Playground Demos",
    listOf(
        Animation,
        FoundationDemos,
        MaterialDemos,
        ActivityDemos,
    )
)
package com.ys.composeplayground

import com.ys.composeplayground.ui.album.AlbumActivity
import com.ys.composeplayground.ui.animation.CrossfadeDemo
import com.ys.composeplayground.ui.foundation.*
import com.ys.composeplayground.ui.keyboard.*
import com.ys.composeplayground.ui.material.*
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
        ComposableDemo("Card") { CardDemo() },
        ComposableDemo("Checkbox") { CheckboxDemo() },
        ComposableDemo("FloatingActionButtons") { FloatingActionButtonDemos() },
        ComposableDemo("ModalDrawer") { ModalDrawerDemo() },
        ComposableDemo("RadioButton") { RadioButtonDemo() },
        ComposableDemo("Scaffold") { ScaffoldDemo() },
        ComposableDemo("Slider") { SliderDemo() },
        ComposableDemo("Snackbar") { SnackbarDemo() },
        ComposableDemo("Switch") { SwitchDemo() },
    )
)

val ActivityDemos = DemoCategory(
    "Activities",
    listOf(
        ActivityDemo("Album", AlbumActivity::class),
        ActivityDemo("BottomNavigationAnimationActivity", BottomNavigationAnimationActivity::class),
        ActivityDemo("KeyboardHandlingActivity", KeyboardHandlingActivity::class),
    )
)

val KeyboardHandlingDemos = DemoCategory(
    "KeyboardHandling",
    listOf(
        ComposableDemo("KeyboardHandling basic") { KeyboardHandlingDemo1() },
        ComposableDemo("KeyboardHandling Multiple Text Fields") { KeyboardHandlingMultipleTextFieldsDemo() },
        ComposableDemo("KeyboardHandling Showing and Hiding Keyboard()") { ShowingHidingKeyboardDemo() },
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
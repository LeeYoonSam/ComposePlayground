package com.ys.composeplayground

import com.ys.composeplayground.ui.activity.lazycolumn.sectionheader.SectionHeaderLazyColumnActivity
import com.ys.composeplayground.ui.album.AlbumActivity
import com.ys.composeplayground.ui.animation.CrossfadeDemo
import com.ys.composeplayground.ui.foundation.*
import com.ys.composeplayground.ui.foundation.layout.SimpleFlowColumn
import com.ys.composeplayground.ui.foundation.layout.SimpleFlowColumnWithWeights
import com.ys.composeplayground.ui.foundation.layout.SimpleFlowRow
import com.ys.composeplayground.ui.foundation.layout.SimpleFlowRowWithWeights
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
        ComposableDemo("LazyVerticalGrid - Adaptive") { LazyVerticalGridDemo() },
        ComposableDemo("LazyVerticalGrid - Composite Items") { LazyVerticalGridCompositeSample() },
        ComposableDemo("LazyVerticalGrid - Section Items(span)") { LazyVerticalGridSpanSample() },
        ComposableDemo("Shape") { ShapeDemo() },
        ComposableDemo("Text") { TextDemo() },
        ComposableDemo("SelectionContainer") { SelectionSample() },
        ComposableDemo("VerticalPager") { SimpleVerticalPagerSample() },
        ComposableDemo("FlowColumn") { SimpleFlowColumn() },
        ComposableDemo("FlowColumn - weight") { SimpleFlowColumnWithWeights() },
        ComposableDemo("FlowRow") { SimpleFlowRow() },
        ComposableDemo("FlowRow - weight") { SimpleFlowRowWithWeights() },
    )
)

val MaterialDemos = DemoCategory(
    "Materials",
    listOf(
        ComposableDemo("AlertDialog") { AlertDialogSample() },
        ComposableDemo("Button") { ButtonDemos() },
        ComposableDemo("Card") { CardDemo() },
        ComposableDemo("CircularProgressIndicator") { CircularProgressIndicatorDemo() },
        ComposableDemo("Checkbox") { CheckboxDemo() },
        ComposableDemo("FloatingActionButtons") { FloatingActionButtonDemos() },
        ComposableDemo("ModalDrawer") { ModalDrawerDemo() },
        ComposableDemo("RadioButton") { RadioButtonDemo() },
        ComposableDemo("Scaffold") { ScaffoldDemo() },
        ComposableDemo("Slider") { SliderDemo() },
        ComposableDemo("Snackbar") { SnackbarDemo() },
        ComposableDemo("Switch") { SwitchDemo() },
        ComposableDemo("LinearProgressIndicator") { LinearProgressIndicatorDemo() },
        ComposableDemo("BadgeBox") { BadgeBoxDemo() },
        ComposableDemo("ModalBottomSheet") { ModalBottomSheetDemo() },
        ComposableDemo("Surface") { SurfaceDemo() },
    )
)

val ActivityDemos = DemoCategory(
    "Activities",
    listOf(
        ActivityDemo("Album", AlbumActivity::class),
        ActivityDemo("BottomNavigationAnimationActivity", BottomNavigationAnimationActivity::class),
        ActivityDemo("KeyboardHandlingActivity", KeyboardHandlingActivity::class),
        ActivityDemo("AnimatedLazyColumnSectionHeader", SectionHeaderLazyColumnActivity::class),
    )
)

val KeyboardHandlingDemos = DemoCategory(
    "KeyboardHandling",
    listOf(
        ComposableDemo("KeyboardHandling basic") { KeyboardHandlingDemo1() },
        ComposableDemo("KeyboardHandling Multiple Text Fields") { KeyboardHandlingMultipleTextFieldsDemo() },
        ComposableDemo("KeyboardHandling Showing and Hiding Keyboard()") { ShowingHidingKeyboardDemo() },
        ComposableDemo("KeyboardHandling LazyColumn Keyboard") { KeyboardLazyColumnDemo() },
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
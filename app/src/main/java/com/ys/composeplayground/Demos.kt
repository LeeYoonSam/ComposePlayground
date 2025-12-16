package com.ys.composeplayground

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.ys.composeplayground.core.model.data.DarkThemeConfig
import com.ys.composeplayground.core.model.data.ThemeBrand
import com.ys.composeplayground.ui.activity.lazycolumn.sectionheader.SectionHeaderLazyColumnActivity
import com.ys.composeplayground.ui.album.AlbumActivity
import com.ys.composeplayground.ui.animation.ColorTransitionDemo
import com.ys.composeplayground.ui.animation.CrossfadeDemo
import com.ys.composeplayground.ui.animation.ExpandableContentDemo
import com.ys.composeplayground.ui.animation.FadeInOutDemo
import com.ys.composeplayground.ui.animation.FlipCardDemo
import com.ys.composeplayground.ui.animation.LottieLoadingResultScreen
import com.ys.composeplayground.ui.animation.PressScaleDemo
import com.ys.composeplayground.ui.animation.ShimmerDemo
import com.ys.composeplayground.ui.animation.SlideAnimationDemo
import com.ys.composeplayground.ui.animation.SpringBounceDemo
import com.ys.composeplayground.ui.animation.StaggeredListDemo
import com.ys.composeplayground.ui.animation.SwipeToDismissDemo
import com.ys.composeplayground.ui.animation.xmas.XmasActivity
import com.ys.composeplayground.ui.canvas.DynamicStampScreen
import com.ys.composeplayground.ui.canvas.MovingCircleMenuCanvas
import com.ys.composeplayground.ui.custom.filter.FilterScreen
import com.ys.composeplayground.ui.custom.orderstatus.OrderStatusScreen
import com.ys.composeplayground.ui.custom.timeline.TimelineScreen
import com.ys.composeplayground.ui.dialog.settings.SettingsDialog
import com.ys.composeplayground.ui.dialog.settings.SettingsUiState
import com.ys.composeplayground.ui.dialog.settings.UserEditableSettings
import com.ys.composeplayground.ui.foundation.BaseTextField
import com.ys.composeplayground.ui.foundation.CanvasDrawExample
import com.ys.composeplayground.ui.foundation.ImageResourceDemo
import com.ys.composeplayground.ui.foundation.LazyColumnDemo
import com.ys.composeplayground.ui.foundation.LazyRowDemo
import com.ys.composeplayground.ui.foundation.LazyVerticalGridCompositeSample
import com.ys.composeplayground.ui.foundation.LazyVerticalGridDemo
import com.ys.composeplayground.ui.foundation.LazyVerticalGridSpanSample
import com.ys.composeplayground.ui.foundation.SelectionSample
import com.ys.composeplayground.ui.foundation.ShapeDemo
import com.ys.composeplayground.ui.foundation.SimpleVerticalPagerSample
import com.ys.composeplayground.ui.foundation.TextDemo
import com.ys.composeplayground.ui.foundation.layout.SimpleFlowColumn
import com.ys.composeplayground.ui.foundation.layout.SimpleFlowColumnWithWeights
import com.ys.composeplayground.ui.foundation.layout.SimpleFlowRow
import com.ys.composeplayground.ui.foundation.layout.SimpleFlowRowWithWeights
import com.ys.composeplayground.ui.grid.chip.GridChipScreenDemo
import com.ys.composeplayground.ui.keyboard.KeyboardHandlingActivity
import com.ys.composeplayground.ui.keyboard.KeyboardHandlingDemo1
import com.ys.composeplayground.ui.keyboard.KeyboardHandlingMultipleTextFieldsDemo
import com.ys.composeplayground.ui.keyboard.KeyboardLazyColumnDemo
import com.ys.composeplayground.ui.keyboard.ShowingHidingKeyboardDemo
import com.ys.composeplayground.ui.lazycolumn.NetflixToolbarScreen
import com.ys.composeplayground.ui.material.AlertDialogSample
import com.ys.composeplayground.ui.material.BadgeBoxDemo
import com.ys.composeplayground.ui.material.ButtonDemos
import com.ys.composeplayground.ui.material.CardDemo
import com.ys.composeplayground.ui.material.CheckboxDemo
import com.ys.composeplayground.ui.material.CircularProgressIndicatorDemo
import com.ys.composeplayground.ui.material.FloatingActionButtonDemos
import com.ys.composeplayground.ui.material.LinearProgressIndicatorDemo
import com.ys.composeplayground.ui.material.ModalBottomSheetDemo
import com.ys.composeplayground.ui.material.ModalDrawerDemo
import com.ys.composeplayground.ui.material.RadioButtonDemo
import com.ys.composeplayground.ui.material.ScaffoldDemo
import com.ys.composeplayground.ui.material.SliderDemo
import com.ys.composeplayground.ui.material.SnackbarDemo
import com.ys.composeplayground.ui.material.SurfaceDemo
import com.ys.composeplayground.ui.material.SwitchDemo
import com.ys.composeplayground.ui.modifier.LookaheadDemos
import com.ys.composeplayground.ui.navigation.BottomNavigationAnimationActivity
import com.ys.composeplayground.ui.sample.drawing.DrawingActivity
import com.ys.composeplayground.ui.sample.payment.CheckoutActivity
import com.ys.composeplayground.ui.sample.photoapp.PhotoMainActivity
import com.ys.composeplayground.ui.sample.product.ProductDetailMainActivity
import com.ys.composeplayground.ui.scroll.TimerCircleComponent
import com.ys.composeplayground.ui.scroll.TimerComponent
import com.ys.composeplayground.ui.scroll.timer.ScrollTimerActivity
import com.ys.composeplayground.ui.scroll.timer.ScrollTimerComposeActivity

val Animation = DemoCategory(
    "Animations",
    listOf(
        ComposableDemo("\uD83D\uDFE2 Beginner #1: 버튼 Press 스케일 애니메이션") { PressScaleDemo() },
        ComposableDemo("\uD83D\uDFE2 Beginner #2: 색상 전환 애니메이션") { ColorTransitionDemo() },
        ComposableDemo("\uD83D\uDFE2 Beginner #3: 확장/축소 콘텐츠 애니메이션") { ExpandableContentDemo() },
        ComposableDemo("\uD83D\uDFE2 Beginner #4: 페이드 인/아웃 애니메이션") { FadeInOutDemo() },
        ComposableDemo("\uD83D\uDFE2 Beginner #5: 슬라이드 진입 애니메이션") { SlideAnimationDemo() },
        ComposableDemo("\uD83D\uDFE1 Intermediate #6: Shimmer 로딩 애니메이션") { ShimmerDemo() },
        ComposableDemo("\uD83D\uDFE1 Intermediate #7: Spring 바운스 애니메이션") { SpringBounceDemo() },
        ComposableDemo("\uD83D\uDFE1 Intermediate #8: Staggered 리스트 애니메이션") { StaggeredListDemo() },
        ComposableDemo("\uD83D\uDFE1 Intermediate #9: Swipe to Dismiss 애니메이션") { SwipeToDismissDemo() },
        ComposableDemo("\uD83D\uDFE1 Intermediate #10: 3D 카드 플립 애니메이션") { FlipCardDemo() },
        ComposableDemo("LottieLoadingResult") { LottieLoadingResultScreen() },
        ComposableDemo("Crossfade") { CrossfadeDemo() },
        ActivityDemo("Xmas Animation", XmasActivity::class),
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
        ComposableDemo("Grid - Chip") { GridChipScreenDemo() },
    )
)

val ModifierDemos = DemoCategory(
    "Modifiers",
    listOf(
        ComposableDemo("ApproachLayout (Lookahead)") { LookaheadDemos() },
    )
)

val DialogDemos = DemoCategory(
    "Dialogs",
    listOf(
        ComposableDemo("SettingsDialog") {
            var showSettingsDialog by rememberSaveable { mutableStateOf(true) }
            if (showSettingsDialog) {
                SettingsDialog(
                    onDismiss = { showSettingsDialog = false},
                    settingsUiState = SettingsUiState.Success(
                        UserEditableSettings(
                            brand = ThemeBrand.DEFAULT,
                            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                            useDynamicColor = false,
                        ),
                    ),
                    onChangeThemeBrand = {},
                    onChangeDynamicColorPreference = {},
                    onChangeDarkThemeConfig = {},
                )
            }
        },
        ComposableDemo("SettingsDialog - supportDynamicColor") {
            var showSettingsDialog by rememberSaveable { mutableStateOf(true) }
            if (showSettingsDialog) {
                SettingsDialog(
                    onDismiss = { showSettingsDialog = false},
                    settingsUiState = SettingsUiState.Success(
                        UserEditableSettings(
                            brand = ThemeBrand.DEFAULT,
                            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                            useDynamicColor = false,
                        ),
                    ),
                    supportDynamicColor = true,
                    onChangeThemeBrand = {},
                    onChangeDynamicColorPreference = {},
                    onChangeDarkThemeConfig = {},
                )
            }
        },
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
        ActivityDemo("Payment Sample", CheckoutActivity::class),
        ActivityDemo("PhotoApp Sample", PhotoMainActivity::class),
        ActivityDemo("Product.Detail Sample", ProductDetailMainActivity::class),
        ActivityDemo("DrawingCanvas", DrawingActivity::class),
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

val ScrollDemos = DemoCategory(
    "Scrolls",
    listOf(
        ComposableDemo("TimerComponent") { TimerComponent() },
        ComposableDemo("TimerCircleComponent") { TimerCircleComponent() },
    )
)

val LazyListDemos = DemoCategory(
    "LazyList",
    listOf(
        ComposableDemo("NetflixToolbar") { NetflixToolbarScreen() },
    )
)

val CanvasDemos = DemoCategory(
    "Canvas",
    listOf(
        ComposableDemo("Moving circle") { MovingCircleMenuCanvas() },
        ComposableDemo("Dynamic Stamp") { DynamicStampScreen() },
    )
)

val CustomDemos = DemoCategory(
    "Custom Design",
    listOf(
        ComposableDemo("Filter") { FilterScreen() },
        ComposableDemo("Timeline") { TimelineScreen() },
        ComposableDemo("ShippingProgress") { OrderStatusScreen() },
    )
)

val ScrollTimerDemos = DemoCategory(
    "Scroll Timer",
    listOf(
        ActivityDemo("RecyclerView 버전", ScrollTimerActivity::class),
        ActivityDemo("LazyColumn 버전", ScrollTimerComposeActivity::class),
    )
)

val AllDemosCategory = DemoCategory(
    "Jetpack Compose Playground Demos",
    listOf(
        Animation,
        FoundationDemos,
        ModifierDemos,
        DialogDemos,
        MaterialDemos,
        ActivityDemos,
        ScrollDemos,
        LazyListDemos,
        CanvasDemos,
        CustomDemos,
        ScrollTimerDemos,
    )
)
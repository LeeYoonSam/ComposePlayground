package com.ys.composeplayground

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.ys.composeplayground.ui.activity.lazycolumn.sectionheader.SectionHeaderLazyColumnActivity
import com.ys.composeplayground.ui.album.AlbumActivity
import com.ys.composeplayground.ui.animation.CrossfadeDemo
import com.ys.composeplayground.ui.animation.LottieLoadingResultScreen
import com.ys.composeplayground.core.model.data.DarkThemeConfig
import com.ys.composeplayground.ui.dialog.settings.SettingsDialog
import com.ys.composeplayground.ui.dialog.settings.SettingsUiState
import com.ys.composeplayground.core.model.data.ThemeBrand
import com.ys.composeplayground.ui.canvas.MovingCircleMenuCanvas
import com.ys.composeplayground.ui.dialog.settings.UserEditableSettings
import com.ys.composeplayground.ui.filter.FilterScreen
import com.ys.composeplayground.ui.foundation.*
import com.ys.composeplayground.ui.foundation.layout.SimpleFlowColumn
import com.ys.composeplayground.ui.foundation.layout.SimpleFlowColumnWithWeights
import com.ys.composeplayground.ui.foundation.layout.SimpleFlowRow
import com.ys.composeplayground.ui.foundation.layout.SimpleFlowRowWithWeights
import com.ys.composeplayground.ui.keyboard.*
import com.ys.composeplayground.ui.lazycolumn.NetflixToolbarScreen
import com.ys.composeplayground.ui.material.*
import com.ys.composeplayground.ui.navigation.BottomNavigationAnimationActivity
import com.ys.composeplayground.ui.sample.drawing.DrawingActivity
import com.ys.composeplayground.ui.sample.payment.CheckoutActivity
import com.ys.composeplayground.ui.sample.photoapp.PhotoMainActivity
import com.ys.composeplayground.ui.sample.product.ProductDetailMainActivity
import com.ys.composeplayground.ui.scroll.TimerCircleComponent
import com.ys.composeplayground.ui.scroll.TimerComponent

val Animation = DemoCategory(
    "Animations",
    listOf(
        ComposableDemo("Crossfade") { CrossfadeDemo() },
        ComposableDemo("LottieLoadingResult") { LottieLoadingResultScreen() },
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
    )
)

val CustomDemos = DemoCategory(
    "Custom Design",
    listOf(
        ComposableDemo("Filter") { FilterScreen() },
    )
)

val AllDemosCategory = DemoCategory(
    "Jetpack Compose Playground Demos",
    listOf(
        Animation,
        FoundationDemos,
        DialogDemos,
        MaterialDemos,
        ActivityDemos,
        ScrollDemos,
        LazyListDemos,
        CanvasDemos,
        CustomDemos,
    )
)
package com.ys.composeplayground

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.ComposeView
import com.ys.composeplayground.extensions.toast
import com.ys.composeplayground.ui.common.DemoApp
import com.ys.composeplayground.ui.common.Navigator
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {

    private var backPressed = 0L

    private val finish: () -> Unit = {
        if (backPressed + 1500 > System.currentTimeMillis()) {
            finishAndRemoveTask()
        } else {
            toast(getString(R.string.app_exit_label))
        }
        backPressed = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ComposeView(this).also {
            setContentView(it)
        }.setContent {
            val activityStarter = fun(demo: ActivityDemo<*>) {
                startActivity(Intent(this, demo.activityClass.java))
            }

            val navigator = rememberSaveable(
                saver = Navigator.Saver(AllDemosCategory, onBackPressedDispatcher, activityStarter, finish)
            ) {
                Navigator(AllDemosCategory, onBackPressedDispatcher, activityStarter, finish)
            }

            ComposePlaygroundTheme {
                DemoApp(
                    currentDemo = navigator.currentDemo,
                    backStackTitle = navigator.backStackTitle,
                    onNavigateToDemo = { demo ->
                        navigator.navigateTo(demo)
                    }
                )
            }
        }
    }
}
package com.ys.composeplayground.ui.keyboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.ComposeView
import com.ys.composeplayground.ActivityDemo
import com.ys.composeplayground.KeyboardHandlingDemos
import com.ys.composeplayground.ui.common.DemoApp
import com.ys.composeplayground.ui.common.Navigator
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class KeyboardHandlingActivity : AppCompatActivity() {

    private val finish: () -> Unit = {
        finish()
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
                saver = Navigator.Saver(KeyboardHandlingDemos, onBackPressedDispatcher, activityStarter, finish)
            ) {
                Navigator(KeyboardHandlingDemos, onBackPressedDispatcher, activityStarter, finish)
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
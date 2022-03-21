package com.ys.composeplayground.ui.common

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import com.ys.composeplayground.ActivityDemo
import com.ys.composeplayground.Demo
import com.ys.composeplayground.DemoCategory

class Navigator private constructor(
    private val backDispatcher: OnBackPressedDispatcher,
    private val launchActivityDemo: (ActivityDemo<*>) -> Unit,
    private val rootDemo: Demo,
    initialDemo: Demo,
    private val backStack: MutableList<Demo>,
    private val finish: () -> Unit
) {
    constructor(
        rootDemo: Demo,
        backDispatcher: OnBackPressedDispatcher,
        launchActivityDemo: (ActivityDemo<*>) -> Unit,
        finish: () -> Unit,
    ) : this(backDispatcher, launchActivityDemo, rootDemo, rootDemo, mutableListOf<Demo>(), finish)

    private val onBackPressed = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            if (!isRoot) {
                popBackStack()
                return
            }

            finish()
        }
    }.apply {
        isEnabled = true
        backDispatcher.addCallback(this)
    }

    private var _currentDemo by mutableStateOf(initialDemo)
    var currentDemo: Demo
        get() = _currentDemo
        private set(value) {
            _currentDemo = value
            onBackPressed.isEnabled = true
        }

    val isRoot: Boolean get() = backStack.isEmpty()

    val backStackTitle: String
        get() = (backStack.drop(1) + currentDemo).joinToString(separator = " > ") { it.title }

    fun navigateTo(demo: Demo) {
        if (demo is ActivityDemo<*>) {
            launchActivityDemo(demo)
        } else {
            backStack.add(currentDemo)
            currentDemo = demo
        }
    }

    fun popAll() {
        if (!isRoot) {
            backStack.clear()
            currentDemo = rootDemo
        }
    }

    private fun popBackStack() {
        currentDemo = backStack.removeAt(backStack.lastIndex)
    }

    companion object {
        fun Saver(
            rootDemo: DemoCategory,
            backDispatcher: OnBackPressedDispatcher,
            launchActivityDemo: (ActivityDemo<*>) -> Unit,
            finish: () -> Unit
        ): Saver<Navigator, *> = listSaver<Navigator, String>(
            save = { navigator ->
                (navigator.backStack + navigator.currentDemo).map { it.title }
            },
            restore = { restored ->
                require(restored.isNotEmpty())
                val backStack = restored.mapTo(mutableListOf()) {
                    requireNotNull(findDemo(rootDemo, it))
                }
                val initial = backStack.removeAt(backStack.lastIndex)
                Navigator(backDispatcher, launchActivityDemo, rootDemo, initial, backStack, finish)
            }
        )

        private fun findDemo(demo: Demo, title: String): Demo? {
            if (demo.title == title) {
                return demo
            }
            if (demo is DemoCategory) {
                demo.demos.forEach { child ->
                    findDemo(child, title)?.let { return it }
                }
            }
            return null
        }
    }
}
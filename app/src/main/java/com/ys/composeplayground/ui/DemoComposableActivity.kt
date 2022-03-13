package com.ys.composeplayground.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.*
import com.ys.composeplayground.ui.material.MaterialDemos

class DemoComposableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ComposeView(this).also {
            setContentView(it)
        }.setContent {
            val navigator = rememberSaveable(
                saver = Navigator.Saver(MaterialDemos, onBackPressedDispatcher)
            ) {
                Navigator(MaterialDemos, onBackPressedDispatcher)
            }

            DemoApp(
                currentDemo = navigator.currentDemo,
                backStackTitle = navigator.backStackTitle,
                onNavigateToDemo = { demo ->
                    navigator.navigateTo(demo)
                },
            )
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, DemoComposableActivity::class.java)
    }
}

@Composable
fun DemoApp(
    currentDemo: Demo,
    backStackTitle: String,
    onNavigateToDemo: (Demo) -> Unit,
) {
    Scaffold(
        topBar = {
            DemoAppBar(
                title = backStackTitle,
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        DemoContent(modifier, currentDemo, onNavigateToDemo)
    }
}

@Composable
private fun DemoContent(
    modifier: Modifier,
    currentDemo: Demo,
    onNavigate: (Demo) -> Unit
) {
    Crossfade(targetState = currentDemo) { demo ->
        Surface(modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            DisplayDemo(demo, onNavigate)
        }
    }
}

@Composable
private fun DisplayDemo(demo: Demo, onNavigate: (Demo) -> Unit) {
    when (demo) {
        is ActivityDemo<*> -> {
            /* should never get here as activity demos are not added to the backstack*/
        }
        is ComposableDemo -> demo.content()
        is DemoCategory -> DisplayDemoCategory(demo, onNavigate)
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun DisplayDemoCategory(category: DemoCategory, onNavigate: (Demo) -> Unit) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        category.demos.forEach { demo ->
            ListItem(
                text = {
                    Text(
                        modifier = Modifier.height(56.dp)
                            .wrapContentSize(Alignment.Center),
                        text = demo.title
                    )
                },
                modifier = Modifier.clickable { onNavigate(demo) }
            )
        }
    }
}

@Suppress("ComposableLambdaParameterNaming", "ComposableLambdaParameterPosition")
@Composable
private fun DemoAppBar(
    title: String,
) {
    TopAppBar(
        title = {
            Text(title, Modifier.testTag(Tags.AppBarTitle))
        }
    )
}

private class Navigator private constructor(
    private val backDispatcher: OnBackPressedDispatcher,
//    private val launchActivityDemo: (ActivityDemo<*>) -> Unit,
    private val rootDemo: Demo,
    initialDemo: Demo,
    private val backStack: MutableList<Demo>
) {
    constructor(
        rootDemo: Demo,
        backDispatcher: OnBackPressedDispatcher,
//        launchActivityDemo: (ActivityDemo<*>) -> Unit
//    ) : this(backDispatcher, launchActivityDemo, rootDemo, rootDemo, mutableListOf<Demo>())
    ) : this(backDispatcher, rootDemo, rootDemo, mutableListOf<Demo>())

    private val onBackPressed = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            popBackStack()
        }
    }.apply {
        isEnabled = !isRoot
        backDispatcher.addCallback(this)
    }

    private var _currentDemo by mutableStateOf(initialDemo)
    var currentDemo: Demo
        get() = _currentDemo
        private set(value) {
            _currentDemo = value
            onBackPressed.isEnabled = !isRoot
        }

    val isRoot: Boolean get() = backStack.isEmpty()

    val backStackTitle: String
        get() = (backStack.drop(1) + currentDemo).joinToString(separator = " > ") { it.title }

    fun navigateTo(demo: Demo) {
        if (demo is ActivityDemo<*>) {
//            launchActivityDemo(demo)
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
//            launchActivityDemo: (ActivityDemo<*>) -> Unit
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
//                Navigator(backDispatcher, launchActivityDemo, rootDemo, initial, backStack)
                Navigator(backDispatcher, rootDemo, initial, backStack)
            }
        )

        private fun findDemo(demo: Demo, title: String): Demo? {
            if (demo.title == title) {
                return demo
            }
            if (demo is DemoCategory) {
                demo.demos.forEach { child ->
                    findDemo(child, title)
                        ?.let { return it }
                }
            }
            return null
        }
    }
}
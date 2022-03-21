package com.ys.composeplayground.ui.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.*

@Suppress("ComposableLambdaParameterNaming", "ComposableLambdaParameterPosition")
@Composable
fun DemoAppBar(
    title: String,
) {
    TopAppBar(
        title = {
            Text(title, Modifier.testTag(Tags.AppBarTitle))
        }
    )
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
        Surface(modifier.fillMaxWidth(), color = MaterialTheme.colors.background) {
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
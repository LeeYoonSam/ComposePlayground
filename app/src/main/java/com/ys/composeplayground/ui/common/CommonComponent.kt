package com.ys.composeplayground.ui.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.ys.composeplayground.ActivityDemo
import com.ys.composeplayground.ComposableDemo
import com.ys.composeplayground.Demo
import com.ys.composeplayground.DemoCategory
import com.ys.composeplayground.Tags
import com.ys.composeplayground.ui.snackbar.SnackbarController
import com.ys.composeplayground.ui.snackbar.SnackbarControllerProvider
import com.ys.composeplayground.ui.theme.CustomTypography

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ComposableLambdaParameterNaming", "ComposableLambdaParameterPosition")
@Composable
fun DemoAppBar(
    title: String,
) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = CustomTypography.body1BoldMedium,
                    modifier = Modifier
                        .padding(10.dp)
                        .testTag(Tags.AppBarTitle)
                )

                Divider()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoApp(
    currentDemo: Demo,
    backStackTitle: String,
    onNavigateToDemo: (Demo) -> Unit,
) {
    SnackbarControllerProvider { host ->
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = host) },
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
}

@Composable
private fun DemoContent(
    modifier: Modifier,
    currentDemo: Demo,
    onNavigate: (Demo) -> Unit
) {
    Crossfade(targetState = currentDemo, label = "") { demo ->
        Surface(modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.background) {
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
private fun DisplayDemoCategory(category: DemoCategory, onNavigate: (Demo) -> Unit) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        category.demos.forEach { demo ->
            ListItem(
                headlineContent = {
                    Text(
                        modifier = Modifier
                            .height(56.dp)
                            .wrapContentSize(Alignment.Center),
                        text = demo.title
                    )
                },
                modifier = Modifier.clickable { onNavigate(demo) }
            )
        }
    }
}
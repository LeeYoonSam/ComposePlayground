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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

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
    val saveableStateHolder = rememberSaveableStateHolder()

    Crossfade(targetState = currentDemo, label = "") { demo ->
        Surface(modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.background) {
            saveableStateHolder.SaveableStateProvider(key = demo.title) {
                DisplayDemo(demo, onNavigate)
            }
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

@OptIn(FlowPreview::class)
@Composable
private fun DisplayDemoCategory(category: DemoCategory, onNavigate: (Demo) -> Unit) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var filteredDemos by remember {
        mutableStateOf(
            if (searchQuery.isBlank()) category.demos
            else category.demos.flatMap { demo -> searchDemos(demo, searchQuery) }
        )
    }

    LaunchedEffect(category) {
        snapshotFlow { searchQuery }
            .debounce(300)
            .collectLatest { query ->
                filteredDemos = if (query.isBlank()) {
                    category.demos
                } else {
                    category.demos.flatMap { demo -> searchDemos(demo, query) }
                }
            }
    }

    Column {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("검색") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "검색") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "지우기")
                    }
                }
            },
            singleLine = true,
        )

        Column(Modifier.verticalScroll(rememberScrollState())) {
            filteredDemos.forEach { demo ->
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
}

private fun searchDemos(demo: Demo, query: String): List<Demo> {
    val lowerQuery = query.lowercase()
    return when (demo) {
        is DemoCategory -> demo.demos.flatMap { searchDemos(it, query) }
        else -> if (demo.title.lowercase().contains(lowerQuery)) listOf(demo) else emptyList()
    }
}
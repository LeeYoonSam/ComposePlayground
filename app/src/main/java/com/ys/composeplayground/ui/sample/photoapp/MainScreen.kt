package com.ys.composeplayground.ui.sample.photoapp

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ys.composeplayground.ui.sample.photoapp.feed.Feed
import com.ys.composeplayground.ui.sample.photoapp.profile.Profile

@Composable
fun MainScreen() {
    StatusBarColorProvider()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        val viewModel = viewModel<PhotographersViewModel>()
        var selectedId by rememberSaveable { mutableStateOf<String?>(null) }
        Crossfade(targetState = selectedId, label = "") { id ->
            if (id == null) {
                Feed(
                    photographersFlow = viewModel.photographers,
                    onSelected = { selectedId = it.id }
                )
            } else {
                Profile(viewModel.getById(id))
                BackHandler {
                    selectedId = null
                }
            }
        }
    }
}
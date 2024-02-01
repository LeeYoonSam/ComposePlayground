package com.ys.composeplayground.ui.sample.photoapp

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ys.composeplayground.ui.sample.photoapp.feed.Feed

@Composable
fun MainScreen() {
    StatusBarColorProvider()
    Surface(color = MaterialTheme.colorScheme.onSurface) {
        val viewModel = viewModel<PhotographersViewModel>()
        var selectedId by rememberSaveable { mutableStateOf<String?>(null) }
        Crossfade(targetState = selectedId, label = "") { id ->
            if (id == null) {
                Feed(
                    photographersFlow = viewModel.photographers,
                    onSelected = {}
                )
            } else {
                Text(text = "Profile - ${viewModel.getById(id)}")
                BackHandler {
                    selectedId = null
                }
            }
        }
    }
}
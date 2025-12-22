package com.ys.composeplayground.ui.sample.photoapp.feed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ys.composeplayground.ui.sample.photoapp.Photographer
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Feed(
    photographersFlow: StateFlow<List<Photographer>>,
    onSelected: (Photographer) -> Unit,
) {
    Surface(Modifier.fillMaxSize()) {
        val photographers by photographersFlow.collectAsState()
        val state = rememberLazyListState()
        LazyColumn(state = state) {
            item {
                FeedHeader()
            }
            items(photographers.subList(fromIndex = 0, toIndex = minOf(photographers.size, 2))) {
                PhotographerCard(photographer = it, onClick = { onSelected(it) })
            }
            if (photographers.size > 2) {
                item {
                    AdBanner()
                }
                items(photographers.subList(fromIndex = 2, toIndex = photographers.size)) {
                    PhotographerCard(photographer = it, onClick = { onSelected(it) })
                }
            }

        }
        FeedFab(
            state = state,
            modifier = Modifier.wrapContentSize(align = Alignment.BottomEnd)
        )
    }
}
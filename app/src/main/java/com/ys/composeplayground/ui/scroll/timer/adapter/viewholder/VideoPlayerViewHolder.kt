package com.ys.composeplayground.ui.scroll.timer.adapter.viewholder

import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.ys.composeplayground.ui.scroll.timer.VideoPlayerItem
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class VideoPlayerViewHolder(
    private val composeView: ComposeView
) : RecyclerView.ViewHolder(composeView) {

    fun bind(title: String?) {
        composeView.setContent {
            ComposePlaygroundTheme {
                VideoPlayerItem(title)
            }
        }
    }
}

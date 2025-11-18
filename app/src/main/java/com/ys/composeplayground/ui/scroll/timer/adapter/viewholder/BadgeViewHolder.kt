package com.ys.composeplayground.ui.scroll.timer.adapter.viewholder

import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.ys.composeplayground.ui.scroll.timer.BadgeItem
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class BadgeViewHolder(
    private val composeView: ComposeView
) : RecyclerView.ViewHolder(composeView) {

    fun bind(badges: List<*>?) {
        composeView.setContent {
            ComposePlaygroundTheme {
                BadgeItem(badges)
            }
        }
    }
}

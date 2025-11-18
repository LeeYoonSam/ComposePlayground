package com.ys.composeplayground.ui.scroll.timer.adapter.viewholder

import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.ys.composeplayground.ui.scroll.timer.ProductSpecItem
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class ProductSpecViewHolder(
    private val composeView: ComposeView
) : RecyclerView.ViewHolder(composeView) {

    fun bind(specs: Map<*, *>?) {
        composeView.setContent {
            ComposePlaygroundTheme {
                ProductSpecItem(specs)
            }
        }
    }
}

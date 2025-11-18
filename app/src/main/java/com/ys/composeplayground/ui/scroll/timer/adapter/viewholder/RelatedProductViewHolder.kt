package com.ys.composeplayground.ui.scroll.timer.adapter.viewholder

import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.ys.composeplayground.ui.scroll.timer.RelatedProductItem
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class RelatedProductViewHolder(
    private val composeView: ComposeView
) : RecyclerView.ViewHolder(composeView) {

    fun bind(products: List<*>?) {
        composeView.setContent {
            ComposePlaygroundTheme {
                RelatedProductItem(products)
            }
        }
    }
}

package com.ys.composeplayground.ui.scroll.timer.adapter.viewholder

import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.ys.composeplayground.R
import com.ys.composeplayground.ui.scroll.timer.ImageGalleryItem
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class ImageGalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val composeView: ComposeView = itemView.findViewById(R.id.imageGalleryComposeView)

    @Suppress("UNCHECKED_CAST")
    fun bind(images: List<*>?) {
        composeView.setContent {
            ComposePlaygroundTheme {
                ImageGalleryItem(images as? List<String>)
            }
        }
    }
}

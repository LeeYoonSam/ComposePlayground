package com.ys.composeplayground.ui.scroll.timer.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ys.composeplayground.R

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val titleText: TextView = itemView.findViewById(R.id.titleText)
    private val priceText: TextView = itemView.findViewById(R.id.priceText)

    fun bind(title: String?) {
        titleText.text = title ?: "작품 제목"
        priceText.text = "₩ 50,000"
    }
}

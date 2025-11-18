package com.ys.composeplayground.ui.scroll.timer.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ys.composeplayground.R

class DescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)

    fun bind(description: String?) {
        descriptionText.text = description ?: "상품 설명이 없습니다."
    }
}

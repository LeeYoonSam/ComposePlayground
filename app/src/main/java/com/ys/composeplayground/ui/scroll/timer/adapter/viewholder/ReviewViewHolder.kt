package com.ys.composeplayground.ui.scroll.timer.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ys.composeplayground.R

class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val reviewText: TextView = itemView.findViewById(R.id.reviewText)

    fun bind(review: String?) {
        reviewText.text = review ?: "후기 내용이 없습니다."
    }
}

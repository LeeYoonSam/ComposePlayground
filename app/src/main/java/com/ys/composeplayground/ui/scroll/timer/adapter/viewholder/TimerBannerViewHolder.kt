package com.ys.composeplayground.ui.scroll.timer.adapter.viewholder

import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.ys.composeplayground.R
import com.ys.composeplayground.ui.scroll.timer.TimerBannerView
import com.ys.composeplayground.ui.scroll.timer.model.TimerState
import com.ys.composeplayground.ui.theme.ComposePlaygroundTheme

class TimerBannerViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    private val composeView: ComposeView = itemView.findViewById(R.id.timerBannerComposeView)

    fun bind(timerState: TimerState?) {
        timerState?.let { state ->
            composeView.setContent {
                ComposePlaygroundTheme {
                    TimerBannerView(state)
                }
            }
        }
    }
}

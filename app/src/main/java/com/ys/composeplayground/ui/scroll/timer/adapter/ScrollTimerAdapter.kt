package com.ys.composeplayground.ui.scroll.timer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView
import com.ys.composeplayground.R
import com.ys.composeplayground.ui.scroll.timer.adapter.viewholder.*
import com.ys.composeplayground.ui.scroll.timer.model.ScrollTimerItem
import com.ys.composeplayground.ui.scroll.timer.model.TimerState
import com.ys.composeplayground.ui.scroll.timer.model.ViewType

/**
 * RecyclerView Adapter for 스크롤 기반 타이머
 */
class ScrollTimerAdapter(
    items: List<ScrollTimerItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<ScrollTimerItem> = items.toMutableList()

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (ViewType.entries[viewType]) {
            ViewType.HEADER -> HeaderViewHolder(
                inflater.inflate(R.layout.item_header, parent, false)
            )
            ViewType.IMAGE_GALLERY -> ImageGalleryViewHolder(
                inflater.inflate(R.layout.item_image_gallery, parent, false)
            )
            ViewType.BADGE -> BadgeViewHolder(
                ComposeView(parent.context)
            )
            ViewType.DESCRIPTION -> DescriptionViewHolder(
                inflater.inflate(R.layout.item_description, parent, false)
            )
            ViewType.TIMER_BANNER -> TimerBannerViewHolder(
                inflater.inflate(R.layout.item_timer_banner, parent, false)
            )
            ViewType.PRODUCT_SPEC -> ProductSpecViewHolder(
                ComposeView(parent.context)
            )
            ViewType.DIVIDER -> DividerViewHolder(
                ComposeView(parent.context)
            )
            ViewType.REVIEW -> ReviewViewHolder(
                inflater.inflate(R.layout.item_review, parent, false)
            )
            ViewType.VIDEO_PLAYER -> VideoPlayerViewHolder(
                ComposeView(parent.context)
            )
            ViewType.RELATED_PRODUCT -> RelatedProductViewHolder(
                ComposeView(parent.context)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (holder) {
            is HeaderViewHolder -> holder.bind(item.data as? String)
            is ImageGalleryViewHolder -> holder.bind(item.data as? List<*>)
            is BadgeViewHolder -> holder.bind(item.data as? List<*>)
            is DescriptionViewHolder -> holder.bind(item.data as? String)
            is TimerBannerViewHolder -> holder.bind(item.data as? TimerState)
            is ProductSpecViewHolder -> holder.bind(item.data as? Map<*, *>)
            is DividerViewHolder -> holder.bind(item.data as? String)
            is ReviewViewHolder -> holder.bind(item.data as? String)
            is VideoPlayerViewHolder -> holder.bind(item.data as? String)
            is RelatedProductViewHolder -> holder.bind(item.data as? List<*>)
        }
    }

    override fun getItemCount(): Int = items.size

    /**
     * 타이머 상태 업데이트
     */
    fun updateTimerState(timerState: TimerState) {
        val index = items.indexOfFirst { it.viewType == ViewType.TIMER_BANNER }
        if (index != -1) {
            items[index] = items[index].copy(data = timerState)
            notifyItemChanged(index)
        }
    }

    /**
     * 타이머 완료 시 타이머 배너 아이템 제거
     */
    fun notifyTimerCompleted() {
        val index = items.indexOfFirst { it.viewType == ViewType.TIMER_BANNER }
        if (index != -1) {
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}

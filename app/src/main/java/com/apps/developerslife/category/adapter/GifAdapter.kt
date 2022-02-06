package com.apps.developerslife.category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.developerslife.R
import com.apps.developerslife.model.GifItem

class GifAdapter : RecyclerView.Adapter<BaseGifViewHolder>() {

    private var gifItems: MutableList<GifItem> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseGifViewHolder {
        val gifItems: View =
            LayoutInflater.from(parent.context).inflate(R.layout.gif_item, parent, false)
        return GifViewHolder(gifItems)
    }

    override fun onBindViewHolder(holder: BaseGifViewHolder, position: Int) {
        holder.onBindViewHolder(gifItems[position])
    }

    override fun getItemCount(): Int {
        return gifItems.size
    }

    fun update(newGifs: List<GifItem>) {
        val positionStart = gifItems.size
        val itemCount = newGifs.size - gifItems.size
        gifItems.clear()
        gifItems.addAll(newGifs)
        notifyItemRangeInserted(positionStart, itemCount)
    }
}
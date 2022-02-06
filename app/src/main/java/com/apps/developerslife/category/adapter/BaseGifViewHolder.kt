package com.apps.developerslife.category.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.apps.developerslife.model.GifItem

abstract class BaseGifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onCreateViewHolder()
    abstract fun onBindViewHolder(gifItem: GifItem)
}
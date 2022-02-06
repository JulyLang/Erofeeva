package com.apps.developerslife.category.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.apps.developerslife.R
import com.apps.developerslife.model.GifItem
import com.apps.developerslife.okhttp.GlideApp
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class GifViewHolder(itemView: View) : BaseGifViewHolder(itemView) {

    private var gifItem: GifItem? = null
    private var itemGif: ImageView = itemView.findViewById(R.id.itemGif)
    private var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    private var dataLoadErrorTextView: TextView = itemView.findViewById(R.id.dataLoadErrorTextView)
    private var retryButton: Button = itemView.findViewById(R.id.retryButton)

    override fun onCreateViewHolder() {
        retryButton.setOnClickListener {
            dataLoadErrorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            startImageLoading()
        }
    }

    override fun onBindViewHolder(gifItem: GifItem) {
        this.gifItem = gifItem
        progressBar.visibility = View.VISIBLE
        startImageLoading()
    }

    private fun startImageLoading() {
        GlideApp.with(itemGif)
            .asGif()
            .load(gifItem?.gifUrl)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    dataLoadErrorTextView.visibility = View.VISIBLE
                    retryButton.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    return true
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    dataLoadErrorTextView.visibility = View.GONE
                    retryButton.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(itemGif)
    }
}
package com.apps.developerslife.category

import com.apps.developerslife.model.GifItem

data class GifPageData(
    val gifs: List<GifItem>,
    val areGifsUpdated: Boolean,
    val isForwardButtonEnabled: Boolean,
    val isBackButtonEnabled: Boolean
)
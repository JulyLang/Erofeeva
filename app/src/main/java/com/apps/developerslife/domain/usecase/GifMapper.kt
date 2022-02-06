package com.apps.developerslife.domain.usecase

import com.apps.developerslife.data.net.GifModel
import com.apps.developerslife.model.GifItem

object GifMapper {

    fun mapModelToItem(gifModel: GifModel): GifItem {
       return GifItem(
           gifUrl = gifModel.gifURL ?: "",
           gifDescription = gifModel.gifDescription ?: ""
       )
    }

    fun mapModelToItem(gifModels: List<GifModel>): List<GifItem> {
        return gifModels.map { mapModelToItem(it) }
    }
}
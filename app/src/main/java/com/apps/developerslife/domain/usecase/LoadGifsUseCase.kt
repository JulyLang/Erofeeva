package com.apps.developerslife.domain.usecase

import com.apps.developerslife.data.repository.IGifRepository
import com.apps.developerslife.model.GifItem
import io.reactivex.Observable

class LoadGifsUseCase(
    private val repository: IGifRepository
) {

    fun buildObservable(params: Params): Observable<List<GifItem>> {
        return repository.getGifPage(params.category, params.pageNumber)
            .map { it.map { gifModel -> GifItem(gifUrl = gifModel.gifURL ?: "") } }
    }

    class Params(
        val category: String,
        val pageNumber: Int
    )
}
package com.apps.developerslife.domain.usecase

import com.apps.developerslife.data.repository.IGifRepository
import com.apps.developerslife.model.GifItem
import io.reactivex.Observable

class LoadGifsUseCase(
    private val repository: IGifRepository
) {

    fun observeGifs(): Observable<List<GifItem>> {
        return repository.observeGifs()
            .map { gifModels -> GifMapper.mapModelToItem(gifModels) }
    }

    fun loadGifs(params: LoadParams): Observable<Boolean> {
        return repository.loadGifPage(params.category, params.clearPrevious)
    }

    class LoadParams(
        val category: String,
        val clearPrevious: Boolean
    )
}
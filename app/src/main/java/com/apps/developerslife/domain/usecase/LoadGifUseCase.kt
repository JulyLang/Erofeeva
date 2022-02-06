package com.apps.developerslife.domain.usecase

import com.apps.developerslife.data.repository.IGifRepository
import com.apps.developerslife.model.GifItem
import io.reactivex.Observable

class LoadGifUseCase(
    private val repository: IGifRepository
) {

    fun buildObservable(params: Params): Observable<GifItem> {
        return repository.getRandomGif().map { model -> GifItem(model.gifURL?: "")}
    }

    class Params(
        id: Int
    )
}
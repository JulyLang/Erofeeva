package com.apps.developerslife.data.repository

import com.apps.developerslife.data.net.GifModel
import io.reactivex.Observable

class GifRepository(
    private val localDataStore: IGifLocalDataStore,
    private val remoteDataStore: IGifRemoteDataStore
) : IGifRepository {

    override fun getRandomGif(): Observable<GifModel> {
        return remoteDataStore.getRandomGif()
    }

    override fun observeGifs(): Observable<List<GifModel>> {
        return localDataStore.observeGifs()
    }

    override fun loadGifPage(category: String, clearPrevious: Boolean): Observable<Boolean> {
        return localDataStore.getLastPageNumber()
            .flatMap { pageNumber -> remoteDataStore.getGifPage(category, pageNumber) }
            .flatMap { localDataStore.saveGifs(clearPrevious, it) }
            .flatMap { result -> localDataStore.notifyGifs().map { result } }
    }
}
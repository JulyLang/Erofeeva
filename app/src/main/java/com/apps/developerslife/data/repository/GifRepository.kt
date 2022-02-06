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

    override fun getGifPage(category: String, pageNumber: Int): Observable<List<GifModel>> {
        return remoteDataStore.getGifPage(category, pageNumber)
    }
}
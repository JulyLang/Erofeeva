package com.apps.developerslife.data.repository

import com.apps.developerslife.data.net.GifModel
import com.apps.developerslife.data.net.GifService
import io.reactivex.Observable

class GifRemoteDataStore (private val gifService: GifService): IGifRemoteDataStore {
    override fun getRandomGif(): Observable<GifModel> {
        return gifService.getRandomGif()
    }
    override fun getGifPage(category: String, pageNumber: Int): Observable<List<GifModel>> {
        return gifService.getGifPage(category, pageNumber)
    }
}
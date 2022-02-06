package com.apps.developerslife.data.repository

import com.apps.developerslife.data.net.GifModel
import io.reactivex.Observable


interface IGifRemoteDataStore {
    fun getRandomGif() : Observable<GifModel>
    fun getGifPage(category: String, pageNumber: Int): Observable<List<GifModel>>
}
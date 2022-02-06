package com.apps.developerslife.data.repository

import com.apps.developerslife.data.net.GifModel
import io.reactivex.Observable

interface IGifRepository {

    fun getRandomGif(): Observable<GifModel>

    fun observeGifs(): Observable<List<GifModel>>

    fun loadGifPage(category: String, clearPrevious: Boolean): Observable<Boolean>
}
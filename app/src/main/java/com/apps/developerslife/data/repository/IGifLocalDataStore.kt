package com.apps.developerslife.data.repository

import com.apps.developerslife.data.net.GifModel
import io.reactivex.Observable

interface IGifLocalDataStore {

    fun observeGifs(): Observable<List<GifModel>>

    fun getLastPageNumber(): Observable<Int>

    fun saveGifs(clearPrevious: Boolean, gifs: List<GifModel>): Observable<Boolean>

    fun notifyGifs(): Observable<Unit>
}
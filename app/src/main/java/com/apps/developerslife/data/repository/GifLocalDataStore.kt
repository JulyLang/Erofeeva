package com.apps.developerslife.data.repository

import com.apps.developerslife.data.net.GifModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class GifLocalDataStore : IGifLocalDataStore {

    //in-memory storage
    private val gifs: MutableList<GifModel> = mutableListOf()
    private var lastPageNumber = 0

    private val gifsSubject: PublishSubject<List<GifModel>> = PublishSubject.create()

    override fun observeGifs(): Observable<List<GifModel>> {
        return gifsSubject
    }

    override fun getLastPageNumber(): Observable<Int> {
        return Observable.just(lastPageNumber)
    }

    override fun saveGifs(clearPrevious: Boolean, gifs: List<GifModel>): Observable<Boolean> {
        return Observable.create { emitter ->
            if (clearPrevious) {
                this.gifs.clear()
                lastPageNumber = 0
            }
            this.gifs.addAll(gifs)
            lastPageNumber++
            emitter.onNext(true)
            emitter.onComplete()
        }
    }

    override fun notifyGifs(): Observable<Unit> {
        return Observable.create { emitter ->
            gifsSubject.onNext(gifs)
            emitter.onNext(Unit)
            emitter.onComplete()
        }
    }
}
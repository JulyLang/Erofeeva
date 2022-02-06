package com.apps.developerslife.category

import android.util.Log
import com.apps.developerslife.domain.usecase.LoadGifUseCase
import com.apps.developerslife.domain.usecase.LoadGifsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicBoolean

class CategoryPresenter(
    private val loadGifUseCase: LoadGifUseCase,
    private val loadGifsUseCase: LoadGifsUseCase
) : CategoryContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    private var view: CategoryContract.View? = null

    private var currentPosition: Int = 0
    private var gifPageState: GifPageState = GifPageState.LOADING
    private var gifPageData = GifPageData(
        gifs = emptyList(),
        areGifsUpdated = false,
        isForwardButtonEnabled = false,
        isBackButtonEnabled = false
    )

    private val loadingStarted: AtomicBoolean = AtomicBoolean(false)

    override fun subscribe(view: CategoryContract.View?) {
        this.view = view
        view?.renderState(gifPageState, gifPageData)
        startGifsObserving()
        startGifsLoading(clearPrevious = true)
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
        view = null
    }

    override fun onBackButtonPressed() {
        if (!isBackButtonEnabled(currentPosition)) return

        currentPosition--
        view?.scrollToPosition(currentPosition)

        val gifs = gifPageData.gifs
        gifPageData = gifPageData.copy(
            areGifsUpdated = false,
            isForwardButtonEnabled = isForwardButtonEnabled(currentPosition, gifs.size),
            isBackButtonEnabled = isBackButtonEnabled(currentPosition)
        )
        view?.renderState(gifPageState, gifPageData)
    }

    override fun onForwardButtonPressed() {
        val gifs = gifPageData.gifs
        if (!isForwardButtonEnabled(currentPosition, gifs.size)) return

        currentPosition++
        view?.scrollToPosition(currentPosition)

        gifPageData = gifPageData.copy(
            areGifsUpdated = false,
            isForwardButtonEnabled = isForwardButtonEnabled(currentPosition, gifs.size),
            isBackButtonEnabled = isBackButtonEnabled(currentPosition)
        )
        view?.renderState(gifPageState, gifPageData)

        if (gifPageData.gifs.size - currentPosition <= START_LOADING_THRESHOLD) {
            startGifsLoading(clearPrevious = false)
        }
    }

    override fun onRetryButtonPressed() {
        gifPageState = GifPageState.LOADING
        view?.renderState(gifPageState, gifPageData)
        startGifsLoading(clearPrevious = false)
    }

    private fun isForwardButtonEnabled(position: Int, gifsSize: Int): Boolean {
        return position < gifsSize - 1
    }

    private fun isBackButtonEnabled(position: Int): Boolean {
        return position > 0
    }

    private fun startGifsObserving() {
        val observeGifsDisposable = loadGifsUseCase.observeGifs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    gifPageState = GifPageState.SHOW_DATA
                    gifPageData = GifPageData(
                        gifs = it,
                        areGifsUpdated = true,
                        isForwardButtonEnabled = isForwardButtonEnabled(currentPosition, it.size),
                        isBackButtonEnabled = isBackButtonEnabled(currentPosition)
                    )
                    view?.renderState(gifPageState, gifPageData)
                },
                onError = {
                    Log.e(TAG,"Something went wrong", it)
                }
            )
        compositeDisposable.add(observeGifsDisposable)
    }

    private fun startGifsLoading(clearPrevious: Boolean) {
        Log.v(TAG, "startGifsLoading clearPrevious=$clearPrevious")
        if (loadingStarted.compareAndSet(false, true)) {
            val loadGifsDisposable = loadGifsUseCase.loadGifs(
                LoadGifsUseCase.LoadParams("latest", clearPrevious)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { loadingStarted.set(false) }
                .subscribeBy(
                    onNext = { Log.d(TAG, "Gifs was loaded with result=$it") },
                    onError = {
                        Log.e(TAG,"Can\'t load gifs", it)
                        gifPageState = GifPageState.ERROR
                        view?.renderState(gifPageState, gifPageData)
                    }
                )
            compositeDisposable.add(loadGifsDisposable)
        } else {
            Log.v(TAG, "startGifsLoading skipped, reason: already running sync with back-end, clearPrevious=$clearPrevious")
        }
    }

    companion object {
        private const val TAG = "CategoryPresenter"
        private const val START_LOADING_THRESHOLD = 3
    }
}
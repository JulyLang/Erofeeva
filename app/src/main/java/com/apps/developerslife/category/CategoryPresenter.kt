package com.apps.developerslife.category

import android.util.Log
import com.apps.developerslife.domain.usecase.LoadGifUseCase
import com.apps.developerslife.domain.usecase.LoadGifsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

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

    override fun subscribe(view: CategoryContract.View?) {
        this.view = view

        view?.renderState(gifPageState, gifPageData)
        startGifsLoading()
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
    }

    override fun onRetryButtonPressed() {
        gifPageState = GifPageState.LOADING
        view?.renderState(gifPageState, gifPageData)
        startGifsLoading()
    }

    private fun isForwardButtonEnabled(position: Int, gifsSize: Int): Boolean {
        return position < gifsSize - 1
    }

    private fun isBackButtonEnabled(position: Int): Boolean {
        return position > 0
    }

    private fun startGifsLoading() {
        val loadGifsDisposable = loadGifsUseCase.buildObservable(LoadGifsUseCase.Params("latest", 0))
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
                onError = { Log.e("CategoryPresenter","can\'t load gifs", it)
                    gifPageState = GifPageState.ERROR
                    view?.renderState(gifPageState, gifPageData)
                }
            )
        compositeDisposable.add(loadGifsDisposable)
    }
}
package com.apps.developerslife.domain.usecase

import com.apps.developerslife.data.net.GifServiceHolder
import com.apps.developerslife.data.repository.*

object UseCaseHolder {

    private val localDataStore: IGifLocalDataStore = GifLocalDataStore()
    private val remoteDataStore: IGifRemoteDataStore = GifRemoteDataStore(
        GifServiceHolder.service
    )
    private val repository: IGifRepository = GifRepository(
        localDataStore, remoteDataStore
    )

    val loadGifUseCase = LoadGifUseCase(repository)
    val loadGifsUseCase = LoadGifsUseCase(repository)
}
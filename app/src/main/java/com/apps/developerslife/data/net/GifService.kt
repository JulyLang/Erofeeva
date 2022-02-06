package com.apps.developerslife.data.net

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class GifService {

    private val BASE_URL = "https://developerslife.ru"
    private var mRetrofit: Retrofit? = null
    private var gifApi: GifApi? = null

    init {
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        gifApi = mRetrofit!!.create(GifApi::class.java)
    }

    fun getRandomGif(): Observable<GifModel> {
      return gifApi!!.getRandomGif(json = true)
    }

    fun getGifPage(category: String, pageNumber: Int): Observable<List<GifModel>>  {
        return gifApi!!.getGifPage(category, pageNumber, json = true).map { it.gifModels }
    }

}
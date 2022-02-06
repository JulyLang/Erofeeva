package com.apps.developerslife.data.net

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GifApi {
    //<раздел>/<номер страницы>?json=true
    @GET("/{category}/{pageNumber}")
    fun getGifPage(
        @Path("category") category: String,
        @Path("pageNumber") pageNumber: Int,
        @Query("json") json: Boolean
    ): Observable<GifPageModel>

    @GET("/random")
    fun getRandomGif(
        @Query("json") json: Boolean
    ): Observable<GifModel>

}
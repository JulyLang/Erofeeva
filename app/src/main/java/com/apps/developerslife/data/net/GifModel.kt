package com.apps.developerslife.data.net

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class GifModel {
    @SerializedName("gifURL")
    @Expose
    val gifURL: String? = null

    @SerializedName("description")
    @Expose
    val gifDescription: String? = null
}
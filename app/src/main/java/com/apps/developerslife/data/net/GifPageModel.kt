package com.apps.developerslife.data.net

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GifPageModel {
    @SerializedName("result")
    @Expose
    val gifModels: List<GifModel> = emptyList()
}
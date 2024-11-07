package ru.nyxsed.postscan.data.models.response


import com.google.gson.annotations.SerializedName

data class SizeResponse(
    @SerializedName("height")
    val height: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)
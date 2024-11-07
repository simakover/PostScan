package ru.nyxsed.postscan.data.models.response


import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("sizes")
    val sizes: List<SizeResponse>,
)
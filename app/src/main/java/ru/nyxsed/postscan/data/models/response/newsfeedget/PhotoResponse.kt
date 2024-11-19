package ru.nyxsed.postscan.data.models.response.newsfeedget


import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("sizes")
    val sizes: List<SizeResponse>,
)
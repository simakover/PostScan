package ru.nyxsed.postscan.data.models.response.newsfeedget


import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("image")
    val image: List<ImageResponse>,
)
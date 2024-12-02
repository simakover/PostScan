package ru.nyxsed.postscan.data.models.response.newsfeedget


import com.google.gson.annotations.SerializedName

data class WallGetResponse(
    @SerializedName("response")
    val content: WallGetContentResponse?,
)
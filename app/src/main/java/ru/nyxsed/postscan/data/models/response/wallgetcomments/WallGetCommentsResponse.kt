package ru.nyxsed.postscan.data.models.response.wallgetcomments


import com.google.gson.annotations.SerializedName

data class WallGetCommentsResponse(
    @SerializedName("response")
    val content: WallGetCommentsContentResponse?
)
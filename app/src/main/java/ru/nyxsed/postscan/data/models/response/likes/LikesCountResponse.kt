package ru.nyxsed.postscan.data.models.response.likes

import com.google.gson.annotations.SerializedName

data class LikesCountResponse(
    @SerializedName("response")
    val likes : LikesCountContentResponse,
)

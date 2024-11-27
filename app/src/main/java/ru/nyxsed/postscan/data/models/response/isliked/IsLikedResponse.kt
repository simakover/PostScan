package ru.nyxsed.postscan.data.models.response.isliked


import com.google.gson.annotations.SerializedName

data class IsLikedResponse(
    @SerializedName("response")
    val response: IsLikedContentResponse?
)
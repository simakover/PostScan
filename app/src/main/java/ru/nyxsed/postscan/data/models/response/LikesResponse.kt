package ru.nyxsed.postscan.data.models.response


import com.google.gson.annotations.SerializedName

data class LikesResponse(
    @SerializedName("user_likes")
    val userLikes: Int
)
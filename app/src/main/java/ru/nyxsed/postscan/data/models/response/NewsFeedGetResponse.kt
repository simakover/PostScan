package ru.nyxsed.postscan.data.models.response


import com.google.gson.annotations.SerializedName

data class NewsFeedGetResponse(
    @SerializedName("response")
    val content: NewsfeedGetContentResponse,
)
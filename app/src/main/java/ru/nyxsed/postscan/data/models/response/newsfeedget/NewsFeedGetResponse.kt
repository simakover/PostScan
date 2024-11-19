package ru.nyxsed.postscan.data.models.response.newsfeedget


import com.google.gson.annotations.SerializedName

data class NewsFeedGetResponse(
    @SerializedName("response")
    val content: NewsfeedGetContentResponse,
)
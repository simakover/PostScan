package ru.nyxsed.postscan.data.models.response.wallgetcomments


import com.google.gson.annotations.SerializedName

data class ThreadResponse(
    @SerializedName("items")
    val items: List<ItemResponse>
)
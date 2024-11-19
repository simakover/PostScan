package ru.nyxsed.postscan.data.models.response.newsfeedget


import com.google.gson.annotations.SerializedName

data class NewsfeedGetContentResponse(
    @SerializedName("groups")
    val groups: List<GroupResponse>,
    @SerializedName("items")
    val items: List<ItemResponse>,
    @SerializedName("next_from")
    val nextFrom: String?,
)
package ru.nyxsed.postscan.data.models.response.groupsgetbyid


import com.google.gson.annotations.SerializedName

data class GroupResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("photo_50")
    val photo50: String,
    @SerializedName("screen_name")
    val screenName: String
)
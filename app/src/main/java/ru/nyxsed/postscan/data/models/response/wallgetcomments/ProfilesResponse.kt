package ru.nyxsed.postscan.data.models.response.wallgetcomments

import com.google.gson.annotations.SerializedName


data class ProfilesResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("photo_50")
    val photo50: String,

)
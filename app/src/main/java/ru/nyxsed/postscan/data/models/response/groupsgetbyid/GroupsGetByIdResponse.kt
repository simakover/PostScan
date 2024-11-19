package ru.nyxsed.postscan.data.models.response.groupsgetbyid


import com.google.gson.annotations.SerializedName

data class GroupsGetByIdResponse(
    @SerializedName("response")
    val response: GroupsGetByIdContentResponse
)
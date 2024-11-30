package ru.nyxsed.postscan.data.models.response.groupsget


import com.google.gson.annotations.SerializedName

data class GroupsGetResponse(
    @SerializedName("response")
    val response: GroupsGetContentResponse?,
)
package ru.nyxsed.postscan.data.models.response.groupsget


import com.google.gson.annotations.SerializedName

data class GroupsGetContentResponse(
    @SerializedName("groups")
    val groups: List<GroupResponse>?,
    @SerializedName("items")
    val items: List<GroupResponse>?,
)
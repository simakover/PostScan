package ru.nyxsed.postscan.data.models.response.groupsgetbyid


import com.google.gson.annotations.SerializedName

data class GroupsGetByIdContentResponse(
    @SerializedName("groups")
    val groups: List<GroupResponse>
)
package ru.nyxsed.postscan.data.models.response.newsfeedget


import com.google.gson.annotations.SerializedName

data class ItemCopyResponse(
    @SerializedName("attachments")
    val attachments: List<AttachmentResponse>?,
    @SerializedName("date")
    val date: Long,
    @SerializedName("id")
    val id: Long,
    @SerializedName("likes")
    val likes: LikesResponse,
    @SerializedName("owner_id")
    val ownerId: Long,
    @SerializedName("post_id")
    val postId: Long,
    @SerializedName("text")
    val text: String
)
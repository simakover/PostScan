package ru.nyxsed.postscan.data.models.response.newsfeedget


import com.google.gson.annotations.SerializedName

data class AttachmentResponse(
    @SerializedName("type")
    val type: String,
    @SerializedName("photo")
    val photo: PhotoResponse?,
    @SerializedName("video")
    val video: VideoResponse?,
)
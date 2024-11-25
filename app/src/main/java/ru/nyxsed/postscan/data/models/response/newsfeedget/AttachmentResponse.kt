package ru.nyxsed.postscan.data.models.response.newsfeedget


import com.google.gson.annotations.SerializedName

data class AttachmentResponse(
    @SerializedName("photo")
    val photo: PhotoResponse?,
)
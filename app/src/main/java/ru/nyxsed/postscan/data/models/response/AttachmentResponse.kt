package ru.nyxsed.postscan.data.models.response


import com.google.gson.annotations.SerializedName

data class AttachmentResponse(
    @SerializedName("photo")
    val photo: PhotoResponse,
)
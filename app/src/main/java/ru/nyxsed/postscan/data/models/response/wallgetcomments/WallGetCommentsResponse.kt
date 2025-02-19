package ru.nyxsed.postscan.data.models.response.wallgetcomments


import com.google.gson.annotations.SerializedName
import ru.nyxsed.postscan.data.models.response.ErrorResponse

data class WallGetCommentsResponse(
    @SerializedName("response")
    val content: WallGetCommentsContentResponse?,
    @SerializedName("error")
    val error: ErrorResponse?,
)
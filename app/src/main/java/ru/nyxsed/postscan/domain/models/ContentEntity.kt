package ru.nyxsed.postscan.domain.models

data class ContentEntity(
    val contentId: Long,
    val ownerId: Long,
    val type: String,
    var isLiked : Boolean = false,
    val urlSmall: String,
    val urlMedium: String,
    val urlBig: String,
)
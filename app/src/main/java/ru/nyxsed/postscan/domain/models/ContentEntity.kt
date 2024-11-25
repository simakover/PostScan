package ru.nyxsed.postscan.domain.models

data class ContentEntity(
    val contentId: Long,
    val ownerId: Long,
    val type: String,
    val urlSmall: String,
    val urlMedium: String,
    val urlBig: String,
)

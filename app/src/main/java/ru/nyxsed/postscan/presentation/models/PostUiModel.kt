package ru.nyxsed.postscan.presentation.models

data class PostUiModel(
    val id: Long,
    val ownerId: Long,
    val ownerName: String,
    val ownerImageUrl: String,
    val publicationDate: String,
    val contentText: String,
    val contentImageUrl: String?,
    val isLiked: Boolean,
)

package ru.nyxsed.postscan.domain.models

data class CommentEntity(
    val commentId: Long,
    val ownerId: Long,
    val ownerName: String,
    val ownerImageUrl: String,
    val publicationDate: Long,
    val contentText: String,
    val content: List<ContentEntity>,
    val parentStack: Long?,
)

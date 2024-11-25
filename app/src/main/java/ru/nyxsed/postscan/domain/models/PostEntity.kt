package ru.nyxsed.postscan.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val postId : Long,
    val ownerId: Long,
    val ownerName: String,
    val ownerImageUrl: String,
    val publicationDate: Long,
    val contentText: String,
    val contentImageUrl: List<String>,
    val contentVideoUrl: List<String>,
    val isLiked: Boolean,
)

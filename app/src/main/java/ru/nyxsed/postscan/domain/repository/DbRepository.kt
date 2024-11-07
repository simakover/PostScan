package ru.nyxsed.postscan.domain.repository

import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.domain.models.PostEntity

interface DbRepository {
    suspend fun getAllPosts() : List<PostEntity>

    suspend fun addPost(postEntity: PostEntity)
}
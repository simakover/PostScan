package ru.nyxsed.postscan.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ru.nyxsed.postscan.domain.models.PostEntity

interface DbRepository {
    fun getAllPosts(): StateFlow<List<PostEntity>>

    suspend fun addPost(postEntity: PostEntity)
}
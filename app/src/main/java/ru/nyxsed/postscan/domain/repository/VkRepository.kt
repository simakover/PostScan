package ru.nyxsed.postscan.domain.repository

import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.domain.models.PostEntity

interface VkRepository {
    suspend fun getPostsForGroup(group : GroupEntity) : List<PostEntity>
}
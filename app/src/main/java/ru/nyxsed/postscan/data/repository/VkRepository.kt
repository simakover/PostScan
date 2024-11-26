package ru.nyxsed.postscan.data.repository

import com.vk.api.sdk.VKKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.delay
import ru.nyxsed.postscan.data.mapper.VkMapper
import ru.nyxsed.postscan.data.network.ApiService
import ru.nyxsed.postscan.domain.models.ContentEntity
import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.domain.models.PostEntity

class VkRepository(
    private val apiService: ApiService,
    private val mapper: VkMapper,
    private val storage: VKKeyValueStorage,
) {

    private val token
        get() = VKAccessToken.restore(storage)

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw IllegalStateException("Token is null")
    }

    suspend fun getPostsForGroup(groupEntity: GroupEntity): List<PostEntity> {
        var startFrom: String? = ""
        val posts = mutableListOf<PostEntity>()
        val lastFetchDate = groupEntity.lastFetchDate

        while (startFrom != null) {
            val response = apiService.newsfeedGet(
                token = getAccessToken(),
                sourceId = (groupEntity.groupId?.times(-1)).toString(),
                startFrom = startFrom,
                startTime = (lastFetchDate / 1000).toString()
            )

            val responsePosts = mapper.mapNewsFeedResponseToPosts(response)
            responsePosts.forEach {
                posts.add(it)
            }

            startFrom = response.content?.nextFrom
            delay(350)
        }
        return posts.toList()
    }

    suspend fun groupsGetById(groupId: String): GroupEntity {
        val response = apiService.groupsGetById(
            token = getAccessToken(),
            groupId = groupId
        )

        return mapper.mapGroupsGetByIdResponseToGroup(response)
    }

    // post
    suspend fun changeLikeStatus(post: PostEntity) {
        if (!post.isLiked) {
            apiService.addLike(
                token = getAccessToken(),
                ownerId = post.ownerId,
                itemId = post.postId,
                type = "post"
            )
        } else {
            apiService.deleteLike(
                token = getAccessToken(),
                ownerId = post.ownerId,
                itemId = post.postId,
                type = "post"
            )
        }
    }

    // content
    suspend fun changeLikeStatus(contentEntity: ContentEntity) {
        if (!contentEntity.isLiked) {
            apiService.addLike(
                token = getAccessToken(),
                ownerId = contentEntity.ownerId,
                itemId = contentEntity.contentId,
                type = if (contentEntity.type == "album") "photo" else contentEntity.type
            )
        } else {
            apiService.deleteLike(
                token = getAccessToken(),
                ownerId = contentEntity.ownerId,
                itemId = contentEntity.contentId,
                type = if (contentEntity.type == "album") "photo" else contentEntity.type
            )
        }
    }

    suspend fun checkLikeStatus(contentEntity: ContentEntity): Boolean {
        val response = apiService.isLiked(
            token = getAccessToken(),
            ownerId = contentEntity.ownerId,
            itemId = contentEntity.contentId,
            type = if (contentEntity.type == "album") "photo" else contentEntity.type
        )
        return response.response.liked == 1
    }
}
package ru.nyxsed.postscan.data.repository

import com.vk.api.sdk.VKKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import ru.nyxsed.postscan.data.mapper.VkMapper
import ru.nyxsed.postscan.data.models.entity.ContentEntity
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.data.models.entity.PostEntity
import ru.nyxsed.postscan.data.network.ApiService
import ru.nyxsed.postscan.util.DataStoreInteraction
import ru.nyxsed.postscan.util.DataStoreInteraction.Companion.NOT_LOAD_LIKED_POSTS

class VkRepository(
    private val apiService: ApiService,
    private val mapper: VkMapper,
    private val storage: VKKeyValueStorage,
    private val dataStoreInteraction: DataStoreInteraction,
) {
    val scope = CoroutineScope(Dispatchers.Default)

    private val token
        get() = VKAccessToken.restore(storage)

    private fun getAccessToken(): String {
        return token?.accessToken ?: throw IllegalStateException("Token is null")
    }

    // groups
    suspend fun groupsGetById(groupId: String): List<GroupEntity> {
        val response = apiService.groupsGetById(
            token = getAccessToken(),
            groupId = groupId
        )
        return mapper.mapGroupsGetResponseToGroups(response)
    }

    fun getGroupsStateFlow() =
        flow {
            val response = apiService.groupsGet(
                token = getAccessToken()
            )
            emit(mapper.mapGroupsGetResponseToGroups(response))
        }
            .retry(2)
            .stateIn(
                scope = scope,
                started = SharingStarted.Lazily,
                initialValue = listOf()
            )

    // post
    suspend fun getPostsForGroup(groupEntity: GroupEntity): List<PostEntity> {
        var offset : Int = 0
        val posts = mutableListOf<PostEntity>()
        val lastFetchDate = groupEntity.lastFetchDate
        val notLoadLikedPosts = dataStoreInteraction.getSettingBooleanFromDataStore(NOT_LOAD_LIKED_POSTS)

        while (true) {
            val response = apiService.wallGet(
                token = getAccessToken(),
                ownerId = (groupEntity.groupId.times(-1)).toString(),
                offset = offset
            )

            if (response.content == null || response.content.items.isNullOrEmpty()) break

            val responsePosts = mapper.mapWallGetResponseToPosts(response)
            responsePosts
                .filter {
                    it.publicationDate > lastFetchDate
                }
                .filter {
                    if (notLoadLikedPosts) {
                        it.isLiked == false
                    } else {
                        true
                    }
                }
                .forEach {
                    posts.add(it)
                }

            if (responsePosts.last().publicationDate <= lastFetchDate) break

            offset += 100
            delay(350)
        }
        return posts.toList()
    }

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
        return response.response?.liked == 1
    }

    // comments
    fun getCommentsStateFlow(post: PostEntity) =
        flow {
            val response = apiService.wallGetComments(
                token = getAccessToken(),
                ownerId = post.ownerId,
                postId = post.postId
            )
            emit(mapper.mapWallGetCommentsResponseToComments(response))
        }
            .retry(2)
            .stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = listOf()
            )
}
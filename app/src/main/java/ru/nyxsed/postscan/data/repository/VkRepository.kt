package ru.nyxsed.postscan.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.vk.api.sdk.VKKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.nyxsed.postscan.data.mapper.VkMapper
import ru.nyxsed.postscan.data.network.ApiService
import ru.nyxsed.postscan.domain.models.CommentEntity
import ru.nyxsed.postscan.domain.models.ContentEntity
import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.util.Constants.NOT_LOAD_LIKED_POSTS
import ru.nyxsed.postscan.util.Constants.getSettingFromDataStore

class VkRepository(
    private val apiService: ApiService,
    private val mapper: VkMapper,
    private val storage: VKKeyValueStorage,
    private val dataStore: DataStore<Preferences>,
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
        val notLoadLikedPosts = getSettingFromDataStore(dataStore, NOT_LOAD_LIKED_POSTS) == "1"

        while (startFrom != null) {
            val response = apiService.newsfeedGet(
                token = getAccessToken(),
                sourceId = (groupEntity.groupId?.times(-1)).toString(),
                startFrom = startFrom,
                startTime = (lastFetchDate / 1000).toString()
            )

            val responsePosts = mapper.mapNewsFeedResponseToPosts(response)
            responsePosts
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
        return response.response?.liked == 1
    }

    // comments
    val scope = CoroutineScope(Dispatchers.Default)
    fun getCommentsStateFlow(post: PostEntity) =
        flow {
            val response = apiService.wallGetComments(
                token = getAccessToken(),
                ownerId = post.ownerId,
                postId = post.postId
            )
            emit(mapper.mapWallGetCommentsResponseToComments(response))
        }.stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = listOf()
        )

    suspend fun getComments(post: PostEntity): List<CommentEntity> {
        val response = apiService.wallGetComments(
            token = getAccessToken(),
            ownerId = post.ownerId,
            postId = post.postId
        )
        return mapper.mapWallGetCommentsResponseToComments(response)
    }
}
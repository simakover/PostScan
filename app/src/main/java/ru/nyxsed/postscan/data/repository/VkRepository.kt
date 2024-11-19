package ru.nyxsed.postscan.data.repository

import com.vk.api.sdk.VKKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.delay
import ru.nyxsed.postscan.data.mapper.VkMapper
import ru.nyxsed.postscan.data.network.ApiService
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
                sourceId = groupEntity.groupId.toString(),
                startFrom = startFrom,
                startTime = (lastFetchDate/1000).toString()
            )

            val responsePosts = mapper.mapNewsFeedResponseToPosts(response)
            responsePosts.forEach {
                posts.add(it)
            }

            startFrom = response.content.nextFrom
            delay(500)
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
}
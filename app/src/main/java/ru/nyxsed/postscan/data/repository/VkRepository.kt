package ru.nyxsed.postscan.data.repository

import kotlinx.coroutines.delay
import ru.nyxsed.postscan.data.mapper.VkMapper
import ru.nyxsed.postscan.data.network.ApiService
import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.domain.models.PostEntity

class VkRepository(
    private val apiService: ApiService,
    private val mapper: VkMapper,
) {

    private val token =
        "vk1.a.K7RrrcobHBv7Q0BT6npti4qgocTStsXH-nl9OpeOk-Cab09IIC0Jw6BHcrT--YCu4uugG4Q-r05giHrjWWwgGoL-mcDRnZJ3yMERN24wVuSR2ZToUwYq4_XMljoNDzV9TXNmq-GZ7ReYXZvY9Xi2aEXi9GxiJYDTWKnCJ7tuY2ahyY9py8vg6VNgeytB464b26mYRUxBXUbgSrW0CDZw6A"

    private fun getAccessToken(): String {
        return token
    }

    suspend fun getPostsForGroup(groupEntity: GroupEntity): List<PostEntity> {
        var startFrom: String? = ""
        val posts = mutableListOf<PostEntity>()
        val lastFetchDate = groupEntity.lastFetchDate

        while (startFrom != null) {
            val response = apiService.newsfeedGet(
                token = getAccessToken(),
                sourceId = groupEntity.groupId,
                startFrom = startFrom,
                startTime = lastFetchDate
            )

            val responsePosts = mapper.mapResponseToPosts(response)
            responsePosts.forEach {
                posts.add(it)
            }

            startFrom = response.content.nextFrom
            delay(500)
        }

        return posts.toList()
    }
}
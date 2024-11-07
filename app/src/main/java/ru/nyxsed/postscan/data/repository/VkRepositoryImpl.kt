package ru.nyxsed.postscan.data.repository

import ru.nyxsed.postscan.data.mapper.VkMapper
import ru.nyxsed.postscan.data.network.ApiFactory
import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.domain.repository.VkRepository

class VkRepositoryImpl : VkRepository {
    private val apiService = ApiFactory.apiService
    private val mapper = VkMapper()

    private val token =
        "vk1.a.LZda7WuaUDQk77NP9mOZnMsaEupG3TveMcgx0jxPUImAuMX9DtQBvDY0gaatq-WJK-7L51KLN_aVYBy-h2DKPSgOhNCMd-YHZSRwmnIhuBHq9_rbCN42xf6Ywj6TgcOTIIzK_rjM2aQ-aUfmsDpG_gr9BzrKp6FKfk1QUa2wJ8-qAHl1W7qkuFZg7BloZ6NoUnjfVh2qvJ3wLEarZMudew"

    private fun getAccessToken(): String {
        return token
    }

    override suspend fun getPostsForGroup(groupEntity: GroupEntity): List<PostEntity> {
        val response = apiService.newsfeedGet(
            token = getAccessToken(),
            ownerId = groupEntity.id
        )
        val posts = mapper.mapResponseToPosts(response)
        return posts
    }
}
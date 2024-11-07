package ru.nyxsed.postscan.data.mapper

import ru.nyxsed.postscan.data.models.response.NewsFeedGetResponse
import ru.nyxsed.postscan.domain.models.PostEntity
import kotlin.math.absoluteValue

class VkMapper {
    fun mapResponseToPosts(response: NewsFeedGetResponse): List<PostEntity> {
        val result = mutableListOf<PostEntity>()

        val posts = response.content.items
        val groups = response.content.groups

        for (post in posts) {
            val group = groups.find { it.id == post.ownerId.absoluteValue } ?: continue

            val postEnt = PostEntity(
                postId = post.id,
                ownerId = post.ownerId,
                ownerName = group.name,
                ownerImageUrl = group.photo50,
                publicationDate = post.date * 1000,
                contentText = post.text,
                contentImageUrl = post.attachments.firstOrNull()?.photo?.sizes?.find { it.type == "p" }?.url,
                isLiked = post.likes.userLikes > 0
            )
            result.add(postEnt)
        }
        return result
    }
}
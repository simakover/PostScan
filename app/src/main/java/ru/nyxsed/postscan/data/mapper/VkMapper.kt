package ru.nyxsed.postscan.data.mapper

import ru.nyxsed.postscan.data.models.response.groupsgetbyid.GroupsGetByIdResponse
import ru.nyxsed.postscan.data.models.response.newsfeedget.NewsFeedGetResponse
import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.domain.models.PostEntity
import kotlin.math.absoluteValue

class VkMapper {
    fun mapNewsFeedResponseToPosts(response: NewsFeedGetResponse): List<PostEntity> {
        val result = mutableListOf<PostEntity>()

        val posts = response.content?.items
        val groups = response.content?.groups

        posts?.let { posts ->
            for (post in posts) {
                val group = groups?.find { it.id == post.ownerId.absoluteValue } ?: continue


                val images = post.attachments?.map { attachment ->
                    attachment.photo.sizes.find { it.type == "p" }?.url ?: ""
                } ?: emptyList()

                val postEnt = PostEntity(
                    postId = post.id,
                    ownerId = post.ownerId,
                    ownerName = group.name,
                    ownerImageUrl = group.photo50,
                    publicationDate = post.date * 1000,
                    contentText = post.text,
                    contentImageUrl = images,
                    isLiked = post.likes.userLikes > 0
                )
                result.add(postEnt)
            }
        }

        return result
    }

    fun mapGroupsGetByIdResponseToGroup(response: GroupsGetByIdResponse): GroupEntity {
        val group = response.response?.groups?.first()

        return GroupEntity(
            groupId = group?.id ?: 0,
            name = group?.name ?: "",
            screenName = group?.screenName ?: "",
            avatarUrl = group?.photo50 ?: "",
            lastFetchDate = System.currentTimeMillis()
        )
    }
}
package ru.nyxsed.postscan.data.mapper

import ru.nyxsed.postscan.data.models.response.groupsgetbyid.GroupsGetByIdResponse
import ru.nyxsed.postscan.data.models.response.newsfeedget.AttachmentResponse
import ru.nyxsed.postscan.data.models.response.newsfeedget.NewsFeedGetResponse
import ru.nyxsed.postscan.domain.models.ContentEntity
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

                val listContentEntity: MutableList<ContentEntity> = mutableListOf()

                post.attachments?.forEach { attachment ->
                    val content = getContentEntity(attachment)
                    if (content != null) {
                        listContentEntity.add(content)
                    }
                }

                val copyHistory = post.copyHistory
                copyHistory?.forEach { repost ->
                    val listRepostContentEntity: MutableList<ContentEntity> = mutableListOf()

                    repost.attachments?.forEach { attachment ->
                        val content = getContentEntity(attachment)
                        if (content != null) {
                            listRepostContentEntity.add(content)
                        }
                    }
                    listContentEntity.addAll(listRepostContentEntity)
                }

                val postEnt = PostEntity(
                    postId = post.id,
                    ownerId = post.ownerId,
                    ownerName = group.name,
                    ownerImageUrl = group.photo50,
                    publicationDate = post.date * 1000,
                    contentText = post.text,
                    isLiked = post.likes.userLikes > 0,
                    content = listContentEntity,
                    haveReposts = if (copyHistory.isNullOrEmpty()) false else true
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

    private fun getContentEntity(attachment: AttachmentResponse): ContentEntity? {
        var contentId: Long = 0
        var ownerId: Long = 0
        var type: String = ""
        var isLiked: Boolean = false
        var urlSmall: String = ""
        var urlMedium: String = ""
        var urlBig: String = ""
        var title: String = ""

        when (attachment.type) {
            "photo" -> {
                val attachmentPhoto = attachment.photo
                attachmentPhoto?.let { photo ->
                    contentId = photo.id
                    ownerId = photo.ownerId
                    type = attachment.type
                    urlSmall = photo.sizes.find { it.type == "s" }?.url ?: ""
                    urlMedium = photo.sizes.find { it.type == "p" }?.url ?: ""
                    urlBig = photo.sizes.find { it.type == "w" }?.url ?: ""
                    contentId = photo.id
                }
            }

            "video" -> {
                val attachmentVideo = attachment.video
                attachmentVideo?.let { video ->
                    contentId = video.id
                    ownerId = video.ownerId
                    type = attachment.type
                    urlSmall = video.image.find { it.url.takeLast(5) == "vid_s" }?.url ?: ""
                    urlMedium = video.image.find { it.url.takeLast(5) == "vid_l" }?.url ?: ""
                    urlBig = video.image.find { it.url.takeLast(5) == "vid_x" }?.url ?: ""
                }
            }

            "album" -> {
                val attachmentAlbum = attachment.album
                attachmentAlbum?.let { album ->
                    contentId = album.thumb.id
                    ownerId = album.thumb.ownerId
                    type = attachment.type
                    urlSmall = album.thumb.sizes.find { it.type == "s" }?.url ?: ""
                    urlMedium = album.thumb.sizes.find { it.type == "p" }?.url ?: ""
                    urlBig = album.thumb.sizes.find { it.type == "w" }?.url ?: ""
                    title = album.title
                }
            }
        }

        return if (contentId != 0L) {
            ContentEntity(
                contentId = contentId,
                ownerId = ownerId,
                type = type,
                isLiked = isLiked,
                urlSmall = urlSmall,
                urlMedium = urlMedium,
                urlBig = urlBig,
                title = title
            )
        } else {
            null
        }
    }
}
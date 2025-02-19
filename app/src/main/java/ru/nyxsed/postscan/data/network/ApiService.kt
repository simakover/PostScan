package ru.nyxsed.postscan.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.nyxsed.postscan.data.models.response.groupsget.GroupsGetResponse
import ru.nyxsed.postscan.data.models.response.isliked.IsLikedResponse
import ru.nyxsed.postscan.data.models.response.likes.LikesCountResponse
import ru.nyxsed.postscan.data.models.response.newsfeedget.NewsFeedGetResponse
import ru.nyxsed.postscan.data.models.response.wallgetcomments.WallGetCommentsResponse
import ru.nyxsed.postscan.util.Constants.VK_API_VERSION

interface ApiService {
    @GET("newsfeed.get?v=$VK_API_VERSION&filters=post")
    suspend fun newsfeedGet(
        @Query("access_token") token: String,
        @Query("source_ids") sourceId: String,
        @Query("start_from") startFrom: String,
        @Query("start_time") startTime: String,
    ): NewsFeedGetResponse

    @GET("groups.get?v=$VK_API_VERSION&extended=1&count=1000")
    suspend fun groupsGet(
        @Query("access_token") token: String,
    ): GroupsGetResponse

    @GET("groups.getById?v=$VK_API_VERSION")
    suspend fun groupsGetById(
        @Query("access_token") token: String,
        @Query("group_id") groupId: String,
    ): GroupsGetResponse

    @GET("likes.add?v=$VK_API_VERSION")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long,
        @Query("type") type: String,
    ): LikesCountResponse

    @GET("likes.delete?v=$VK_API_VERSION")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long,
        @Query("type") type: String,
    ): LikesCountResponse

    @GET("likes.isLiked?v=$VK_API_VERSION")
    suspend fun isLiked(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long,
        @Query("type") type: String,
    ): IsLikedResponse

    @GET("wall.getComments?v=$VK_API_VERSION&count=100&thread_items_count=10&extended=1&fields=photo_50")
    suspend fun wallGetComments(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("post_id") postId: Long,
    ): WallGetCommentsResponse
}
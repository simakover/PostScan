package ru.nyxsed.postscan.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.nyxsed.postscan.util.Constants.VK_API_VERSION
import ru.nyxsed.postscan.data.models.response.groupsgetbyid.GroupsGetByIdResponse
import ru.nyxsed.postscan.data.models.response.likes.LikesCountResponse
import ru.nyxsed.postscan.data.models.response.newsfeedget.NewsFeedGetResponse

interface ApiService {
    @GET("newsfeed.get?v=$VK_API_VERSION&filters=post")
    suspend fun newsfeedGet(
        @Query("access_token") token: String,
        @Query("source_ids") sourceId: String,
        @Query("start_from") startFrom: String,
        @Query("start_time") startTime: String,
    ): NewsFeedGetResponse

    @GET("groups.getById?v=$VK_API_VERSION")
    suspend fun groupsGetById(
        @Query("access_token") token: String,
        @Query("group_id") groupId: String,
    ): GroupsGetByIdResponse

    @GET("likes.add?v=$VK_API_VERSION&type=post")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
    ): LikesCountResponse

    @GET("likes.delete?v=$VK_API_VERSION&type=post")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
    ): LikesCountResponse
}
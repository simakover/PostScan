package ru.nyxsed.postscan.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.nyxsed.postscan.Util.Constants.VK_API_VERSION
import ru.nyxsed.postscan.data.models.response.NewsFeedGetResponse

interface ApiService {
    @GET("newsfeed.get?v=$VK_API_VERSION&count=5&filters=post")
    suspend fun newsfeedGet(
        @Query("access_token") token: String,
        @Query("source_ids") sourceId: String,
        @Query("start_from") startFrom: String,
        @Query("start_time") startTime: String,
    ): NewsFeedGetResponse
}
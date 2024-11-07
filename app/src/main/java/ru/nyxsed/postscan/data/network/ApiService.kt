package ru.nyxsed.postscan.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.nyxsed.postscan.Util.Constants.VK_API_VERSION
import ru.nyxsed.postscan.data.models.response.NewsFeedGetResponse

interface ApiService {
    @GET("newsfeed.get?v=$VK_API_VERSION&count=2")
    suspend fun newsfeedGet(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: String,
    ): NewsFeedGetResponse
}
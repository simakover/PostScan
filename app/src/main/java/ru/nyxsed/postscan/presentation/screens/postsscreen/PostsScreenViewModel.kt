package ru.nyxsed.postscan.presentation.screens.postsscreen

import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.data.models.entity.PostEntity
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.util.Constants.MANGA_SEARCH_ACTION
import ru.nyxsed.postscan.util.Constants.VK_URL
import ru.nyxsed.postscan.util.Constants.VK_WALL_URL
import ru.nyxsed.postscan.util.DataStoreInteraction
import ru.nyxsed.postscan.util.NotificationHelper.completeNotification
import ru.nyxsed.postscan.util.NotificationHelper.initNotification
import ru.nyxsed.postscan.util.NotificationHelper.updateProgress

class PostsScreenViewModel(
    private val dbRepository: DbRepository,
    private val vkRepository: VkRepository,
    private val dataStoreInteraction: DataStoreInteraction,
) : ViewModel() {
    val posts = dbRepository.getAllPosts()
    val groups = dbRepository.getAllGroups()

    fun loadPosts(context: Context) {
        viewModelScope.launch {
            initNotification(context)
            groups.value.forEachIndexed { index, group ->
                val postEntities = vkRepository.getPostsForGroup(group)
                postEntities.forEach { post ->
                    dbRepository.addPost(post)
                }

                val updatedGroup = group.copy(
                    lastFetchDate = (System.currentTimeMillis())
                )
                dbRepository.updateGroup(updatedGroup)

                val percentage = (index + 1) * 100 / groups.value.size
                updateProgress(context, percentage)
            }
            completeNotification(context)
        }
    }

    fun addPost(post: PostEntity) {
        viewModelScope.launch {
            dbRepository.addPost(post)
        }
    }

    fun deletePost(post: PostEntity) {
        viewModelScope.launch {
            dbRepository.deletePost(post)
        }
    }

    suspend fun changeLikeStatus(post: PostEntity): PostEntity {
        vkRepository.changeLikeStatus(post)
        val changedPost = post.copy(isLiked = !post.isLiked)
        dbRepository.updatePost(changedPost)
        return changedPost
    }

    fun openPostUri(uriHandler: UriHandler, post: PostEntity) {
        uriHandler.openUri("${VK_WALL_URL}${post.ownerId}_${post.postId}")
    }

    fun openGroupUri(uriHandler: UriHandler, group: GroupEntity) {
        uriHandler.openUri("${VK_URL}${group.screenName}")
    }

    fun mihonIntent(query: String): Intent {
        return Intent().apply {
            action = MANGA_SEARCH_ACTION
            putExtra("query", query)
        }
    }

    suspend fun getSettingBoolean(key: String): Boolean {
        return dataStoreInteraction.getSettingBooleanFromDataStore(key)
    }
}
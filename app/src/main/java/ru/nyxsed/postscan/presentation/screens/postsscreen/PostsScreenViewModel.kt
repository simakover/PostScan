package ru.nyxsed.postscan.presentation.screens.postsscreen

import android.content.Intent
import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.util.Constants.MANGA_SEARCH_ACTION
import ru.nyxsed.postscan.util.Constants.VK_WALL_URL

class PostsScreenViewModel(
    private val dbRepository: DbRepository,
    private val vkRepository: VkRepository,
) : ViewModel() {
    val posts = dbRepository.getAllPosts()
    val groups = dbRepository.getAllGroups()

    fun loadPosts() {
        viewModelScope.launch {
            groups.value.forEach { group ->
                val posts = vkRepository.getPostsForGroup(group)
                val updatedGroup = group.copy(
                    lastFetchDate = (System.currentTimeMillis())
                )
                dbRepository.updateGroup(updatedGroup)
                posts.forEach { post ->
                    dbRepository.addPost(post)
                }
            }
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

    fun changeLikeStatus(post: PostEntity) {
        viewModelScope.launch {
            vkRepository.changeLikeStatus(post)
            val changedPost = post.copy(isLiked = !post.isLiked)
            dbRepository.updatePost(changedPost)
        }
    }

    fun openPostUri(uriHandler: UriHandler, post: PostEntity) {
        uriHandler.openUri("${VK_WALL_URL}${post.ownerId}_${post.postId}")
    }

    fun mihonIntent(query: String): Intent {
        return Intent().apply {
            action = MANGA_SEARCH_ACTION
            putExtra("query", query)
        }
    }
}
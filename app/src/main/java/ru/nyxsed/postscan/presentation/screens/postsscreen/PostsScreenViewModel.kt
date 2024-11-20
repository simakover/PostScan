package ru.nyxsed.postscan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.domain.models.PostEntity

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

    fun deletePost(post: PostEntity) {
        viewModelScope.launch {
            dbRepository.deletePost(post)
        }
    }

    fun changeLikeStatus(post: PostEntity) {
        viewModelScope.launch {
            vkRepository.changeLikeStatus(post)
            val changedPost = post.copy( isLiked = !post.isLiked)
            dbRepository.updatePost(changedPost)
        }
    }
}
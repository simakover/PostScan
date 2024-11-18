package ru.nyxsed.postscan.presentation

import androidx.compose.runtime.Composable
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
                    lastFetchDate = (System.currentTimeMillis()/1000).toString()
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
}
package ru.nyxsed.postscan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.domain.models.GroupEntity

class PostScreenViewModel(
    private val dbRepository: DbRepository,
    private val vkRepository: VkRepository,
) : ViewModel() {
    val posts = dbRepository.getAllPosts()

    fun loadPosts() {
        val groups = listOf<GroupEntity>(GroupEntity())

        viewModelScope.launch {
            groups.forEach { group ->
                val posts = vkRepository.getPostsForGroup(group)

                posts.forEach { post ->
                    dbRepository.addPost(post)
                }
            }
        }
    }
}
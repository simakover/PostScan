package ru.nyxsed.postscan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.api.sdk.VKKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.presentation.screens.postsscreen.AuthState

class PostsScreenViewModel(
    private val dbRepository: DbRepository,
    private val vkRepository: VkRepository,
    private val storage: VKKeyValueStorage,
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

    // auth
    private val _authStateFlow = MutableStateFlow<AuthState>(AuthState.NotAuthorized)
    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()

    fun checkState() {
        val currentToken = VKAccessToken.restore(storage)
        val loggedIn = currentToken != null && currentToken.isValid
        _authStateFlow.value = if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
    }
}
package ru.nyxsed.postscan.presentation.screens.postsscreen

import android.content.Context
import android.content.res.Resources
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.data.models.entity.PostEntity
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.presentation.screens.commentsscreen.CommentsScreen
import ru.nyxsed.postscan.presentation.screens.loginscreen.LoginScreen
import ru.nyxsed.postscan.util.ConnectionChecker
import ru.nyxsed.postscan.util.Constants.VK_URL
import ru.nyxsed.postscan.util.Constants.VK_WALL_URL
import ru.nyxsed.postscan.util.DataStoreInteraction
import ru.nyxsed.postscan.util.NotificationHelper.completeNotification
import ru.nyxsed.postscan.util.NotificationHelper.errorNotification
import ru.nyxsed.postscan.util.NotificationHelper.initNotification
import ru.nyxsed.postscan.util.NotificationHelper.updateProgress
import ru.nyxsed.postscan.util.UiEvent

class PostsScreenViewModel(
    private val dbRepository: DbRepository,
    private val vkRepository: VkRepository,
    private val dataStoreInteraction: DataStoreInteraction,
    private val connectionChecker: ConnectionChecker,
    private val resources: Resources,
) : ViewModel() {
    val posts = dbRepository.getAllPosts()
    val groups = dbRepository.getAllGroups()

    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    private val _groupSelected = MutableStateFlow<Long>(0L)
    val groupSelected: StateFlow<Long> = _groupSelected.asStateFlow()

    fun loadPosts(context: Context) {
        viewModelScope.launch {
            initNotification(context)
            try {
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
            } catch (e: Exception) {
                _uiEventFlow.emit(UiEvent.ShowToast(e.message!!))
                errorNotification(context, e.message!!)
            }
        }
    }

    fun addPost(post: PostEntity) {
        viewModelScope.launch {
            dbRepository.addPost(post)
        }
    }

    fun deletePost(post: PostEntity, context: Context, snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            if (posts.value.filter { it.ownerId == post.ownerId }.size == 1) {
                selectGroup(0L)
            }
            dbRepository.deletePost(post)

            snackbarHostState.currentSnackbarData?.dismiss()
            val snackbarResult = snackbarHostState.showSnackbar(
                message = context.getString(R.string.post_deleted),
                actionLabel = context.getString(R.string.undo),
                duration = SnackbarDuration.Short
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                addPost(post)
            }
        }
    }

    fun changeLikeStatus(
        post: PostEntity,
        settingDeleteAfterLike: Boolean,
        context: Context,
        snackbarHostState: SnackbarHostState,
    ) {
        viewModelScope.launch {
            if (!connectionChecker.isInternetAvailable()) {
                _uiEventFlow.emit(UiEvent.ShowToast(resources.getString(R.string.no_internet_connection)))
                return@launch
            }

            if (!connectionChecker.isTokenValid()) {
                _uiEventFlow.emit(UiEvent.Navigate(LoginScreen))
                return@launch
            }
            try {
                changeLikeStatusVK(post)
                if (settingDeleteAfterLike) {
                    deletePost(post, context, snackbarHostState)
                } else {
                    changeLikeStatusDb(post)
                }
            } catch (e: Exception) {
                _uiEventFlow.emit(UiEvent.ShowToast(e.message!!))
            }
        }
    }

    suspend fun changeLikeStatusVK(post: PostEntity) {
        vkRepository.changeLikeStatus(post)
    }

    suspend fun changeLikeStatusDb(post: PostEntity) {
        dbRepository.updatePost(post.copy(isLiked = !post.isLiked))
    }

    fun openPostUri(uriHandler: UriHandler, post: PostEntity) {
        uriHandler.openUri("${VK_WALL_URL}${post.ownerId}_${post.postId}")
    }

    fun openGroupUri(uriHandler: UriHandler, group: GroupEntity) {
        uriHandler.openUri("${VK_URL}${group.screenName}")
    }

    suspend fun getSettingBoolean(key: String): Boolean {
        return dataStoreInteraction.getSettingBooleanFromDataStore(key)
    }

    fun refreshPosts(context: Context) {
        viewModelScope.launch {
            if (!connectionChecker.isInternetAvailable()) {
                _uiEventFlow.emit(UiEvent.ShowToast(resources.getString(R.string.no_internet_connection)))
                return@launch
            }

            if (!connectionChecker.isTokenValid()) {
                _uiEventFlow.emit(UiEvent.Navigate(LoginScreen))
                return@launch
            }

            loadPosts(context)
            _uiEventFlow.emit(UiEvent.Scroll())
        }
    }

    fun toComments(post: PostEntity) {
        viewModelScope.launch {
            if (!connectionChecker.isInternetAvailable()) {
                _uiEventFlow.emit(UiEvent.ShowToast(resources.getString(R.string.no_internet_connection)))
                return@launch
            }

            if (!connectionChecker.isTokenValid()) {
                _uiEventFlow.emit(UiEvent.Navigate(LoginScreen))
                return@launch
            }

            _uiEventFlow.emit(UiEvent.NavigateToPost(CommentsScreen, post))
        }
    }

    fun selectGroup(groupId: Long) {
        _groupSelected.value = groupId
    }
}

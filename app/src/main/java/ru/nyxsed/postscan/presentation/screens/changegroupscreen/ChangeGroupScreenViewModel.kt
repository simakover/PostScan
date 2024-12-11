package ru.nyxsed.postscan.presentation.screens.changegroupscreen

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.util.ConnectionChecker
import ru.nyxsed.postscan.util.Constants.VK_URL
import ru.nyxsed.postscan.util.Constants.toDateLong
import ru.nyxsed.postscan.util.NotificationHelper.completeNotification
import ru.nyxsed.postscan.util.NotificationHelper.errorNotification
import ru.nyxsed.postscan.util.NotificationHelper.initNotification
import ru.nyxsed.postscan.util.UiEvent

class ChangeGroupScreenViewModel(
    private val vkRepository: VkRepository,
    private val dbRepository: DbRepository,
    private val connectionChecker: ConnectionChecker,
    private val resources: Resources,
) : ViewModel() {
    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    fun updateGroup(
        groupId: Long,
        groupName: String,
        screenName: String,
        avatarUrl: String,
        lastFetchDate: String,
    ) {
        viewModelScope.launch {
            val fetchDate = lastFetchDate.toDateLong()

            val group = GroupEntity(
                groupId = groupId,
                name = groupName,
                screenName = screenName,
                avatarUrl = avatarUrl,
                lastFetchDate = fetchDate
            )

            dbRepository.updateGroup(group)
        }
    }

    fun openGroupUri(group: GroupEntity) {
        viewModelScope.launch {
            if (!connectionChecker.isInternetAvailable()) {
                _uiEventFlow.emit(UiEvent.ShowToast(resources.getString(R.string.no_internet_connection)))
                return@launch
            }

            _uiEventFlow.emit(UiEvent.OpenUrl(url = "${VK_URL}${group.screenName}"))
        }
    }

    fun loadPosts(context: Context, group: GroupEntity, startDate: String, endDate: String) {
        val startDateUnix = startDate.toDateLong()
        val endDateUnix = endDate.toDateLong()

        viewModelScope.launch {
            initNotification(context)
            try {
                Log.d("loadPosts", "start = $startDateUnix end = ${endDateUnix + 86399000}")
                val postEntities = vkRepository.getPostsForGroupDateInterval(
                    groupEntity = group,
                    startDate = startDateUnix,
                    endDate = endDateUnix + 86399000
                )
                Log.d("loadPosts", "postEntities = ${postEntities.size}")
                postEntities.forEach { post ->
                    dbRepository.addPost(post)
                }
                completeNotification(context)
            } catch (e: Exception) {
                _uiEventFlow.emit(UiEvent.ShowToast(e.message!!))
                errorNotification(context, e.message!!)
            }
        }
    }

    fun deleteGroupWithPosts(group: GroupEntity) {
        viewModelScope.launch {
            dbRepository.deleteAllPostsForGroup(group)
        }
    }
}
package ru.nyxsed.postscan.presentation.screens.changegroupscreen

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.util.ConnectionChecker
import ru.nyxsed.postscan.util.Constants.VK_URL
import ru.nyxsed.postscan.util.UiEvent
import java.text.SimpleDateFormat

class ChangeGroupScreenViewModel(
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
            val format = SimpleDateFormat("ddMMyyyy")
            val fetchDate = format.parse(lastFetchDate)?.time ?: System.currentTimeMillis()

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
}
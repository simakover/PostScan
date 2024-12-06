package ru.nyxsed.postscan.presentation.screens.groupsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.presentation.screens.loginscreen.LoginScreen
import ru.nyxsed.postscan.util.ConnectionChecker
import ru.nyxsed.postscan.util.UiEvent

class GroupsScreenViewModel(
    private val dbRepository: DbRepository,
    private val connectionChecker: ConnectionChecker,
) : ViewModel() {
    val dbGroups = dbRepository.getAllGroups()
    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    fun deleteGroup(group: GroupEntity) {
        viewModelScope.launch {
            dbRepository.deleteGroup(group)
        }
    }

    fun deleteAllPostsForGroup(group: GroupEntity) {
        viewModelScope.launch {
            dbRepository.deleteAllPostsForGroup(group)
        }
    }

    suspend fun checkConnect(): Boolean {
        if (!connectionChecker.isInternetAvailable()) {
            _uiEventFlow.emit(UiEvent.ShowToast(R.string.no_internet_connection))
            return false
        }

        if (!connectionChecker.isTokenValid()) {
            _uiEventFlow.emit(UiEvent.Navigate(LoginScreen))
            return false
        }
        return true
    }
}
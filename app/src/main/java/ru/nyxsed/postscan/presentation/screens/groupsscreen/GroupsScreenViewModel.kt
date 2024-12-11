package ru.nyxsed.postscan.presentation.screens.groupsscreen

import android.content.res.Resources
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
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.presentation.screens.loginscreen.LoginScreen
import ru.nyxsed.postscan.presentation.screens.pickgroupscreen.PickGroupScreen
import ru.nyxsed.postscan.util.ConnectionChecker
import ru.nyxsed.postscan.util.UiEvent

class GroupsScreenViewModel(
    private val dbRepository: DbRepository,
    private val connectionChecker: ConnectionChecker,
    private val resources: Resources,
) : ViewModel() {
    val dbGroups = dbRepository.getAllGroups()
    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    private var groupToDelete: GroupEntity? = null

    fun deleteGroupWithPosts() {
        viewModelScope.launch {
            dbRepository.deleteGroup(groupToDelete!!)
            dbRepository.deleteAllPostsForGroup(groupToDelete!!)
            toggleDeleteDialog()
        }
    }

    fun navigateToPickScreen(param: String) {
        viewModelScope.launch {
            if (!connectionChecker.isInternetAvailable()) {
                _uiEventFlow.emit(UiEvent.ShowToast(resources.getString(R.string.no_internet_connection)))
                return@launch
            }

            if (!connectionChecker.isTokenValid()) {
                _uiEventFlow.emit(UiEvent.Navigate(LoginScreen))
                return@launch
            }

            _uiEventFlow.emit(UiEvent.NavigateToPicker(PickGroupScreen, param))
            toggleAddDialog()
        }
    }

    fun toggleAddDialog() {
        _showAddDialog.value = !_showAddDialog.value
    }

    fun toggleDeleteDialog(group: GroupEntity? = null) {
        _showDeleteDialog.value = !_showDeleteDialog.value
        groupToDelete = group
    }
}
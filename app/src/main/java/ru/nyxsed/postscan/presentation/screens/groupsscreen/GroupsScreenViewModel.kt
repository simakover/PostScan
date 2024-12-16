package ru.nyxsed.postscan.presentation.screens.groupsscreen

import android.content.Context
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
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.presentation.screens.loginscreen.LoginScreen
import ru.nyxsed.postscan.presentation.screens.pickgroupscreen.PickGroupScreen
import ru.nyxsed.postscan.util.ConnectionChecker
import ru.nyxsed.postscan.util.Constants.toDateLong
import ru.nyxsed.postscan.util.NotificationHelper.completeNotification
import ru.nyxsed.postscan.util.NotificationHelper.errorNotification
import ru.nyxsed.postscan.util.NotificationHelper.initNotification
import ru.nyxsed.postscan.util.NotificationHelper.updateProgress
import ru.nyxsed.postscan.util.UiEvent

class GroupsScreenViewModel(
    private val dbRepository: DbRepository,
    private val connectionChecker: ConnectionChecker,
    private val resources: Resources,
    private val vkRepository: VkRepository,
) : ViewModel() {
    val dbGroups = dbRepository.getAllGroups()
    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    private val _showDeleteAllDialog = MutableStateFlow(false)
    val showDeleteAllDialog: StateFlow<Boolean> = _showDeleteAllDialog.asStateFlow()

    private val _showDownloadDialog = MutableStateFlow(false)
    val showDownloadDialog: StateFlow<Boolean> = _showDownloadDialog.asStateFlow()

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

    fun toggleDeleteAllDialog() {
        _showDeleteAllDialog.value = !_showDeleteAllDialog.value
    }

    fun deleteAllPosts() {
        viewModelScope.launch {
            dbRepository.deleteAllPosts()
            toggleDeleteAllDialog()
        }
    }

    fun toggleDownloadDialog() {
        _showDownloadDialog.value = !_showDownloadDialog.value
    }

    fun loadPosts(context: Context, startDate: String, endDate: String) {
        val startDateUnix = startDate.toDateLong()
        val endDateUnix = endDate.toDateLong()

        viewModelScope.launch {
            initNotification(context)
            try {
                dbGroups.value.forEachIndexed { index, group ->
                    val postEntities = vkRepository.getPostsForGroupDateInterval(
                        groupEntity = group,
                        startDate = startDateUnix,
                        endDate = endDateUnix + 86399000
                    )
                    postEntities.forEach { post ->
                        dbRepository.addPost(post)
                    }

                    val percentage = (index + 1) * 100 / dbGroups.value.size
                    updateProgress(context, percentage)

                }
                completeNotification(context)
            } catch (e: Exception) {
                _uiEventFlow.emit(UiEvent.ShowToast(e.message!!))
                errorNotification(context, e.message!!)
            }
        }
        toggleDownloadDialog()
    }
}
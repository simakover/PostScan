package ru.nyxsed.postscan.presentation.screens.groupsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.data.repository.DbRepository

class GroupsScreenViewModel(
    private val dbRepository: DbRepository,
) : ViewModel() {
    val dbGroups = dbRepository.getAllGroups()

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
}
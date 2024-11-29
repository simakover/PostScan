package ru.nyxsed.postscan.presentation.screens.groupsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.data.repository.DbRepository

class GroupsScreenViewModel(
    private val dbRepository: DbRepository,
) : ViewModel() {
    val groups = dbRepository.getAllGroups()

    fun addGroup(group: GroupEntity) {
        viewModelScope.launch {
            dbRepository.addGroup(group)
        }
    }

    fun deleteGroup(group: GroupEntity) {
        viewModelScope.launch {
            dbRepository.deleteGroup(group)
        }
    }
}
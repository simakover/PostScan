package ru.nyxsed.postscan.presentation.screens.groupsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.domain.models.GroupEntity

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
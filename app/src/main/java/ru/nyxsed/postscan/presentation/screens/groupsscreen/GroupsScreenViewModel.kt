package ru.nyxsed.postscan.presentation.screens.groupsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository

class GroupsScreenViewModel(
    private val dbRepository: DbRepository,
    private val vkRepository: VkRepository,
) : ViewModel() {
    val dbGroups = dbRepository.getAllGroups()
    val fetchedGroups = vkRepository.getGroupsStateFlow()

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
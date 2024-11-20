package ru.nyxsed.postscan.presentation.screens.addgroupscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.domain.models.GroupEntity

class AddGroupScreenViewModel(
    private val dbRepository: DbRepository,
    private val vkRepository: VkRepository,
) : ViewModel() {
    fun addGroup(group: GroupEntity) {
        viewModelScope.launch {
            val group = dbRepository.getGroupById(group.groupId!!)
            if (group != null) {
                dbRepository.addGroup(group)
            }
        }
    }

    suspend fun groupsGetById(groupName: String): GroupEntity {
        return vkRepository.groupsGetById(groupName)
    }
}
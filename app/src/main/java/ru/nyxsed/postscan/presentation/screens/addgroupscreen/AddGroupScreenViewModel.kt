package ru.nyxsed.postscan.presentation.screens.addgroupscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.domain.models.GroupEntity
import java.text.SimpleDateFormat

class AddGroupScreenViewModel(
    private val dbRepository: DbRepository,
    private val vkRepository: VkRepository,
) : ViewModel() {
    fun addGroup(group: GroupEntity, fetchDate: String) {
        viewModelScope.launch {

            val lastFetchDate = if (fetchDate.isEmpty()) {
                System.currentTimeMillis()
            } else {
                val format = SimpleDateFormat("ddMMyyyy")
                format.parse(fetchDate)?.time
            } ?: System.currentTimeMillis()

            group.lastFetchDate = lastFetchDate

            val fetchedGroup = dbRepository.getGroupById(group.groupId!!)
            if (fetchedGroup == null) {
                dbRepository.addGroup(group)
            } else {
                dbRepository.updateGroup(group)
            }
        }
    }

    suspend fun groupsGetById(groupName: String): GroupEntity {
        return vkRepository.groupsGetById(groupName)
    }
}
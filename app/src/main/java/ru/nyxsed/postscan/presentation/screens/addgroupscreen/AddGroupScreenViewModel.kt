package ru.nyxsed.postscan.presentation.screens.addgroupscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import java.text.SimpleDateFormat

class AddGroupScreenViewModel(
    private val dbRepository: DbRepository,
    private val vkRepository: VkRepository,
) : ViewModel() {
    fun addGroup(
        groupId : Long,
        groupName: String,
        screenName: String,
        avatarUrl: String,
        lastFetchDate : String
    ) {
        viewModelScope.launch {

            val fetchDate = if (lastFetchDate.isEmpty()) {
                System.currentTimeMillis()
            } else {
                val format = SimpleDateFormat("ddMMyyyy")
                format.parse(lastFetchDate)?.time
            } ?: System.currentTimeMillis()

            val group = GroupEntity(
                groupId = groupId,
                name = groupName,
                screenName = screenName,
                avatarUrl = avatarUrl,
                lastFetchDate = fetchDate
            )

            val fetchedGroup = dbRepository.getGroupById(group.groupId)
            if (fetchedGroup == null) {
                dbRepository.addGroup(group)
            } else {
                dbRepository.updateGroup(group)
            }
        }
    }

    suspend fun groupsGetById(groupName: String): List<GroupEntity> {
        return vkRepository.groupsGetById(groupName)
    }
}
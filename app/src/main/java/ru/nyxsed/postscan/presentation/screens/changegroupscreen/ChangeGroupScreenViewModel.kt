package ru.nyxsed.postscan.presentation.screens.changegroupscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.data.repository.DbRepository
import java.text.SimpleDateFormat

class ChangeGroupScreenViewModel(
    private val dbRepository: DbRepository,
) : ViewModel() {
    fun updateGroup(
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

            dbRepository.updateGroup(group)
        }
    }
}
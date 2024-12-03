package ru.nyxsed.postscan.presentation.screens.pickgroupscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.data.repository.DbRepository
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.presentation.screens.pickgroupscreen.PickGroupState.*
import kotlin.collections.filter

class PickGroupScreenViewModel(
    private val dbRepository: DbRepository,
    private val vkRepository: VkRepository,
) : ViewModel() {
    private val _screenStateFlow = MutableStateFlow<PickGroupState>(PickGroupState.User())
    val screenStateFlow: StateFlow<PickGroupState> = _screenStateFlow.asStateFlow()

    private val fetchedGroupsState = MutableStateFlow<List<GroupEntity>>(emptyList())
    private val filteredGroupsState: StateFlow<List<GroupEntity>> = combine(
        fetchedGroupsState,
        dbRepository.getAllGroups()
    ) { fetchedGroups, dbGroups ->
        fetchedGroups.filter { group ->
            dbGroups.none { it.groupId == group.groupId }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    fun setMode(mode: String) {
        when (mode) {
            "USER_GROUPS" -> {
                viewModelScope.launch {
                    vkRepository.getGroupsStateFlow().collect {
                        fetchedGroupsState.value = it
                        _screenStateFlow.value = PickGroupState.User(filteredGroupsState.value)
                    }
                }
            }

            "SEARCH" -> {
                _screenStateFlow.value = PickGroupState.Search()
            }
        }
    }

    fun fetchedGroups(searchQuery: String) {
        viewModelScope.launch {
            _screenStateFlow.value = PickGroupState.Loading()
            val groups = vkRepository.searchGroupsStateFlow(searchQuery)
            fetchedGroupsState.value = groups
            _screenStateFlow.value = PickGroupState.Search(groups = filteredGroupsState.value)
        }
    }

    fun addGroup(group: GroupEntity) {
        viewModelScope.launch {
            dbRepository.addGroup(group)
            val currentState = _screenStateFlow.value
            fetchedGroupsState.value = fetchedGroupsState.value.filter { it.groupId != group.groupId }

            when (currentState) {
                is PickGroupState.Search -> _screenStateFlow.value = PickGroupState.Search(groups = filteredGroupsState.value)
                is PickGroupState.User -> _screenStateFlow.value = PickGroupState.User(groups = filteredGroupsState.value)
                is PickGroupState.Loading -> {}
            }
        }
    }
}
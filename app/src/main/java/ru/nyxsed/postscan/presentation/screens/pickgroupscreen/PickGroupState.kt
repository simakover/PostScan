package ru.nyxsed.postscan.presentation.screens.pickgroupscreen

import ru.nyxsed.postscan.data.models.entity.GroupEntity

sealed class PickGroupState {
    class Search(
        val groups: List<GroupEntity> = emptyList(),
    ) : PickGroupState()

    class User(
        val groups: List<GroupEntity> = emptyList(),
    ) : PickGroupState()

    class Loading() : PickGroupState()
}
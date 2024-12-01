package ru.nyxsed.postscan.presentation.screens.pickgroupscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupCard
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreenViewModel

val PickGroupScreen by navDestination<Unit> {
    val groupScreenViewModel = koinViewModel<GroupsScreenViewModel>()
    val fetchedGroupsState = groupScreenViewModel.fetchedGroups.collectAsState()
    val dbGroupsState = groupScreenViewModel.dbGroups.collectAsState()

    val groupDifference = fetchedGroupsState.value.filter { group ->
        val foundedGroup = dbGroupsState.value.find { it.groupId == group.groupId }
        group.groupId != foundedGroup?.groupId
    }

    Scaffold { paddings ->
        LazyColumn(
            modifier = Modifier
                .padding(paddings),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = groupDifference,
                key = { it.groupId!! }
            ) {
                Box(
                    modifier = Modifier
                        .animateItem()
                ) {
                    GroupCard(
                        group = it,
                        onGroupDeleteClicked = { },
                        onGroupClicked = {
                            groupScreenViewModel.addGroup(it)
                        },
                        deleteEnabled = false
                    )
                }
            }
        }
    }
}
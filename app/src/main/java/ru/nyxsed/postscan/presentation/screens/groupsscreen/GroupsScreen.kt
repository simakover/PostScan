package ru.nyxsed.postscan.presentation.screens.groupsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.presentation.screens.addgroupscreen.AddGroupScreen

val GroupsScreen by navDestination<Unit> {
    val groupScreenViewModel = koinViewModel<GroupsScreenViewModel>()
    val groupsState = groupScreenViewModel.groups.collectAsState()
    val navController = navController()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AddGroupScreen)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        }
    ) { paddings ->
        LazyColumn(
            modifier = Modifier
                .padding(paddings),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            groupsState.value.forEach {
                item {
                    GroupCard(
                        group = it,
                        onGroupDeleteClicked = {
                            groupScreenViewModel.deleteGroup(it)
                        },
                        onGroupClicked = {
                            navController.navigate(AddGroupScreen, it)
                        }
                    )
                }
            }
        }
    }
}
package ru.nyxsed.postscan.presentation.screens.groupsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.presentation.screens.addgroupscreen.AddGroupScreen

val GroupsScreen by navDestination<Unit> {
    val groupScreenViewModel = koinViewModel<GroupsScreenViewModel>()
    val groupsState = groupScreenViewModel.groups.collectAsState()

    val navController = navController()
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddings ->
        LazyColumn(
            modifier = Modifier
                .padding(paddings),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = groupsState.value,
                key = { it.groupId!! }
            ) {
                Box(
                    modifier = Modifier
                        .animateItem()
                ) {
                    GroupCard(
                        group = it,
                        onGroupDeleteClicked = {
                            groupScreenViewModel.deleteGroup(it)
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                val snackbarResult = snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.group_deleted),
                                    actionLabel = context.getString(R.string.undo),
                                    duration = SnackbarDuration.Short
                                )
                                if (snackbarResult == SnackbarResult.ActionPerformed) {
                                    groupScreenViewModel.addGroup(it)
                                }
                            }
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
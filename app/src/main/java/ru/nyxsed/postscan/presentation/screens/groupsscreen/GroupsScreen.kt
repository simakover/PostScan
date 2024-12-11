package ru.nyxsed.postscan.presentation.screens.groupsscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.presentation.screens.changegroupscreen.ChangeGroupScreen
import ru.nyxsed.postscan.util.UiEvent

val GroupsScreen by navDestination<Unit> {
    val navController = navController()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val groupScreenViewModel = koinViewModel<GroupsScreenViewModel>()

    val groupsState = groupScreenViewModel.dbGroups.collectAsState()
    val showAddDialog by groupScreenViewModel.showAddDialog.collectAsState()
    val showDeleteDialog by groupScreenViewModel.showDeleteDialog.collectAsState()

    LaunchedEffect(Unit) {
        groupScreenViewModel.uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                is UiEvent.Navigate ->
                    navController.navigate(event.destination)

                is UiEvent.NavigateToPicker ->
                    navController.navigate(event.destination, event.navArgs)

                else -> {}
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    groupScreenViewModel.toggleAddDialog()
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
            items(
                items = groupsState.value,
                key = { it.groupId }
            ) {
                Box(
                    modifier = Modifier
                        .animateItem()
                ) {
                    GroupCard(
                        group = it,
                        onGroupDeleteClicked = {
                            groupScreenViewModel.toggleDeleteDialog(it)
                        },
                        onGroupClicked = {
                            navController.navigate(ChangeGroupScreen, it)
                        },
                        deleteEnabled = true
                    )
                }
            }
        }
        ShowAddModalDialog(
            showDialog = showAddDialog,
            onDismiss = {
                groupScreenViewModel.toggleAddDialog()
            },
            onSearchClicked = {
                scope.launch {
                    groupScreenViewModel.navigateToPickScreen("SEARCH")
                }
            },
            onPickClicked = {
                scope.launch {
                    groupScreenViewModel.navigateToPickScreen("USER_GROUPS")
                }
            }
        )
        ShowDeleteModalDialog(
            showDialog = showDeleteDialog,
            onDismiss = {
                groupScreenViewModel.toggleDeleteDialog()
            },
            onConfirmClicked = {
                groupScreenViewModel.deleteGroupWithPosts()
            }
        )
    }
}

@Composable
fun ShowDeleteModalDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmClicked: () -> Unit,
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Text(
                        text = stringResource(R.string.delete_group),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = stringResource(R.string.group_delete_dialog_question)
                    )
                    TextButton(
                        onClick = {
                            onConfirmClicked()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.confirm)
                        )
                    }
                    TextButton(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.cancel)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowAddModalDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSearchClicked: () -> Unit,
    onPickClicked: () -> Unit,
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Text(
                        text = stringResource(R.string.add_group),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = stringResource(R.string.group_search_dialog_question)
                    )
                    TextButton(
                        onClick = {
                            onSearchClicked()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.search_for)
                        )
                    }
                    TextButton(
                        onClick = {
                            onPickClicked()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.pick_from)
                        )
                    }
                }
            }
        }
    }
}
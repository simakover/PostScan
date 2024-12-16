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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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

@OptIn(ExperimentalMaterial3Api::class)
val GroupsScreen by navDestination<Unit> {
    val navController = navController()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val groupScreenViewModel = koinViewModel<GroupsScreenViewModel>()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val groupsState = groupScreenViewModel.dbGroups.collectAsState()
    val showAddDialog by groupScreenViewModel.showAddDialog.collectAsState()
    val showDeleteDialog by groupScreenViewModel.showDeleteDialog.collectAsState()
    val showDeleteAllDialog by groupScreenViewModel.showDeleteAllDialog.collectAsState()

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
        },
        topBar = {
            GroupsScreenBar(
                onDownloadClicked = {

                },
                onDeleteClicked = {
                    groupScreenViewModel.toggleDeleteAllDialog()
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddings ->
        LazyColumn(
            modifier = Modifier
                .padding(paddings)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
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
        AddModalDialog(
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
        DeleteModalDialog(
            title = stringResource(R.string.delete_group),
            description = stringResource(R.string.group_delete_dialog_question),
            showDialog = showDeleteDialog,
            onDismiss = {
                groupScreenViewModel.toggleDeleteDialog()
            },
            onConfirmClicked = {
                groupScreenViewModel.deleteGroupWithPosts()
            }
        )
        DeleteModalDialog(
            title = stringResource(R.string.delete_all_posts),
            description = stringResource(R.string.delete_all_posts_dialog_question),
            showDialog = showDeleteAllDialog,
            onDismiss = {
                groupScreenViewModel.toggleDeleteAllDialog()
            },
            onConfirmClicked = {
                groupScreenViewModel.deleteAllPosts()
            }
        )
    }
}

@Composable
fun DeleteModalDialog(
    title: String,
    description: String,
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
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = description
                    )
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            onConfirmClicked()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.confirm)
                        )
                    }
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth(),
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
fun AddModalDialog(
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
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            onSearchClicked()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.search_for)
                        )
                    }
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth(),
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
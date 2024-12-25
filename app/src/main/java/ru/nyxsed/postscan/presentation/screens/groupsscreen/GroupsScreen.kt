package ru.nyxsed.postscan.presentation.screens.groupsscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.presentation.elements.DatePickerTextField
import ru.nyxsed.postscan.util.Constants.toDateLong
import ru.nyxsed.postscan.util.Constants.toStringDate
import ru.nyxsed.postscan.util.NotificationHelper.completeNotification
import ru.nyxsed.postscan.util.NotificationHelper.errorNotification
import ru.nyxsed.postscan.util.NotificationHelper.initNotification
import ru.nyxsed.postscan.util.NotificationHelper.updateProgress
import ru.nyxsed.postscan.util.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
val GroupsScreen by navDestination<Unit> {
    val navController = navController()
    val context = LocalContext.current

    val groupScreenViewModel = koinViewModel<GroupsScreenViewModel>()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val groupsState = groupScreenViewModel.dbGroups.collectAsState()
    val showAddDialog = groupScreenViewModel.showAddDialog.collectAsState()
    val showDeleteDialog = groupScreenViewModel.showDeleteDialog.collectAsState()
    val showDeleteAllDialog = groupScreenViewModel.showDeleteAllDialog.collectAsState()
    val showDownloadDialog = groupScreenViewModel.showDownloadDialog.collectAsState()

    LaunchedEffect(Unit) {
        groupScreenViewModel.uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                is UiEvent.Navigate ->
                    navController.navigate(event.destination)

                is UiEvent.NavigateToPicker ->
                    navController.navigate(event.destination, event.navArgs)

                is UiEvent.NavigateToChangeGroup ->
                    navController.navigate(event.destination, event.navArgs)

                is UiEvent.InitNotification ->
                    initNotification(context)

                is UiEvent.ErrorNotification ->
                    errorNotification(context, event.message)

                is UiEvent.UpdateNotification ->
                    updateProgress(context, event.percent)

                is UiEvent.CompleteNotification ->
                    completeNotification(context)

                else -> {}
            }
        }
    }

    GroupScreenContent(
        groupScreenViewModel = groupScreenViewModel,
        scrollBehavior = scrollBehavior,
        groupsState = groupsState,
        showAddDialog = showAddDialog,
        showDeleteDialog = showDeleteDialog,
        showDeleteAllDialog = showDeleteAllDialog,
        showDownloadDialog = showDownloadDialog,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreenContent(
    groupScreenViewModel: GroupsScreenViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    groupsState: State<List<GroupEntity>>,
    showAddDialog: State<Boolean>,
    showDeleteDialog: State<Boolean>,
    showDeleteAllDialog: State<Boolean>,
    showDownloadDialog: State<Boolean>,
) {
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
                    groupScreenViewModel.toggleDownloadDialog()
                },
                onDeleteClicked = {
                    groupScreenViewModel.toggleDeleteAllDialog()
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddings ->

        if (groupsState.value.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_data_found),
                    fontSize = 20.sp,
                )
            }
        } else {
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
                                groupScreenViewModel.navigateToChangeGroupScreen(it)
                            },
                            deleteEnabled = true
                        )
                    }
                }
            }
        }
        AddModalDialog(
            showDialog = showAddDialog.value,
            onDismiss = {
                groupScreenViewModel.toggleAddDialog()
            },
            onSearchClicked = {
                groupScreenViewModel.navigateToPickScreen("SEARCH")
            },
            onPickClicked = {
                groupScreenViewModel.navigateToPickScreen("USER_GROUPS")
            }
        )
        DeleteModalDialog(
            title = stringResource(R.string.delete_group),
            description = stringResource(R.string.group_delete_dialog_question),
            showDialog = showDeleteDialog.value,
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
            showDialog = showDeleteAllDialog.value,
            onDismiss = {
                groupScreenViewModel.toggleDeleteAllDialog()
            },
            onConfirmClicked = {
                groupScreenViewModel.deleteAllPosts()
            }
        )
        DownloadPostsModalDialog(
            showDialog = showDownloadDialog.value,
            onDismiss = {
                groupScreenViewModel.toggleDownloadDialog()
            },
            onDownloadClicked = { startDate, endDate ->
                groupScreenViewModel.loadPosts(
                    startDate = startDate,
                    endDate = endDate
                )
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

@Composable
fun DownloadPostsModalDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDownloadClicked: (String, String) -> Unit,
) {
    val currentTime = System.currentTimeMillis().toStringDate().replace(".", "")
    var startDate by remember { mutableStateOf(currentTime) }
    var endDate by remember { mutableStateOf(currentTime) }
    val regex = Regex("^([0-2][0-9]|3[01])(0[1-9]|1[0-2])[0-9]{4}$")

    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties()
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    Text(
                        text = stringResource(R.string.download_posts),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = stringResource(R.string.download_posts_for_a_group_in_this_range)
                    )
                    DatePickerTextField(
                        label = stringResource(R.string.start_date),
                        selectedDate = startDate,
                        onDateSelected = { newDate ->
                            startDate = newDate
                        }
                    )
                    DatePickerTextField(
                        label = stringResource(R.string.end_date),
                        selectedDate = endDate,
                        onDateSelected = { newDate ->
                            endDate = newDate
                        }
                    )
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        enabled = regex.matches(startDate) && regex.matches(endDate) && startDate.toDateLong() <= endDate.toDateLong(),
                        onClick = {
                            onDownloadClicked(startDate, endDate)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.download)
                        )
                    }
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
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
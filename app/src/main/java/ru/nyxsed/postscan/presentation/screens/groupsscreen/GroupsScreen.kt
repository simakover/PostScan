package ru.nyxsed.postscan.presentation.screens.groupsscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.presentation.elements.AddModalDialog
import ru.nyxsed.postscan.presentation.elements.DeleteModalDialog
import ru.nyxsed.postscan.presentation.elements.DownloadModalDialog
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
        DownloadModalDialog(
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
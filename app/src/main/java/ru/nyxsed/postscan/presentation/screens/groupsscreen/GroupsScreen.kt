package ru.nyxsed.postscan.presentation.screens.groupsscreen

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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import ru.nyxsed.postscan.presentation.screens.addgroupscreen.AddGroupScreen
import ru.nyxsed.postscan.presentation.screens.pickgroupscreen.PickGroupScreen

val GroupsScreen by navDestination<Unit> {
    val groupScreenViewModel = koinViewModel<GroupsScreenViewModel>()
    val groupsState = groupScreenViewModel.dbGroups.collectAsState()

    val navController = navController()
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
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
                        },
                        deleteEnabled = true
                    )
                }
            }
        }
        ShowModalDialog(
            showDialog = showDialog,
            onDismiss = {
                showDialog = false
            },
            onManuallyAddClicked = {
                showDialog = false
                navController.navigate(AddGroupScreen)
            },
            onPickClicked = {
                showDialog = false
                navController.navigate(PickGroupScreen)
            }
        )
    }
}

@Composable
fun ShowModalDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onManuallyAddClicked: () -> Unit,
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
                        text = stringResource(R.string.group_add_dialog_question)
                    )
                    TextButton(
                        onClick = {
                            onManuallyAddClicked()
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.manually_add)
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
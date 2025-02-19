package ru.nyxsed.postscan.presentation.screens.changegroupscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composegears.tiamat.navArgs
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.presentation.elements.DatePickerTextField
import ru.nyxsed.postscan.presentation.elements.DeleteModalDialog
import ru.nyxsed.postscan.presentation.elements.DownloadModalDialog
import ru.nyxsed.postscan.util.Constants.toStringDate
import ru.nyxsed.postscan.util.NotificationHelper.completeNotification
import ru.nyxsed.postscan.util.NotificationHelper.errorNotification
import ru.nyxsed.postscan.util.NotificationHelper.initNotification
import ru.nyxsed.postscan.util.UiEvent


val ChangeGroupScreen by navDestination<GroupEntity> {
    val group = navArgs()
    val changeGroupScreenViewModel = koinViewModel<ChangeGroupScreenViewModel>()
    val navController = navController()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    val groupId = changeGroupScreenViewModel.groupId.collectAsState()
    var groupName = changeGroupScreenViewModel.groupName.collectAsState()
    var screenName = changeGroupScreenViewModel.screenName.collectAsState()
    var avatarUrl = changeGroupScreenViewModel.avatarUrl.collectAsState()
    var lastFetchDate = changeGroupScreenViewModel.lastFetchDate.collectAsState()

    val showDeleteDialog = changeGroupScreenViewModel.showDeleteDialog.collectAsState()
    val showDownloadDialog = changeGroupScreenViewModel.showDownloadDialog.collectAsState()

    LaunchedEffect(Unit) {
        group.let {
            changeGroupScreenViewModel.changeGroupId(it.groupId)
            changeGroupScreenViewModel.changeGroupName(it.name)
            changeGroupScreenViewModel.changeScreenName(it.screenName)
            changeGroupScreenViewModel.changeAvatarUrl(it.avatarUrl)
            changeGroupScreenViewModel.changeLastFetchDate(it.lastFetchDate.toStringDate().replace(".", ""))
        }
        changeGroupScreenViewModel.uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                is UiEvent.OpenUrl ->
                    uriHandler.openUri(event.url)

                is UiEvent.InitNotification ->
                    initNotification(context)

                is UiEvent.ErrorNotification ->
                    errorNotification(context, event.message)

                is UiEvent.CompleteNotification ->
                    completeNotification(context)

                is UiEvent.NavigateBack ->
                    navController.back()

                else -> {}
            }
        }
    }

    ChangeGroupScreenContent(
        group = group,
        changeGroupScreenViewModel = changeGroupScreenViewModel,
        groupId = groupId,
        groupName = groupName,
        screenName = screenName,
        avatarUrl = avatarUrl,
        lastFetchDate = lastFetchDate,
        showDownloadDialog = showDownloadDialog,
        showDeleteDialog = showDeleteDialog,
    )
}

@Composable
fun ChangeGroupScreenContent(
    group: GroupEntity,
    changeGroupScreenViewModel: ChangeGroupScreenViewModel,
    groupId: State<Long>,
    groupName: State<String>,
    screenName: State<String>,
    avatarUrl: State<String>,
    lastFetchDate: State<String>,
    showDeleteDialog: State<Boolean>,
    showDownloadDialog: State<Boolean>,
) {
    Scaffold { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
                    .clickable(
                        onClick = {
                            changeGroupScreenViewModel.openGroupUri(group)
                        }
                    ),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarUrl.value)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                value = groupName.value,
                onValueChange = {
                    changeGroupScreenViewModel.changeGroupName(it)
                },
                label = {
                    Text(stringResource(R.string.group_name))
                }
            )
            DatePickerTextField(
                label = stringResource(R.string.last_fetch_date),
                selectedDate = lastFetchDate.value,
                onDateSelected = { newDate ->
                    changeGroupScreenViewModel.changeLastFetchDate(newDate)
                }
            )
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                    onClick = {
                        changeGroupScreenViewModel.updateGroup(
                            groupId.value,
                            groupName.value,
                            screenName.value,
                            avatarUrl.value,
                            lastFetchDate.value
                        )
                    },
                    enabled = changeGroupScreenViewModel.regex.matches(lastFetchDate.value) && groupName.value.isNotEmpty()
                ) {
                    Text(text = stringResource(R.string.update_group))
                }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary),
                ) {
                    IconButton(
                        onClick = {
                            changeGroupScreenViewModel.toggleDownloadDialog()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_download),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary),
                ) {
                    IconButton(
                        onClick = {
                            changeGroupScreenViewModel.toggleDeleteDialog()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        }
        DownloadModalDialog(
            showDialog = showDownloadDialog.value,
            onDismiss = {
                changeGroupScreenViewModel.toggleDownloadDialog()
            },
            onDownloadClicked = { startDate, endDate ->
                changeGroupScreenViewModel.loadPosts(
                    group = group,
                    startDate = startDate,
                    endDate = endDate
                )
            }
        )
        DeleteModalDialog(
            title = stringResource(R.string.delete_posts),
            description = stringResource(R.string.do_you_want_to_delete_all_posts_for_this_group),
            showDialog = showDeleteDialog.value,
            onDismiss ={
                changeGroupScreenViewModel.toggleDeleteDialog()
            },
            onConfirmClicked = {
                changeGroupScreenViewModel.deleteGroupWithPosts(group)
            }
        )
    }
}
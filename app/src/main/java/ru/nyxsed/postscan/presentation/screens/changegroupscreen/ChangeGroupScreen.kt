package ru.nyxsed.postscan.presentation.screens.changegroupscreen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.composegears.tiamat.navArgs
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.util.Constants.DATE_MASK
import ru.nyxsed.postscan.util.Constants.toDateLong
import ru.nyxsed.postscan.util.Constants.toStringDate
import ru.nyxsed.postscan.util.MaskVisualTransformation
import ru.nyxsed.postscan.util.UiEvent
import java.util.Calendar


val ChangeGroupScreen by navDestination<GroupEntity> {
    val group = navArgs()
    val changeGroupScreenViewModel = koinViewModel<ChangeGroupScreenViewModel>()
    val navController = navController()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    val regex = Regex("^([0-2][0-9]|3[01])(0[1-9]|1[0-2])[0-9]{4}$")

    var groupId by remember { mutableLongStateOf(0) }
    var groupName by remember { mutableStateOf("") }
    var screenName by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }
    var lastFetchDate by remember { mutableStateOf("") }

    var showDownloadDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        group.let {
            groupId = it.groupId
            groupName = it.name
            screenName = it.screenName
            avatarUrl = it.avatarUrl
            lastFetchDate = it.lastFetchDate.toStringDate().replace(".", "")
        }
        changeGroupScreenViewModel.uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                is UiEvent.OpenUrl ->
                    uriHandler.openUri(event.url)

                else -> {}
            }
        }
    }

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
                model = avatarUrl,
                placeholder = painterResource(R.drawable.ic_placeholder),
                contentDescription = null,
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                value = groupName,
                onValueChange = {
                    groupName = it
                },
                label = {
                    Text(stringResource(R.string.group_name))
                }
            )
            DatePickerTextField(
                label = stringResource(R.string.last_fetch_date),
                selectedDate = lastFetchDate,
                onDateSelected = { newDate ->
                    lastFetchDate = newDate
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
                        changeGroupScreenViewModel.updateGroup(groupId, groupName, screenName, avatarUrl, lastFetchDate)
                        navController.back()
                    },
                    enabled = regex.matches(lastFetchDate) && groupName.isNotEmpty()
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
                            showDownloadDialog = true
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
                            showDeleteDialog = true
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
        DownloadPostsModalDialog(
            showDialog = showDownloadDialog,
            onDismiss = {
                showDownloadDialog = false
            },
            onDownloadClicked = { startDate, endDate ->
                changeGroupScreenViewModel.loadPosts(
                    context = context,
                    group = group,
                    startDate = startDate,
                    endDate = endDate
                )
                showDownloadDialog = false
            }
        )
        DeletePostsModalDialog(
            showDialog = showDeleteDialog,
            onDismiss = {
                showDeleteDialog = false
            },
            onConfirmClicked = {
                changeGroupScreenViewModel.deleteGroupWithPosts(group)
                showDeleteDialog = false
            }
        )
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

@Composable
fun DeletePostsModalDialog(
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
                        text = stringResource(R.string.delete_posts),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = stringResource(R.string.do_you_want_to_delete_all_posts_for_this_group)
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
fun DatePickerTextField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var showDatePicker by remember { mutableStateOf(false) }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clickable { showDatePicker = true },
        value = selectedDate,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable { showDatePicker = true },
                contentDescription = null,
                imageVector = Icons.Filled.DateRange
            )
        },
        visualTransformation = MaskVisualTransformation(DATE_MASK),
    )


    if (showDatePicker) {
        DatePickerDialog(
            context,
            R.style.CustomDatePickerDialog,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "${selectedDay.toString().padStart(2, '0')}${
                    (selectedMonth + 1).toString().padStart(2, '0')
                }$selectedYear"
                onDateSelected(formattedDate)
                showDatePicker = false
            },
            year,
            month,
            day
        ).apply {
            setOnDismissListener {
                showDatePicker = false
            }
            datePicker.setOnDateChangedListener { _, newYear, newMonth, newDay ->
                val formattedDate =
                    "${newDay.toString().padStart(2, '0')}${(newMonth + 1).toString().padStart(2, '0')}$newYear"
                onDateSelected(formattedDate)
                showDatePicker = false
                dismiss()
            }
        }.show()
    }
}
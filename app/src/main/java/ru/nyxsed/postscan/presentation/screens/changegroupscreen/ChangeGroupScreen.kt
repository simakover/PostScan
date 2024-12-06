package ru.nyxsed.postscan.presentation.screens.changegroupscreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.composegears.tiamat.navArgs
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.util.Constants.DATE_LENGTH
import ru.nyxsed.postscan.util.Constants.DATE_MASK
import ru.nyxsed.postscan.util.Constants.convertLongToTime
import ru.nyxsed.postscan.util.MaskVisualTransformation
import ru.nyxsed.postscan.util.UiEvent

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

    LaunchedEffect(Unit) {
        group.let {
            groupId = it.groupId
            groupName = it.name
            screenName = it.screenName
            avatarUrl = it.avatarUrl
            lastFetchDate = convertLongToTime(it.lastFetchDate).replace(".", "")
        }
        changeGroupScreenViewModel.uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast ->
                    Toast.makeText(context, context.getString(event.messageResId), Toast.LENGTH_SHORT).show()

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
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                value = lastFetchDate,
                onValueChange = {
                    if (it.length <= DATE_LENGTH) {
                        lastFetchDate = it
                    }
                },
                visualTransformation = MaskVisualTransformation(DATE_MASK),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(stringResource(R.string.last_fetch_date))
                }
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                onClick = {
                    changeGroupScreenViewModel.updateGroup(groupId, groupName, screenName, avatarUrl, lastFetchDate)
                    navController.back()
                },
                enabled = regex.matches(lastFetchDate) && groupName.isNotEmpty()
            ) {
                Text(text = stringResource(R.string.update_group))
            }
        }
    }
}


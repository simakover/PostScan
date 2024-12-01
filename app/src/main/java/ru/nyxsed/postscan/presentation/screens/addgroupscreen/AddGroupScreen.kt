package ru.nyxsed.postscan.presentation.screens.addgroupscreen

import android.widget.Toast
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.composegears.tiamat.navArgsOrNull
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.util.Constants.DATE_LENGTH
import ru.nyxsed.postscan.util.Constants.DATE_MASK
import ru.nyxsed.postscan.util.Constants.convertLongToTime
import ru.nyxsed.postscan.util.Constants.isInternetAvailable
import ru.nyxsed.postscan.util.MaskVisualTransformation

val AddGroupScreen by navDestination<GroupEntity> {
    val groupArg = navArgsOrNull()
    val addGroupScreenViewModel = koinViewModel<AddGroupScreenViewModel>()
    val navController = navController()
    val scope = CoroutineScope(Dispatchers.Default)
    val context = LocalContext.current

    val regex = Regex("^([0-2][0-9]|3[01])(0[1-9]|1[0-2])[0-9]{4}$")

    var groupId by remember { mutableLongStateOf(0) }
    var groupName by remember { mutableStateOf("") }
    var screenName by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }
    var lastFetchDate by remember { mutableStateOf(convertLongToTime(System.currentTimeMillis()).replace(".", "")) }

    var groupFound by remember { mutableStateOf(false) }
    var enableSearch by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        groupArg?.let {
            lastFetchDate = convertLongToTime(it.lastFetchDate).replace(".", "")
            groupFound = true
            groupId = it.groupId
            screenName = it.screenName
            groupName = it.name
            avatarUrl = it.avatarUrl
            enableSearch = false
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
                    .size(50.dp),
                model = avatarUrl,
                placeholder = painterResource(R.drawable.ic_placeholder),
                contentDescription = null,
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                enabled = enableSearch,
                value = screenName,
                onValueChange = {
                    screenName = it
                    groupFound = false
                },
                label = {
                    Text(stringResource(R.string.group_name_or_id))
                }
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                enabled = enableSearch,
                onClick = {
                    scope.launch {
                        if (!isInternetAvailable(context)) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.no_internet_connection),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            return@launch
                        }
                        addGroupScreenViewModel.groupsGetById(screenName).let {
                            if (it.isNotEmpty()) {
                                groupFound = true
                                it.first().let { group ->
                                    groupId = group.groupId
                                    groupName = group.name
                                    screenName = group.screenName
                                    avatarUrl = group.avatarUrl
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, context.getString(R.string.group_not_found), Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
                }
            ) {
                Text(stringResource(R.string.search_for_group))
            }
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
                    addGroupScreenViewModel.addGroup(groupId, groupName, screenName, avatarUrl, lastFetchDate)
                    navController.back()
                },
                enabled = groupFound && regex.matches(lastFetchDate) && groupName.isNotEmpty()
            ) {
                Text(text = if (groupArg == null) stringResource(R.string.add_group) else stringResource(R.string.update_group))
            }
        }
    }
}


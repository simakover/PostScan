package ru.nyxsed.postscan.presentation.screens.addgroupscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.composegears.tiamat.navArgsOrNull
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.presentation.screens.postsscreen.convertLongToTime
import java.text.SimpleDateFormat


val AddGroupScreen by navDestination<GroupEntity> {
    val args = navArgsOrNull()
    val addGroupScreenViewModel = koinViewModel<AddGroupScreenViewModel>()
    val navController = navController()
    val scope = CoroutineScope(Dispatchers.Default)

    var group by remember { mutableStateOf(GroupEntity()) }
    var buttonLabel by remember { mutableStateOf("Добавить группу") }
    var lastFetchDateString by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        args?.let {
            buttonLabel = "Редактировать группу"
            lastFetchDateString = convertLongToTime(it.lastFetchDate)
            group = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp),
            model = group.avatarUrl,
            placeholder = painterResource(R.drawable.ic_placeholder),
            contentDescription = null,
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            value = group.screenName,
            onValueChange = {
                group = group.copy(screenName = it)
            },
            label = {
                Text("Имя или id группы")
            }
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            onClick = {
                scope.launch {
                    group = addGroupScreenViewModel.groupsGetById(group.screenName)
                }
            }
        ) {
            Text("Искать группу")
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            value = group.name,
            onValueChange = {
                group = group.copy(name = it)
            },
            label = {
                Text("Имя группы")
            }
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            value = lastFetchDateString,
            onValueChange = {
                lastFetchDateString = it
            },
            label = {
                Text("Дата последнего сканирования")
            }
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            onClick = {

                val lastFetchDate = if (lastFetchDateString.isEmpty()) {
                    System.currentTimeMillis()
                } else {
                    val format = SimpleDateFormat("dd.MM.yyyy")
                    format.parse(lastFetchDateString)?.time
                } ?: System.currentTimeMillis()

                group.lastFetchDate = lastFetchDate
                group.groupId = group.groupId?.let { it * -1 }

                addGroupScreenViewModel.addGroup(group)
                navController.back()
            }
        ) {
            Text(buttonLabel)
        }
    }
}
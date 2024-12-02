package ru.nyxsed.postscan.presentation.screens.pickgroupscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navArgsOrNull
import com.composegears.tiamat.navDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupCard
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreenViewModel
import ru.nyxsed.postscan.util.Constants.isInternetAvailable

val PickGroupScreen by navDestination<String> {
    val mode = navArgsOrNull()
    val groupScreenViewModel = koinViewModel<GroupsScreenViewModel>()
    val scope = CoroutineScope(Dispatchers.Default)
    val context = LocalContext.current

    val dbGroupsState by groupScreenViewModel.dbGroups.collectAsState()
    var fetchedGroupsState by remember { mutableStateOf(emptyList<GroupEntity>()) }

    LaunchedEffect(mode) {
        if (mode == "USER_GROUPS") {
            groupScreenViewModel.userGroups.collect { groups ->
                fetchedGroupsState = groups
            }
        }
    }

    val groupDifference = fetchedGroupsState.filter { group ->
        val foundedGroup = dbGroupsState.find { it.groupId == group.groupId }
        group.groupId != foundedGroup?.groupId
    }

    var searchQuery by remember { mutableStateOf("") }

    Scaffold { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(8.dp),
        ) {
            if (mode == "SEARCH") {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    label = {
                        Text(stringResource(R.string.search_query))
                    }
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
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
                            groupScreenViewModel.fetchedGroups(searchQuery).collect { groups ->
                                fetchedGroupsState = groups
                            }
                        }
                    }
                ) {
                    Text(stringResource(R.string.search_for_group))
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = groupDifference,
                    key = { it.groupId }
                ) {
                    Box(
                        modifier = Modifier
                            .animateItem()
                    ) {
                        GroupCard(
                            group = it,
                            onGroupDeleteClicked = { },
                            onGroupClicked = {
                                groupScreenViewModel.addGroup(it)
                            },
                            deleteEnabled = false
                        )
                    }
                }
            }
        }
    }
}



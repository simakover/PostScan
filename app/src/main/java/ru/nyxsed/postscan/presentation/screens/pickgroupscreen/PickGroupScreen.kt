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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navArgs
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.GroupEntity
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupCard
import ru.nyxsed.postscan.util.UiEvent

val PickGroupScreen by navDestination<String> {
    val mode = navArgs()
    val pickGroupScreenViewModel = koinViewModel<PickGroupScreenViewModel>()
    val context = LocalContext.current

    val screenState = pickGroupScreenViewModel.screenStateFlow.collectAsState()

    LaunchedEffect(mode) {
        pickGroupScreenViewModel.setMode(mode)
        pickGroupScreenViewModel.uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast ->
                    Toast.makeText(
                        context,
                        context.getString(event.messageResId),
                        Toast.LENGTH_SHORT
                    ).show()
                else -> {}
            }
        }
    }

    PickGroupContent(
        pickGroupScreenViewModel = pickGroupScreenViewModel,
        screenState = screenState
    )
}

@Composable
fun PickGroupContent(
    pickGroupScreenViewModel: PickGroupScreenViewModel,
    screenState: State<PickGroupState>,
) {
    Scaffold { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(8.dp),
        ) {
            val currentState = screenState.value
            when (currentState) {
                is PickGroupState.Loading -> {
                    SearchView(
                        onSearchClicked = {
                            pickGroupScreenViewModel.fetchedGroups(it)
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is PickGroupState.Search -> {
                    SearchView(
                        onSearchClicked = {
                            pickGroupScreenViewModel.fetchedGroups(it)
                        }
                    )
                    GroupsLazyColum(
                        groups = currentState.groups,
                        onGroupCardClicked = {
                            pickGroupScreenViewModel.addGroup(it)
                        }
                    )
                }

                is PickGroupState.User -> {
                    GroupsLazyColum(
                        groups = currentState.groups,
                        onGroupCardClicked = {
                            pickGroupScreenViewModel.addGroup(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchView(
    onSearchClicked: (String) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

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
            onSearchClicked(searchQuery)
        }
    ) {
        Text(stringResource(R.string.search_for_group))
    }
}

@Composable
fun GroupsLazyColum(
    groups: List<GroupEntity>,
    onGroupCardClicked: (GroupEntity) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = groups,
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
                        onGroupCardClicked(it)
                    },
                    deleteEnabled = false
                )
            }
        }
    }
}

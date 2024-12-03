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
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupCard
import ru.nyxsed.postscan.util.Constants.isInternetAvailable

val PickGroupScreen by navDestination<String> {
    val mode = navArgs()
    val pickGroupScreenViewModel = koinViewModel<PickGroupScreenViewModel>()
    val scope = CoroutineScope(Dispatchers.Default)
    val context = LocalContext.current

    val screenState by pickGroupScreenViewModel.screenStateFlow.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(mode) {
        pickGroupScreenViewModel.setMode(mode)
    }

    Scaffold { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(8.dp),
        ) {
            if (screenState is PickGroupState.Search || screenState is PickGroupState.Loading) {
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
                            pickGroupScreenViewModel.fetchedGroups(searchQuery)
                        }
                    }
                ) {
                    Text(stringResource(R.string.search_for_group))
                }
            }
            if (screenState is PickGroupState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {

                val groups = if (screenState is PickGroupState.Search) {
                    (screenState as PickGroupState.Search).groups
                } else {
                    (screenState as PickGroupState.User).groups
                }

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
                                    pickGroupScreenViewModel.addGroup(it)
                                },
                                deleteEnabled = false
                            )
                        }
                    }
                }
            }
        }
    }
}



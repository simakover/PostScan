package ru.nyxsed.postscan.presentation.screens.postscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.presentation.PostScreenViewModel

val PostScreen by navDestination<Unit> {
    val vm = koinViewModel<PostScreenViewModel>()
    val postListState = vm.posts.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                onRefreshClicked = {
                    vm.loadPosts()
                }
            )
        }
    ) { paddings ->
        LazyColumn(
            modifier = Modifier
                .padding(paddings),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            postListState.value.forEach {
                item {
                    PostCard(
                        post = it,
                        onPostDeleteClicked = {
                            vm.deletePost(it)
                        }
                    )
                }
            }
        }
    }
}
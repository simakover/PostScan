package ru.nyxsed.postscan.presentation.screens.postsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.presentation.PostsScreenViewModel
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreen
import ru.nyxsed.postscan.presentation.screens.loginscreen.LoginScreen

val PostScreen by navDestination<Unit> {
    val postsScreenViewModel = koinViewModel<PostsScreenViewModel>()
    val postListState = postsScreenViewModel.posts.collectAsState()
    val authState = postsScreenViewModel.authStateFlow.collectAsState()
    val navController = navController()
    Scaffold(
        topBar = {
            PostsScreenBar(
                onRefreshClicked = {
                    postsScreenViewModel.checkState()
                    when (authState.value) {
                        AuthState.Authorized -> postsScreenViewModel.loadPosts()
                        AuthState.NotAuthorized -> navController.navigate(LoginScreen)
                    }
                },
                onNavToGroupsClicked = {
                    postsScreenViewModel.checkState()
                    when (authState.value) {
                        AuthState.Authorized -> navController.navigate(GroupsScreen)
                        AuthState.NotAuthorized -> navController.navigate(LoginScreen)
                    }
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
            items(
                items = postListState.value,
                key = { it.id }
            ) {
                Box(
                    modifier = Modifier
                        .animateItem()
                ) {
                    PostCard(
                        post = it,
                        onPostDeleteClicked = {
                            postsScreenViewModel.deletePost(it)
                        },
                        onLikeClicked = {
                            postsScreenViewModel.changeLikeStatus(it)
                        }
                    )
                }
            }
        }
    }
}
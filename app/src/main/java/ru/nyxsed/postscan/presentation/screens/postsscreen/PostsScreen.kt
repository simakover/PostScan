package ru.nyxsed.postscan.presentation.screens.postsscreen

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.SharedViewModel
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreen
import ru.nyxsed.postscan.presentation.screens.imagepagerscreen.ImagePagerArgs
import ru.nyxsed.postscan.presentation.screens.imagepagerscreen.ImagePagerScreen
import ru.nyxsed.postscan.presentation.screens.loginscreen.LoginScreen
import ru.nyxsed.postscan.util.Constants.isInternetAvailable

val PostsScreen by navDestination<Unit> {
    val postsScreenViewModel = koinViewModel<PostsScreenViewModel>()
    val postListState = postsScreenViewModel.posts.collectAsState()

    val sharedViewModel = koinViewModel<SharedViewModel>()
    val authState = sharedViewModel.authStateFlow.collectAsState()

    val navController = navController()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            PostsScreenBar(
                onRefreshClicked = {
                    if (!isInternetAvailable(context)) {
                        Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT)
                            .show()
                        return@PostsScreenBar
                    }
                    when (authState.value) {
                        AuthState.Authorized -> postsScreenViewModel.loadPosts()
                        AuthState.NotAuthorized -> navController.navigate(LoginScreen)
                    }
                },
                onNavToGroupsClicked = {
                    when (authState.value) {
                        AuthState.Authorized -> navController.navigate(GroupsScreen)
                        AuthState.NotAuthorized -> navController.navigate(LoginScreen)
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
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
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                val snackbarResult = snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.post_deleted),
                                    actionLabel = context.getString(R.string.undo),
                                    duration = SnackbarDuration.Short
                                )
                                if (snackbarResult == SnackbarResult.ActionPerformed) {
                                    postsScreenViewModel.addPost(it)
                                }
                            }
                        },
                        onLikeClicked = {
                            postsScreenViewModel.changeLikeStatus(it)
                        },
                        onToVkClicked = {
                            postsScreenViewModel.openPostUri(
                                uriHandler = uriHandler,
                                post = it
                            )
                        },
                        onToMihonClicked = {
                            val intent = postsScreenViewModel.mihonIntent(
                                query = it.contentText
                            )
                            startActivity(context, intent, Bundle())
                        },
                        onTextLongClick = {
                            clipboardManager.setText(
                                annotatedString = AnnotatedString(it.contentText)
                            )
                        },
                        onImageClicked = { post, index ->
                            val imagePagerArgs = ImagePagerArgs(post, index)
                            navController.navigate(ImagePagerScreen, imagePagerArgs)
                        }
                    )
                }
            }
        }
    }
}
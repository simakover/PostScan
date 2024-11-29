package ru.nyxsed.postscan.presentation.screens.postsscreen

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.SharedViewModel
import ru.nyxsed.postscan.presentation.screens.commentsscreen.CommentsScreen
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreen
import ru.nyxsed.postscan.presentation.screens.imagepagerscreen.ImagePagerArgs
import ru.nyxsed.postscan.presentation.screens.imagepagerscreen.ImagePagerScreen
import ru.nyxsed.postscan.presentation.screens.loginscreen.LoginScreen
import ru.nyxsed.postscan.presentation.screens.preferencesscreen.PreferencesScreen
import ru.nyxsed.postscan.util.Constants.USE_MIHON
import ru.nyxsed.postscan.util.Constants.findOrFirst
import ru.nyxsed.postscan.util.Constants.isInternetAvailable
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
val PostsScreen by navDestination<Unit> {
    val postsScreenViewModel = koinViewModel<PostsScreenViewModel>()
    val postListState = postsScreenViewModel.posts.collectAsState()
    val groupListState = postsScreenViewModel.groups.collectAsState()

    val sharedViewModel = koinViewModel<SharedViewModel>()
    val authState = sharedViewModel.authStateFlow.collectAsState()

    val navController = navController()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()

    var groupSelected by rememberSaveable { mutableLongStateOf(0L) }
    val scrollState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var settingUseMihon by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        settingUseMihon = postsScreenViewModel.getSettingBoolean(USE_MIHON)
    }

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
                        AuthState.Authorized -> {
                            postsScreenViewModel.loadPosts(context)
                            scope.launch {
                                scrollState.scrollToItem(0)
                            }
                        }

                        AuthState.NotAuthorized -> navController.navigate(LoginScreen)
                    }
                },
                onNavToGroupsClicked = {
                    when (authState.value) {
                        AuthState.Authorized -> navController.navigate(GroupsScreen)
                        AuthState.NotAuthorized -> navController.navigate(LoginScreen)
                    }
                },
                onNavToSettingsClicked = {
                    navController.navigate(PreferencesScreen)
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize(),
        ) {
            LazyRow(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .zIndex(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                groupListState.value.forEach { group ->
                    val postCount = postListState.value.filter { it.ownerId.absoluteValue == group.groupId }.size
                    if (postCount > 0) {
                        item(
                            key = group.groupId
                        ) {
                            GroupChip(
                                group = group,
                                isSelected = group.groupId == groupSelected,
                                postCount = postCount,
                                onChipClicked = {
                                    if (groupSelected != group.groupId!!) {
                                        groupSelected = group.groupId!!
                                    } else {
                                        groupSelected = 0L
                                    }
                                    scope.launch {
                                        scrollState.scrollToItem(0)
                                    }
                                }
                            )
                        }
                    }
                }
            }
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .padding(4.dp)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    Spacer(
                        modifier = Modifier
                            .height(37.dp),
                    )
                }
                items(
                    items = postListState.value.filter {
                        if (groupSelected == 0L) true else it.ownerId.absoluteValue == groupSelected
                    },
                    key = { it.postId }
                ) {
                    Box(
                        modifier = Modifier
                            .animateItem()
                    ) {
                        PostCard(
                            post = it,
                            settingUseMihon = settingUseMihon,
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
                                    query = it
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
                            },
                            onCommentsClicked = {
                                if (!isInternetAvailable(context)) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.no_internet_connection),
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    return@PostCard
                                }
                                when (authState.value) {
                                    AuthState.Authorized -> navController.navigate(CommentsScreen, it)
                                    AuthState.NotAuthorized -> navController.navigate(LoginScreen)
                                }
                            },
                            onGroupClicked = { post ->
                                val group = groupListState.value.findOrFirst { it.groupId == post.ownerId.absoluteValue }
                                postsScreenViewModel.openGroupUri(
                                    uriHandler = uriHandler,
                                    group = group
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
package ru.nyxsed.postscan.presentation.screens.postsscreen

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import coil3.compose.AsyncImage
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.SharedViewModel
import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.presentation.screens.commentsscreen.CommentsScreen
import ru.nyxsed.postscan.presentation.screens.groupsscreen.GroupsScreen
import ru.nyxsed.postscan.presentation.screens.imagepagerscreen.ImagePagerArgs
import ru.nyxsed.postscan.presentation.screens.imagepagerscreen.ImagePagerScreen
import ru.nyxsed.postscan.presentation.screens.loginscreen.LoginScreen
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
                items(
                    items = groupListState.value,
                    key = { it.groupId!! }
                ) { groupChip ->

                    Chip(
                        group = groupChip,
                        isSelected = groupChip.groupId == groupSelected,
                        postCount = postListState.value.filter { it.ownerId.absoluteValue == groupChip.groupId }.size,
                        onClick = {
                            if (groupSelected != groupChip.groupId!!) {
                                groupSelected = groupChip.groupId!!
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
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .padding(4.dp)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
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
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Chip(
    group: GroupEntity,
    postCount: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val textColor = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary

    val displayedText = if (group.name.length > 10) {
        group.name.substring(0, 10) + "... ($postCount)"
    } else {
        group.name + " ($postCount)"
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .border(1.dp, color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .size(25.dp),
                model = group.avatarUrl,
                placeholder = painterResource(R.drawable.ic_placeholder),
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(4.dp),
                text = displayedText,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
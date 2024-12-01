package ru.nyxsed.postscan.presentation.screens.commentsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navArgs
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.nyxsed.postscan.data.models.entity.PostEntity
import ru.nyxsed.postscan.presentation.screens.imagepagerscreen.ImagePagerArgs
import ru.nyxsed.postscan.presentation.screens.imagepagerscreen.ImagePagerScreen
import ru.nyxsed.postscan.util.DataStoreInteraction.Companion.USE_MIHON

val CommentsScreen by navDestination<PostEntity> {
    val args = navArgs()
    val commentsScreenViewModel = koinViewModel<CommentsScreenViewModel>(
        key = args.postId.toString(),
        parameters = { parametersOf(args) }
    )
    val navController = navController()

    val comments by commentsScreenViewModel.comments.collectAsState()

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var settingUseMihon by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        settingUseMihon = commentsScreenViewModel.getSettingBoolean(USE_MIHON)
    }

    Scaffold { paddings ->
        LazyColumn(
            modifier = Modifier
                .padding(paddings),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = comments
                    .filter { it.parentStack == null }
                    .filter { it.contentText.isNotEmpty() || it.content.isNotEmpty() },
                key = { it.commentId }
            ) { originalComment ->
                CommentCard(
                    comment = originalComment,
                    replays = comments
                        .filter { it.parentStack == originalComment.commentId }
                        .filter { it.contentText.isNotEmpty() || it.content.isNotEmpty() },
                    settingUseMihon = settingUseMihon,
                    onToMihonClicked = {
                        val intent = commentsScreenViewModel.mihonIntent(
                            query = it.contentText
                        )
                        context.startActivity(intent)
                    },
                    onTextLongClick = {
                        clipboardManager.setText(
                            annotatedString = AnnotatedString(it)
                        )
                    },
                    onImageClicked = { content, index ->
                        val imagePagerArgs = ImagePagerArgs(content, index)
                        navController.navigate(ImagePagerScreen, imagePagerArgs)
                    }
                )
            }
        }
    }
}
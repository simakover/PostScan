package ru.nyxsed.postscan.presentation.screens.imagepagerscreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.composegears.tiamat.navArgs
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.SharedViewModel
import ru.nyxsed.postscan.domain.models.ContentEntity
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.presentation.screens.postsscreen.AuthState
import ru.nyxsed.postscan.presentation.ui.theme.likedHeart
import ru.nyxsed.postscan.util.Constants.isInternetAvailable

val ImagePagerScreen by navDestination<ImagePagerArgs> {
    val imagePagerArgs = navArgs()
    val index = imagePagerArgs.index
    val imagePagerViewModel = koinViewModel<ImagePagerViewModel>()
    val context = LocalContext.current

    val sharedViewModel = koinViewModel<SharedViewModel>()
    val authState = sharedViewModel.authStateFlow.collectAsState()

    var content by remember { mutableStateOf<List<ContentEntity>>(imagePagerArgs.post.content) }

    val uriHandler = LocalUriHandler.current
    val pagerState = rememberPagerState(
        initialPage = index,
        pageCount = { content.size }
    )

    var pageData by remember { mutableStateOf<Map<Int, Boolean>>(emptyMap()) }

    // Отслеживание смены страницы. если страница уже открывалась - не делать повторный запрос
    LaunchedEffect(pagerState.currentPage) {
        if (!isInternetAvailable(context)) {
            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            return@LaunchedEffect
        }
        if (authState == AuthState.NotAuthorized) {
            Toast.makeText(context, context.getString(R.string.app_is_not_authorized), Toast.LENGTH_SHORT).show()
            return@LaunchedEffect
        }

        val currentPage = pagerState.currentPage
        if (!pageData.containsKey(currentPage)) {

            val data = imagePagerViewModel.checkLikeStatus(content[currentPage])

            val updatedContent = content.mapIndexed { index, entity ->
                if (index == currentPage) {
                    entity.copy(isLiked = data)
                } else {
                    entity
                }
            }
            content = updatedContent

            pageData = pageData + (currentPage to data)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize(),
            state = pagerState,
        ) { index ->
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = content[index].urlBig,
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_placeholder),
                contentScale = ContentScale.Fit
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        ) {
            IconButton(
                onClick = {
                    imagePagerViewModel.openPostUri(
                        uriHandler = uriHandler,
                        contentEntity = content[index]
                    )
                }
            ) {
                Image(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(R.drawable.vk_logo),
                    contentDescription = null
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            IconButton(
                onClick = {
                    if (!isInternetAvailable(context)) {
                        Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
                        return@IconButton
                    }
                    if (authState == AuthState.NotAuthorized) {
                        Toast.makeText(context, context.getString(R.string.app_is_not_authorized), Toast.LENGTH_SHORT).show()
                        return@IconButton
                    }

                    imagePagerViewModel.changeLikeStatus(content[pagerState.currentPage])

                    val updatedContent = content.mapIndexed { index, entity ->
                        if (index == pagerState.currentPage) {
                            entity.copy(isLiked = !entity.isLiked)
                        } else {
                            entity
                        }
                    }
                    content = updatedContent
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_like),
                    tint = if (content[pagerState.currentPage].isLiked) likedHeart else MaterialTheme.colorScheme.onSecondary,
                    contentDescription = null
                )
            }
        }
    }
}

data class ImagePagerArgs(
    val post: PostEntity,
    val index: Int,
)
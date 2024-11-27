package ru.nyxsed.postscan.presentation.screens.imagepagerscreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.composegears.tiamat.navArgs
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.SharedViewModel
import ru.nyxsed.postscan.domain.models.ContentEntity
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.presentation.screens.postsscreen.AuthState
import ru.nyxsed.postscan.presentation.ui.theme.likedHeart
import ru.nyxsed.postscan.util.Constants.isInternetAvailable

@OptIn(ExperimentalMaterial3Api::class)
val ImagePagerScreen by navDestination<ImagePagerArgs> {
    val imagePagerArgs = navArgs()
    val index = imagePagerArgs.index
    val imagePagerViewModel = koinViewModel<ImagePagerViewModel>()
    val context = LocalContext.current
    val navController = navController()
    val scope = CoroutineScope(Dispatchers.IO)

    val sharedViewModel = koinViewModel<SharedViewModel>()
    val authState by sharedViewModel.authStateFlow.collectAsState()

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
        if (authState is AuthState.NotAuthorized) {
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

    var fullScreen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (!fullScreen) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black
                    ),
                    title = {
                        Text(
                            text = "${pagerState.currentPage + 1} из ${content.size}",
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.back()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                imagePagerViewModel.findYandexImage(uriHandler,content[pagerState.currentPage].urlBig)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                )
            }
        }
    ) { paddings ->
        Column(
            modifier = Modifier
                .background(Color.Black)
                .padding(paddings)
                .fillMaxSize()
        ) {
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = pagerState,
            ) { index ->
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = {
                            fullScreen = !fullScreen
                        }),
                    model = content[index].urlBig,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.ic_placeholder),
                    contentScale = ContentScale.Fit
                )
            }
            if (!fullScreen) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    items(
                        items = content,
                        key = { it.contentId }
                    ) { item ->
                        AsyncImage(
                            modifier = Modifier
                                .size(if (item.contentId == content[pagerState.currentPage].contentId) 60.dp else 30.dp)
                                .clickable(onClick = {
                                    scope.launch {
                                        val currentIndex = content.indexOf(item)
                                        pagerState.scrollToPage(page = currentIndex)
                                    }
                                }),
                            model = item.urlSmall,
                            contentDescription = null,
                            placeholder = painterResource(R.drawable.ic_placeholder),
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
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
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.no_internet_connection),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                return@IconButton
                            }
                            if (authState is AuthState.NotAuthorized) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.app_is_not_authorized),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
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
                            tint = if (content[pagerState.currentPage].isLiked) likedHeart else Color.White,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}


data class ImagePagerArgs(
    val post: PostEntity,
    val index: Int,
)
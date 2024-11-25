package ru.nyxsed.postscan.presentation.screens.imagepagerscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.composegears.tiamat.navArgs
import com.composegears.tiamat.navDestination
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.domain.models.PostEntity

val ImagePagerScreen by navDestination<ImagePagerArgs> {
    val imagePagerArgs = navArgs()
    val urls = imagePagerArgs.post.contentImageUrl
    val index = imagePagerArgs.index

    val pagerState = rememberPagerState(
        initialPage = index,
        pageCount = { urls.size }
    )
    HorizontalPager(
        modifier = Modifier
            .fillMaxSize(),
        state = pagerState,
    ) { index ->
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = urls[index],
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_placeholder),
            contentScale = ContentScale.Fit
        )
    }
}

data class ImagePagerArgs(
    val post: PostEntity,
    val index: Int,
)
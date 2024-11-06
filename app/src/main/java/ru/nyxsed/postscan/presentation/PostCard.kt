package ru.nyxsed.postscan.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.presentation.models.PostUiModel

@Composable
fun PostCard(
    post: PostUiModel,
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(8.dp)
        ) {
            PostHeader(post = post)
            AsyncImage(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                model = post.contentImageUrl,
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_placeholder),
                contentScale = ContentScale.FillWidth
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp),
                text = post.contentText,
                color = MaterialTheme.colorScheme.onSecondary,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_comment),
                        tint = MaterialTheme.colorScheme.onSecondary,
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_like),
                        tint = MaterialTheme.colorScheme.onSecondary,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun PostHeader(
    post: PostUiModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp),
            model = post.ownerImageUrl,
            placeholder = painterResource(R.drawable.ic_placeholder),
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ) {
            Text(
                text = post.ownerName,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = post.publicationDate,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleSmall
            )
        }
        IconButton(
            onClick = {}
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Preview
@Composable
private fun PostCardPreview() {
    val TEST_GROUP_IMG_URL =
        "https://sun9-51.userapi.com/s/v1/ig2/ox1k9csdJ9v6nJqhDGtwAb-jDcRcv2RtZlTe3emRHILprQveGVaYeMUxktUKTRi-Y-AP-odX9GzFGenjXTy4SEB2.jpg?quality=95&crop=135,0,530,530&as=32x32,48x48,72x72,108x108,160x160,240x240,360x360,480x480&ava=1&cs=50x50"
    val TEST_POST_IMG_URL =
        "https://sun9-14.userapi.com/impg/F4ATtq58Pm5bpSPl6jlGv0EVK07vP3Z-dX8WCA/rGBB4skflLE.jpg?size=720x960&quality=96&sign=bff4cded0a5d3ed984985d28c443322d&type=album"
    val postUiModel = PostUiModel(
        id = 1,
        ownerId = 1,
        ownerName = "Some Groupasdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd",
        ownerImageUrl = TEST_GROUP_IMG_URL,
        publicationDate = "23.11.2024",
        contentText = "some textasdasdasdsadasdsdasdsdkjfsldkafjdklfjaskdljjflksdjfklsadfjksdjf",
        contentImageUrl = TEST_POST_IMG_URL,
        isLiked = false
    )
    PostCard(
        post = postUiModel
    )
}
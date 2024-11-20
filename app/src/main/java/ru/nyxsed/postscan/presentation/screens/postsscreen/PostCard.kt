package ru.nyxsed.postscan.presentation.screens.postsscreen

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.util.Constants.convertLongToTime

@Composable
fun PostCard(
    post: PostEntity,
    onPostDeleteClicked: (PostEntity) -> Unit,
    onLikeClicked: (PostEntity) -> Unit,
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondary)
                .padding(8.dp)
        ) {
            PostHeader(
                post = post,
                onPostDeleteClicked = onPostDeleteClicked
            )
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
                    onClick = {
                        onLikeClicked(post)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_like),
                        tint = if (post.isLiked) Color.Red else MaterialTheme.colorScheme.onSecondary,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun PostHeader(
    post: PostEntity,
    onPostDeleteClicked: (PostEntity) -> Unit,
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
                text = convertLongToTime(post.publicationDate),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleSmall
            )
        }
        IconButton(
            onClick = {
                onPostDeleteClicked(post)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}
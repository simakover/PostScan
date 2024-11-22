package ru.nyxsed.postscan.presentation.screens.postsscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.presentation.ui.theme.likedHeart
import ru.nyxsed.postscan.util.Constants.convertLongToTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostCard(
    post: PostEntity,
    onPostDeleteClicked: (PostEntity) -> Unit,
    onLikeClicked: (PostEntity) -> Unit,
    onToVkClicked: (PostEntity) -> Unit,
    onToMihonClicked: (PostEntity) -> Unit,
    onTextLongClick: (PostEntity) -> Unit,
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
                    .fillMaxWidth(),
                model = post.contentImageUrl,
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_placeholder),
                contentScale = ContentScale.FillWidth
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .combinedClickable(
                        onClick = { },
                        onLongClick = {
                            onTextLongClick(post)
                        }
                    ),
                text = post.contentText,
                color = MaterialTheme.colorScheme.onSecondary,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        onToVkClicked(post)
                    }
                ) {
                    Image(
                        modifier = Modifier
                            .size(24.dp),
                        painter = painterResource(R.drawable.vk_logo),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {
                        onToMihonClicked(post)
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp),
                        painter = painterResource(R.drawable.ic_mihon),
                        tint = MaterialTheme.colorScheme.onSecondary,
                        contentDescription = null
                    )
                }
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
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
                        tint = if (post.isLiked) likedHeart else MaterialTheme.colorScheme.onSecondary,
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

@Preview
@Composable
private fun PostCardPreview() {
    PostCard(
        post = PostEntity(
            postId = 14471,
            ownerId = -2355707,
            ownerName = "Единорог - Настольные игры (Екатеринбург)",
            ownerImageUrl = "https://sun9-42.userapi.com/s/v1/ig2/UAtW1ENFT2UnP5QuCrfb_edhBCw3S94ZH0wCDwUOK5yzC6GBmfV7SVHBKS1Yw91aRrOsNJ5HksdcVIPySjiSpcVH.jpg?quality=95&crop=40,40,320,320&as=32x32,48x48,72x72,108x108,160x160,240x240&ava=1&cs=50x50",
            publicationDate = 1732096802000,
            contentText = "\uD83C\uDFB2 21.11 ЧЕТВЕРГ - Играем в настолки!    Откроем новинки издательства Фабрика Игр — «Омерта», «Пикси» и «Король под Горой». К вашим услугам наша гигантская коллекция настолок и лучший в мире гейм-мастер [id8395407|Дима]    ⏰ Начало в 18:00  \uD83D\uDCB0 Участие 250₽, которые мы возвращаем в виде купона достоинством 150₽",
            contentImageUrl = "https://sun9-63.userapi.com/s/v1/ig2/gBz3YWv-488BFbwU9ave2R9KTss5Ady9YvespIa2kTEBjcKsX0Ngd8_--gZWl0Bnz0m6A2R8Sm7NaqmVXASkX5ov.jpg?quality=95&crop=6,0,934,525&as=32x18,48x27,72x40,108x61,160x90,240x135,360x202,480x270,540x304,640x360,720x405,934x525&from=bu&cs=240x135",
            isLiked = false
        ),
        onLikeClicked = {},
        onPostDeleteClicked = {},
        onToVkClicked = {},
        onToMihonClicked = {},
        onTextLongClick = {}
    )
}

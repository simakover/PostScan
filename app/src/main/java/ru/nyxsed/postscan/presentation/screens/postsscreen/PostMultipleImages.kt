package ru.nyxsed.postscan.presentation.screens.postsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.domain.models.ContentEntity
import ru.nyxsed.postscan.domain.models.PostEntity

@Composable
fun PostMultipleImages(
    post: PostEntity,
    onImageClicked: (Int) -> Unit,
) {

    when (post.content.size) {
        0 -> {
            return
        }

        2 -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ImageItem(modifier = Modifier.weight(1f), content = post.content[0], onImageClicked = { onImageClicked(0) })
                ImageItem(modifier = Modifier.weight(1f), content = post.content[1], onImageClicked = { onImageClicked(1) })
            }
        }

        3 -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ImageItem(modifier = Modifier.weight(1f), content = post.content[0], onImageClicked = { onImageClicked(0) })
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[1], onImageClicked = { onImageClicked(1) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[2], onImageClicked = { onImageClicked(2) })
                }
            }
        }

        4 -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[0], onImageClicked = { onImageClicked(0) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[1], onImageClicked = { onImageClicked(1) })
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[2], onImageClicked = { onImageClicked(2) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[3], onImageClicked = { onImageClicked(3) })
                }
            }
        }

        5 -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[0], onImageClicked = { onImageClicked(0) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[1], onImageClicked = { onImageClicked(1) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[2], onImageClicked = { onImageClicked(2) })
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[3], onImageClicked = { onImageClicked(3) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[4], onImageClicked = { onImageClicked(4) })
                }
            }
        }

        6 -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[0], onImageClicked = { onImageClicked(0) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[1], onImageClicked = { onImageClicked(1) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[2], onImageClicked = { onImageClicked(2) })
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[3], onImageClicked = { onImageClicked(3) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[4], onImageClicked = { onImageClicked(4) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[5], onImageClicked = { onImageClicked(5) })
                }
            }
        }

        7 -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[0], onImageClicked = { onImageClicked(0) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[1], onImageClicked = { onImageClicked(1) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[2], onImageClicked = { onImageClicked(2) })
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[3], onImageClicked = { onImageClicked(3) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[4], onImageClicked = { onImageClicked(4) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[5], onImageClicked = { onImageClicked(5) })
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[6], onImageClicked = { onImageClicked(6) })
                }
            }
        }

        8 -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[0], onImageClicked = { onImageClicked(0) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[1], onImageClicked = { onImageClicked(1) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[2], onImageClicked = { onImageClicked(2) })
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[3], onImageClicked = { onImageClicked(3) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[4], onImageClicked = { onImageClicked(4) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[5], onImageClicked = { onImageClicked(5) })
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[6], onImageClicked = { onImageClicked(6) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[7], onImageClicked = { onImageClicked(6) })
                }
            }
        }

        9 -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[0], onImageClicked = { onImageClicked(0) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[1], onImageClicked = { onImageClicked(1) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[2], onImageClicked = { onImageClicked(2) })
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[3], onImageClicked = { onImageClicked(3) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[4], onImageClicked = { onImageClicked(4) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[5], onImageClicked = { onImageClicked(5) })
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[6], onImageClicked = { onImageClicked(6) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[7], onImageClicked = { onImageClicked(6) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[8], onImageClicked = { onImageClicked(8) })
                }
            }
        }

        10 -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[0], onImageClicked = { onImageClicked(0) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[1], onImageClicked = { onImageClicked(1) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[2], onImageClicked = { onImageClicked(2) })
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[3], onImageClicked = { onImageClicked(3) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[4], onImageClicked = { onImageClicked(4) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[5], onImageClicked = { onImageClicked(5) })
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[6], onImageClicked = { onImageClicked(6) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[7], onImageClicked = { onImageClicked(6) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[8], onImageClicked = { onImageClicked(8) })
                    ImageItem(modifier = Modifier.weight(1f), content = post.content[9], onImageClicked = { onImageClicked(9) })
                }
            }
        }

        else -> {
            Row {
                ImageItem(modifier = Modifier.weight(1f), content = post.content[0], onImageClicked = { onImageClicked(0) })
            }

        }
    }
}

@Composable
fun ImageItem(
    modifier: Modifier,
    content: ContentEntity,
    onImageClicked: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(1.dp)
            .clickable(onClick = {
                onImageClicked()
            }),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = content.urlMedium,
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_placeholder),
            contentScale = ContentScale.Crop
        )
        if (content.type == "video") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.5f),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(150.dp)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize(),
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}
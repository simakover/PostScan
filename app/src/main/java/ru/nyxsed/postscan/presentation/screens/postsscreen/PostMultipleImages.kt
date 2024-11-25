package ru.nyxsed.postscan.presentation.screens.postsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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

@Composable
fun PostMultipleImages(
    images: List<String>,
    videos: List<String>,
    onImageClicked: (Int) -> Unit,
) {
    val contentImages: MutableList<ContentImage> = mutableListOf()

    images.forEach {
        contentImages.add(
            ContentImage(
                type = "photo",
                url = it
            )
        )
    }

    videos.forEach {
        contentImages.add(
            ContentImage(
                type = "video",
                url = it
            )
        )
    }

    when (contentImages.size) {
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
                ImageItem(contentImages = contentImages, index = 0, onImageClicked = onImageClicked)
                ImageItem(contentImages = contentImages, index = 1, onImageClicked = onImageClicked)
            }
        }

        3 -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ImageItem(contentImages = contentImages, index = 0, onImageClicked = onImageClicked)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(contentImages = contentImages, index = 1, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 2, onImageClicked = onImageClicked)
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
                    ImageItem(contentImages = contentImages, index = 0, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 1, onImageClicked = onImageClicked)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(contentImages = contentImages, index = 2, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 3, onImageClicked = onImageClicked)
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
                    ImageItem(contentImages = contentImages, index = 0, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 1, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 2, onImageClicked = onImageClicked)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(contentImages = contentImages, index = 3, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 4, onImageClicked = onImageClicked)
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
                    ImageItem(contentImages = contentImages, index = 0, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 1, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 2, onImageClicked = onImageClicked)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(contentImages = contentImages, index = 3, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 4, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 5, onImageClicked = onImageClicked)
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
                    ImageItem(contentImages = contentImages, index = 0, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 1, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 2, onImageClicked = onImageClicked)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(contentImages = contentImages, index = 3, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 4, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 5, onImageClicked = onImageClicked)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(contentImages = contentImages, index = 6, onImageClicked = onImageClicked)
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
                    ImageItem(contentImages = contentImages, index = 0, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 1, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 2, onImageClicked = onImageClicked)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(contentImages = contentImages, index = 3, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 4, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 5, onImageClicked = onImageClicked)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(contentImages = contentImages, index = 6, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 7, onImageClicked = onImageClicked)
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
                    ImageItem(contentImages = contentImages, index = 0, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 1, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 2, onImageClicked = onImageClicked)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(contentImages = contentImages, index = 3, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 4, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 5, onImageClicked = onImageClicked)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(contentImages = contentImages, index = 6, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 7, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 8, onImageClicked = onImageClicked)
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
                    ImageItem(contentImages = contentImages, index = 0, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 1, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 2, onImageClicked = onImageClicked)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(contentImages = contentImages, index = 3, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 4, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 5, onImageClicked = onImageClicked)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(contentImages = contentImages, index = 6, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 7, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 8, onImageClicked = onImageClicked)
                    ImageItem(contentImages = contentImages, index = 9, onImageClicked = onImageClicked)
                }
            }
        }

        else -> {
            Row {
                ImageItem(contentImages = contentImages, index = 0, onImageClicked = onImageClicked)
            }

        }
    }
}

@Composable
fun RowScope.ImageItem(
    contentImages: List<ContentImage>,
    index: Int,
    onImageClicked: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(1.dp)
            .clickable(onClick = {
                onImageClicked(index)
            }),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = contentImages[index].url,
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_placeholder),
            contentScale = ContentScale.Crop
        )
        if (contentImages[index].type == "video") {
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

@Composable
fun ColumnScope.ImageItem(
    contentImages: List<ContentImage>,
    index: Int,
    onImageClicked: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(1.dp)
            .clickable(onClick = {
                onImageClicked(index)
            }),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = contentImages[index].url,
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_placeholder),
            contentScale = ContentScale.Crop
        )
        if (contentImages[index].type == "video") {
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

data class ContentImage(
    val type: String,
    val url: String,
)
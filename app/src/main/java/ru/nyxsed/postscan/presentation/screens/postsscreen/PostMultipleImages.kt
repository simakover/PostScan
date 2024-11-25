package ru.nyxsed.postscan.presentation.screens.postsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.nyxsed.postscan.R

@Composable
fun PostMultipleImages(
    images: List<String>,
) {
    when (images.size) {
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
                ImageItem(images = images, index = 0)
                ImageItem(images = images, index = 1)
            }
        }

        3 -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ImageItem(images = images, index = 0)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(images = images, index = 1)
                    ImageItem(images = images, index = 2)
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
                    ImageItem(images = images, index = 0)
                    ImageItem(images = images, index = 1)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(images = images, index = 2)
                    ImageItem(images = images, index = 3)
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
                    ImageItem(images = images, index = 0)
                    ImageItem(images = images, index = 1)
                    ImageItem(images = images, index = 2)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(images = images, index = 3)
                    ImageItem(images = images, index = 4)
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
                    ImageItem(images = images, index = 0)
                    ImageItem(images = images, index = 1)
                    ImageItem(images = images, index = 2)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(images = images, index = 3)
                    ImageItem(images = images, index = 4)
                    ImageItem(images = images, index = 5)
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
                    ImageItem(images = images, index = 0)
                    ImageItem(images = images, index = 1)
                    ImageItem(images = images, index = 2)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(images = images, index = 3)
                    ImageItem(images = images, index = 4)
                    ImageItem(images = images, index = 5)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(images = images, index = 6)
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
                    ImageItem(images = images, index = 0)
                    ImageItem(images = images, index = 1)
                    ImageItem(images = images, index = 2)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(images = images, index = 3)
                    ImageItem(images = images, index = 4)
                    ImageItem(images = images, index = 5)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(images = images, index = 6)
                    ImageItem(images = images, index = 7)
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
                    ImageItem(images = images, index = 0)
                    ImageItem(images = images, index = 1)
                    ImageItem(images = images, index = 2)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(images = images, index = 3)
                    ImageItem(images = images, index = 4)
                    ImageItem(images = images, index = 5)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(images = images, index = 6)
                    ImageItem(images = images, index = 7)
                    ImageItem(images = images, index = 8)
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
                    ImageItem(images = images, index = 0)
                    ImageItem(images = images, index = 1)
                    ImageItem(images = images, index = 2)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(images = images, index = 3)
                    ImageItem(images = images, index = 4)
                    ImageItem(images = images, index = 5)
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    ImageItem(images = images, index = 6)
                    ImageItem(images = images, index = 7)
                    ImageItem(images = images, index = 8)
                    ImageItem(images = images, index = 9)
                }
            }
        }

        else -> {
            Row {
                ImageItem(images = images, index = 0)
            }

        }
    }
}

@Composable
fun RowScope.ImageItem(
    images: List<String>,
    index: Int,
) {
    AsyncImage(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(1.dp),
        model = images[index],
        contentDescription = null,
        placeholder = painterResource(R.drawable.ic_placeholder),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ColumnScope.ImageItem(
    images: List<String>,
    index: Int,
) {
    AsyncImage(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(1.dp),
        model = images[index],
        contentDescription = null,
        placeholder = painterResource(R.drawable.ic_placeholder),
        contentScale = ContentScale.Crop
    )
}
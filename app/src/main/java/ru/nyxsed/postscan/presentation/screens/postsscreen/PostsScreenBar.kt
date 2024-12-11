package ru.nyxsed.postscan.presentation.screens.postsscreen

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.nyxsed.postscan.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreenBar(
    onRefreshClicked: () -> Unit,
    onNavToGroupsClicked: () -> Unit,
    onNavToSettingsClicked: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = "Post Scan",
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(
                onClick = onRefreshClicked
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_download),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                )
            }
            IconButton(
                onClick = onNavToGroupsClicked
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_groups),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                )
            }
            IconButton(
                onClick = onNavToSettingsClicked
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_settings),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                )
            }
        }
    )
}
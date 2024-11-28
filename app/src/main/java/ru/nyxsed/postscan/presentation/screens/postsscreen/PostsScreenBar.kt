package ru.nyxsed.postscan.presentation.screens.postsscreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreenBar(
    onRefreshClicked: () -> Unit,
    onNavToGroupsClicked: () -> Unit,
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
                    imageVector = Icons.Filled.Refresh,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                )
            }
            IconButton(
                onClick = onNavToGroupsClicked
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountBox,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                )
            }
        }
    )
}
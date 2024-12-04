package ru.nyxsed.postscan.presentation.screens.preferencesscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navDestination
import com.vk.api.sdk.VK
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.util.DataStoreInteraction.Companion.DELETE_AFTER_LIKE
import ru.nyxsed.postscan.util.DataStoreInteraction.Companion.NOT_LOAD_LIKED_POSTS
import ru.nyxsed.postscan.util.DataStoreInteraction.Companion.USE_MIHON

val PreferencesScreen by navDestination<Unit> {
    val preferencesViewModel = koinViewModel<PreferencesScreenViewModel>()

    var settingNotLoadLikedPosts by remember { mutableStateOf(false) }
    var settingUseMihon by remember { mutableStateOf(false) }
    var settingDeleteAfterLike by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        settingNotLoadLikedPosts = preferencesViewModel.getSettingBoolean(NOT_LOAD_LIKED_POSTS)
        settingUseMihon = preferencesViewModel.getSettingBoolean(USE_MIHON)
        settingDeleteAfterLike = preferencesViewModel.getSettingBoolean(DELETE_AFTER_LIKE)
    }

    Scaffold { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(4.dp)
        ) {
            SettingRow(
                label = stringResource(R.string.not_load_liked_posts),
                checked = settingNotLoadLikedPosts,
                onCheckChange = {
                    preferencesViewModel.saveSettingBoolean(NOT_LOAD_LIKED_POSTS, it)
                    settingNotLoadLikedPosts = it
                }
            )
            SettingRow(
                label = stringResource(R.string.use_mihon_for_manga_search),
                checked = settingUseMihon,
                onCheckChange = {
                    preferencesViewModel.saveSettingBoolean(USE_MIHON, it)
                    settingUseMihon = it
                }
            )
            SettingRow(
                label = stringResource(R.string.delete_post_after_liking),
                checked = settingDeleteAfterLike,
                onCheckChange = {
                    preferencesViewModel.saveSettingBoolean(DELETE_AFTER_LIKE, it)
                    settingDeleteAfterLike = it
                }
            )
            SettingButton(
                label = stringResource(R.string.vk_logout),
                onClick = {
                    VK.logout()
                }
            )
        }
    }
}

@Composable
fun SettingRow(
    label: String,
    checked: Boolean,
    onCheckChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            text = label
        )
        Checkbox(
            checked = checked,
            onCheckedChange = {
                onCheckChange(it)
            }
        )
    }
}

@Composable
fun SettingButton(
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onClick()
            }
        ) {
            Text(text = label)
        }
    }
}
package ru.nyxsed.postscan.presentation.screens.preferencesscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navDestination
import org.koin.androidx.compose.koinViewModel
import ru.nyxsed.postscan.util.Constants.NOT_LOAD_LIKED_POSTS
import ru.nyxsed.postscan.util.Constants.USE_MIHON

val PreferencesScreen by navDestination<Unit> {
    val preferencesViewModel = koinViewModel<PreferencesScreenViewModel>()

    var settingNotLoadLikedPosts by remember { mutableStateOf(false) }
    var settingUseMihon by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        settingNotLoadLikedPosts = preferencesViewModel.getSettingBoolean(NOT_LOAD_LIKED_POSTS)
        settingUseMihon = preferencesViewModel.getSettingBoolean(USE_MIHON)
    }

    Scaffold { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(4.dp)
        ) {
            SettingRow(
                label = "Not Load Liked Posts",
                checked = settingNotLoadLikedPosts,
                onCheckChange = {
                    preferencesViewModel.saveSettingBoolean(NOT_LOAD_LIKED_POSTS, it)
                    settingNotLoadLikedPosts = it
                }
            )
            SettingRow(
                label = "Use Mihon for Manga Search",
                checked = settingUseMihon,
                onCheckChange = {
                    preferencesViewModel.saveSettingBoolean(USE_MIHON, it)
                    settingUseMihon = it
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
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            text = label
        )
        Checkbox(
            modifier = Modifier
                .padding(4.dp),
            checked = checked,
            onCheckedChange = {
                onCheckChange(it)
            }
        )
    }
}
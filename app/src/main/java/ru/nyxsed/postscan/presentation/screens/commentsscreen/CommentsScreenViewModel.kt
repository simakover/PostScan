package ru.nyxsed.postscan.presentation.screens.commentsscreen

import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import ru.nyxsed.postscan.data.models.entity.PostEntity
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.util.Constants.MANGA_SEARCH_ACTION
import ru.nyxsed.postscan.util.Constants.getSettingFromDataStore

class CommentsScreenViewModel(
    private val vkRepository: VkRepository,
    private val post: PostEntity,
    private val dataStore: DataStore<Preferences>,
): ViewModel() {
    val comments = vkRepository.getCommentsStateFlow(post = post)

    fun mihonIntent(query: String): Intent {
        return Intent().apply {
            action = MANGA_SEARCH_ACTION
            putExtra("query", query)
        }
    }

    suspend fun getSettingBoolean(key: String): Boolean {
        return getSettingFromDataStore(dataStore, key) == "1"
    }
}
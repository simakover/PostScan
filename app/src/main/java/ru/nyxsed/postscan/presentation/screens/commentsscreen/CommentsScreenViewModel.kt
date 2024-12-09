package ru.nyxsed.postscan.presentation.screens.commentsscreen

import android.content.Intent
import androidx.lifecycle.ViewModel
import ru.nyxsed.postscan.data.models.entity.PostEntity
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.util.Constants.MANGA_SEARCH_ACTION
import ru.nyxsed.postscan.util.DataStoreInteraction

class CommentsScreenViewModel(
    private val vkRepository: VkRepository,
    private val post: PostEntity,
    private val dataStoreInteraction: DataStoreInteraction,
) : ViewModel() {
    val comments = vkRepository.getCommentsStateFlow(post = post)

    fun mihonIntent(query: String): Intent {
        return Intent().apply {
            action = MANGA_SEARCH_ACTION
            val cleanedText = query
                .replace(Regex("[\\n\\r]"), "") // Удаляем переносы строк
                .replace(Regex("[\\p{So}\\p{Sk}\\uD83C-\\uDBFF\\uDC00-\\uDFFF]"), "") // Удаляем смайлы
            putExtra("query", cleanedText)
        }
    }

    suspend fun getSettingBoolean(key: String): Boolean {
        return dataStoreInteraction.getSettingBooleanFromDataStore(key)
    }
}
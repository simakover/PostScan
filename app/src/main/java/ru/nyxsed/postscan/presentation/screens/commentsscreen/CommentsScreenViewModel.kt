package ru.nyxsed.postscan.presentation.screens.commentsscreen

import android.content.Intent
import androidx.lifecycle.ViewModel
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.util.Constants.MANGA_SEARCH_ACTION

class CommentsScreenViewModel(
    private val vkRepository: VkRepository,
    private val post: PostEntity
): ViewModel() {
    val comments = vkRepository.getCommentsStateFlow(post = post)

    fun mihonIntent(query: String): Intent {
        return Intent().apply {
            action = MANGA_SEARCH_ACTION
            putExtra("query", query)
        }
    }
}
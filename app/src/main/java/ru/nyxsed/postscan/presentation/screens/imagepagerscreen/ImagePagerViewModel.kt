package ru.nyxsed.postscan.presentation.screens.imagepagerscreen

import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.domain.models.ContentEntity
import ru.nyxsed.postscan.util.Constants.VK_PHOTO_URL
import ru.nyxsed.postscan.util.Constants.YANDEX_SEARCH_URL

class ImagePagerViewModel(
    private val vkRepository: VkRepository,
) : ViewModel() {

    fun changeLikeStatus(contentEntity: ContentEntity) {
        viewModelScope.launch {
            vkRepository.changeLikeStatus(contentEntity)
        }
    }

    suspend fun checkLikeStatus(contentEntity: ContentEntity): Boolean {
        return vkRepository.checkLikeStatus(contentEntity)
    }

    fun openPostUri(uriHandler: UriHandler, contentEntity: ContentEntity) {
        uriHandler.openUri("${VK_PHOTO_URL}${contentEntity.ownerId}_${contentEntity.contentId}")
    }

    fun findYandexImage(uriHandler: UriHandler, link: String) {
        val endLink = java.net.URLEncoder.encode(link, "utf-8")
        uriHandler.openUri("$YANDEX_SEARCH_URL$endLink")
    }
}
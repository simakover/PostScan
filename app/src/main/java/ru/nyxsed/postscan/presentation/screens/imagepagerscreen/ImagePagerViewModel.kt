package ru.nyxsed.postscan.presentation.screens.imagepagerscreen

import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.data.models.entity.ContentEntity
import ru.nyxsed.postscan.data.repository.VkRepository
import ru.nyxsed.postscan.util.ConnectionChecker
import ru.nyxsed.postscan.util.Constants.VK_PHOTO_URL
import ru.nyxsed.postscan.util.Constants.YANDEX_SEARCH_URL
import ru.nyxsed.postscan.util.UiEvent

class ImagePagerViewModel(
    private val vkRepository: VkRepository,
    private val connectionChecker: ConnectionChecker,
) : ViewModel() {
    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

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

    suspend fun checkConnect(): Boolean {
        if (!connectionChecker.isInternetAvailable()) {
            _uiEventFlow.emit(UiEvent.ShowToast(R.string.no_internet_connection))
            return false
        }

        if (!connectionChecker.isTokenValid()) {
            _uiEventFlow.emit(UiEvent.ShowToast(R.string.token_is_invalid))
            return false
        }
        return true
    }
}
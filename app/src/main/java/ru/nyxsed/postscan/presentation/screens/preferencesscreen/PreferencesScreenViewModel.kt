package ru.nyxsed.postscan.presentation.screens.preferencesscreen

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.api.sdk.VK
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.util.DataStoreInteraction
import ru.nyxsed.postscan.util.DataStoreInteraction.Companion.DELETE_AFTER_LIKE
import ru.nyxsed.postscan.util.DataStoreInteraction.Companion.NOT_LOAD_LIKED_POSTS
import ru.nyxsed.postscan.util.DataStoreInteraction.Companion.USE_MIHON
import ru.nyxsed.postscan.util.UiEvent

class PreferencesScreenViewModel(
    private val dataStoreInteraction: DataStoreInteraction,
    private val resources: Resources,
) : ViewModel() {
    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    private var _settingNotLoadLikedPosts = MutableStateFlow<Boolean>(false)
    val settingNotLoadLikedPosts: StateFlow<Boolean> = _settingNotLoadLikedPosts.asStateFlow()

    private var _settingUseMihon = MutableStateFlow<Boolean>(false)
    val settingUseMihon: StateFlow<Boolean> = _settingUseMihon.asStateFlow()

    private var _settingDeleteAfterLike = MutableStateFlow<Boolean>(false)
    val settingDeleteAfterLike: StateFlow<Boolean> = _settingDeleteAfterLike.asStateFlow()

    fun saveSettingBoolean(key: String, value: Boolean) {
        viewModelScope.launch {
            dataStoreInteraction.saveSettingBooleanToDataStore(key, value)
            when (key) {
                NOT_LOAD_LIKED_POSTS -> _settingNotLoadLikedPosts.value = !_settingNotLoadLikedPosts.value
                USE_MIHON -> _settingUseMihon.value = !_settingUseMihon.value
                DELETE_AFTER_LIKE -> _settingDeleteAfterLike.value = !_settingDeleteAfterLike.value
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            VK.logout()
            _uiEventFlow.emit(UiEvent.ShowToast(resources.getString(R.string.log_out_message)))
        }
    }

    fun loadSettings() {
        viewModelScope.launch {
            _settingNotLoadLikedPosts.value = dataStoreInteraction.getSettingBooleanFromDataStore(NOT_LOAD_LIKED_POSTS)
            _settingUseMihon.value = dataStoreInteraction.getSettingBooleanFromDataStore(USE_MIHON)
            _settingDeleteAfterLike.value = dataStoreInteraction.getSettingBooleanFromDataStore(DELETE_AFTER_LIKE)
        }
    }
}
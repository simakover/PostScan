package ru.nyxsed.postscan.presentation.screens.preferencesscreen

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.api.sdk.VK
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.util.DataStoreInteraction
import ru.nyxsed.postscan.util.UiEvent

class PreferencesScreenViewModel(
    private val dataStoreInteraction: DataStoreInteraction,
    private val resources: Resources,
) : ViewModel() {
    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    fun saveSettingBoolean(key: String, value: Boolean) {
        viewModelScope.launch {
            dataStoreInteraction.saveSettingBooleanToDataStore(key, value)
        }
    }

    suspend fun getSettingBoolean(key: String): Boolean {
        return dataStoreInteraction.getSettingBooleanFromDataStore(key)
    }

    fun logOut() {
        viewModelScope.launch {
            VK.logout()
            _uiEventFlow.emit(UiEvent.ShowToast(resources.getString(R.string.log_out_message)))
        }
    }
}
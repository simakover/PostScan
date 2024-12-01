package ru.nyxsed.postscan.presentation.screens.preferencesscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.util.DataStoreInteraction

class PreferencesScreenViewModel(
    private val dataStoreInteraction: DataStoreInteraction,
) : ViewModel() {

    fun saveSettingBoolean(key: String, value: Boolean) {
        viewModelScope.launch {
            dataStoreInteraction.saveSettingBooleanToDataStore(key, value)
        }
    }

    suspend fun getSettingBoolean(key: String): Boolean {
        return dataStoreInteraction.getSettingBooleanFromDataStore(key)
    }
}
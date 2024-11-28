package ru.nyxsed.postscan.presentation.screens.preferencesscreen

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nyxsed.postscan.util.Constants.getSettingFromDataStore
import ru.nyxsed.postscan.util.Constants.saveSettingToDataStore

class PreferencesScreenViewModel(
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {

    fun saveSettingBoolean(key: String, value: Boolean) {
        viewModelScope.launch {
            val stringValue = if (value) "1" else "0"
            saveSettingToDataStore(dataStore, key, stringValue)
        }
    }

    suspend fun getSettingBoolean(key: String): Boolean {
        return getSettingFromDataStore(dataStore, key) == "1"
    }
}
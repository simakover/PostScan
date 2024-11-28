package ru.nyxsed.postscan.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date

object Constants {
    const val VK_API_VERSION = "5.199"
    const val VK_BASE_URL = "https://api.vk.com/method/"
    const val VK_WALL_URL = "https://vk.com/wall"
    const val VK_PHOTO_URL = "https://vk.com/photo"

    const val MANGA_SEARCH_ACTION = "eu.kanade.tachiyomi.SEARCH"

    const val YANDEX_SEARCH_URL = "https://yandex.ru/images/search?rpt=imageview&url="

    const val DATE_MASK = "##.##.####"
    const val DATE_LENGTH = 8 // Equals to "##.##.####".count { it == '#' }

    const val PROGRESS_CHANNEL_ID = "progress_channel"
    const val PROGRESS_CHANNEL_NAME = "Progress Notifications"
    const val PROGRESS_NOTIFICATION_ID = 1

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.format(date)
    }

    // Функция для проверки подключения
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun <T> List<T>.findOrFirst(predicate: (T) -> Boolean): T =
        find(predicate) ?: first()

    fun <T> List<T>.findOrLast(predicate: (T) -> Boolean): T =
        find(predicate) ?: last()


    const val NOT_LOAD_LIKED_POSTS = "NOT_LOAD_LIKED_POSTS"
    const val USE_MIHON = "USE_MIHON"

    // Функция для сохранения настроек
    suspend fun saveSettingToDataStore(dataStore: DataStore<Preferences>, key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    // Функция для чтения настроек
    suspend fun getSettingFromDataStore(dataStore: DataStore<Preferences>, key: String): String {
        val preferences = dataStore.data.first()
        return preferences[stringPreferencesKey(key)] ?: "default_value"
    }
}
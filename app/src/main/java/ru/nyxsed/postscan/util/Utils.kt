package ru.nyxsed.postscan.util

import java.text.SimpleDateFormat
import java.util.Date

object Constants {
    const val VK_API_VERSION = "5.199"
    const val VK_BASE_URL = "https://api.vk.com/method/"
    const val DATE_MASK = "##.##.####"
    const val DATE_LENGTH = 8 // Equals to "##.##.####".count { it == '#' }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy")
        return format.format(date)
    }
}
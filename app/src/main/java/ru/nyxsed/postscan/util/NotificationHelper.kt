package ru.nyxsed.postscan.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import ru.nyxsed.postscan.R

object NotificationHelper {
    private const val CHANNEL_ID = "progress_channel"
    private const val NOTIFICATION_ID = 1
    private var builder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManagerCompat? = null

    // Инициализация уведомления
    fun initNotification(context: Context) {
        createNotificationChannel(context)
        notificationManager = NotificationManagerCompat.from(context)
        builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.loading_posts))
            .setContentText(context.getString(R.string.loading_progress))
            .setSmallIcon(android.R.drawable.ic_menu_upload)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setProgress(100, 0, false)

        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        )
        notificationManager?.notify(NOTIFICATION_ID, builder!!.build())
    }

    // Обновление прогресса
    fun updateProgress(context: Context, progress: Int) {
        builder?.setProgress(100, progress, false)

        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        )
        notificationManager?.notify(NOTIFICATION_ID, builder!!.build())
    }

    // Завершение уведомления
    fun completeNotification(context: Context) {
        builder?.setContentText(context.getString(R.string.loading_completed))
            ?.setProgress(0, 0, false)

        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        )
        notificationManager?.notify(NOTIFICATION_ID, builder!!.build())
    }

    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Progress Notifications",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}

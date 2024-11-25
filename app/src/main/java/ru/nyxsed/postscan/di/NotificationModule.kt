package ru.nyxsed.postscan.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import org.koin.dsl.module
import ru.nyxsed.postscan.R

val notificationModule = module {
    single<NotificationManager> { (context: Context, channelId: String, channelName: String) ->
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager
    }

    // Регистрация NotificationCompat.Builder
    factory { (context: Context, channelId: String) ->
        NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_placeholder)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(context.getString(R.string.loading_posts))
            .setContentText(context.getString(R.string.loading_progress))
            .setProgress(100, 0, false)
    }
}
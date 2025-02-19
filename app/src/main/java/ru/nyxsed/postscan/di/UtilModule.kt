package ru.nyxsed.postscan.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import androidx.core.app.NotificationCompat
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.nyxsed.postscan.R
import ru.nyxsed.postscan.util.ConnectionChecker

val utilModule = module {
    single<NotificationManager> { (context: Context, channelId: String, channelName: String) ->
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            enableVibration(true)
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager
    }

    factory { (context: Context, channelId: String) ->
        NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_placeholder)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(context.getString(R.string.loading_posts))
            .setContentText(context.getString(R.string.loading_progress))
            .setProgress(100, 0, false)
    }

    single<ConnectionChecker> {
        ConnectionChecker(
            context = get(),
            storage = get(),
        )
    }

    single<Resources> {
        androidContext().resources
    }
}
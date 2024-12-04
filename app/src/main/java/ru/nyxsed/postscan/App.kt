package ru.nyxsed.postscan

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.nyxsed.postscan.di.appModule
import ru.nyxsed.postscan.di.dbModule
import ru.nyxsed.postscan.di.networkModule
import ru.nyxsed.postscan.di.utilModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule, dbModule, networkModule, utilModule)
        }
    }
}
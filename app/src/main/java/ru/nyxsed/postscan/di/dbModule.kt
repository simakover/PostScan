package ru.nyxsed.postscan.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import org.koin.dsl.module
import ru.nyxsed.postscan.data.database.AppDatabase
import ru.nyxsed.postscan.data.mapper.VkMapper

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val dbModule = module {
    // DB dependencies
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory {
        get<AppDatabase>().DbDao()
    }

    single {
        VkMapper()
    }


    // Datastore
    single<DataStore<Preferences>> { get<Context>().dataStore }
}
package ru.nyxsed.postscan.di

import androidx.room.Room
import org.koin.dsl.module
import ru.nyxsed.postscan.data.database.AppDatabase
import ru.nyxsed.postscan.data.repository.DbRepositoryImpl
import ru.nyxsed.postscan.data.repository.VkRepositoryImpl
import ru.nyxsed.postscan.domain.repository.DbRepository
import ru.nyxsed.postscan.domain.repository.VkRepository

val appModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory {
        get<AppDatabase>().DbDao()
    }

    single<VkRepository> {
        VkRepositoryImpl()
    }

    single<DbRepository> {
        DbRepositoryImpl(get())
    }
}
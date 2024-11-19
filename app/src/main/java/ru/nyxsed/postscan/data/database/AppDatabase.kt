package ru.nyxsed.postscan.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.domain.models.PostEntity

@Database(entities = [PostEntity::class,GroupEntity::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun DbDao(): DbDao
}
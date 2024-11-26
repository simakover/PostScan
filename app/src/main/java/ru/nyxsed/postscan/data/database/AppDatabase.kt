package ru.nyxsed.postscan.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.domain.models.PostEntity

@Database(entities = [PostEntity::class,GroupEntity::class], version = 6)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun DbDao(): DbDao
}
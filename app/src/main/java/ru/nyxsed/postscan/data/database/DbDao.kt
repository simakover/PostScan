package ru.nyxsed.postscan.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.nyxsed.postscan.domain.models.PostEntity

@Dao
interface DbDao {

    @Insert
    suspend fun insert(postEntity: PostEntity)

    @Query("SELECT * FROM posts")
    suspend fun getAllPosts(): List<PostEntity>
}
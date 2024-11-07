package ru.nyxsed.postscan.data.repository

import ru.nyxsed.postscan.data.database.DbDao
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.domain.repository.DbRepository

class DbRepositoryImpl(
    private val dbDao: DbDao,
) : DbRepository {
    override suspend fun getAllPosts(): List<PostEntity> {
        return dbDao.getAllPosts()
    }

    override suspend fun addPost(postEntity: PostEntity) {
        dbDao.insert(postEntity)
    }
}
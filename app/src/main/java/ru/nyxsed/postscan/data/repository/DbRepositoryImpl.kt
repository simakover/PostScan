package ru.nyxsed.postscan.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.nyxsed.postscan.data.database.DbDao
import ru.nyxsed.postscan.domain.models.PostEntity
import ru.nyxsed.postscan.domain.repository.DbRepository

class DbRepositoryImpl(
    private val dbDao: DbDao,
) : DbRepository {

    val scope = CoroutineScope(Dispatchers.Default)

    override fun getAllPosts(): StateFlow<List<PostEntity>> =
        dbDao.getAllPosts()
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = listOf()
            )

    override suspend fun addPost(postEntity: PostEntity) {
        dbDao.insert(postEntity)
    }
}
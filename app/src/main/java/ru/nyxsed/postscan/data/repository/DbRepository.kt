package ru.nyxsed.postscan.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.nyxsed.postscan.data.database.DbDao
import ru.nyxsed.postscan.domain.models.PostEntity

class DbRepository(
    private val dbDao: DbDao,
) {

    val scope = CoroutineScope(Dispatchers.Default)

    fun getAllPosts(): StateFlow<List<PostEntity>> =
        dbDao.getAllPosts()
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = listOf()
            )

    suspend fun addPost(postEntity: PostEntity) {
        dbDao.insert(postEntity)
    }
}
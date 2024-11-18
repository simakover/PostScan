package ru.nyxsed.postscan.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.nyxsed.postscan.data.database.DbDao
import ru.nyxsed.postscan.domain.models.GroupEntity
import ru.nyxsed.postscan.domain.models.PostEntity

class DbRepository(
    private val dbDao: DbDao,
) {

    val scope = CoroutineScope(Dispatchers.Default)

    // posts
    fun getAllPosts(): StateFlow<List<PostEntity>> =
        dbDao.getAllPosts()
            .stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = listOf()
            )

    suspend fun addPost(post: PostEntity) {
        dbDao.insertPost(post)
    }

    suspend fun deletePost(post: PostEntity) {
        dbDao.deletePost(post)
    }

    // groups
    fun getAllGroups(): StateFlow<List<GroupEntity>> =
        dbDao.getAllGroups()
            .stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = listOf()
            )

    suspend fun addGroup(group: GroupEntity) {
        dbDao.insertGroup(group)
    }

    suspend fun deleteGroup(group: GroupEntity) {
        dbDao.deleteGroup(group)
    }

    suspend fun updateGroup(group: GroupEntity) {
        dbDao.updateGroup(group)
    }
}
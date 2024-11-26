package ru.nyxsed.postscan.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    var groupId: Long? = null,
    var name: String = "",
    var screenName: String = "",
    val avatarUrl: String = "",
    var lastFetchDate: Long = System.currentTimeMillis(),
)

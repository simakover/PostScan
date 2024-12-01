package ru.nyxsed.postscan.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    var groupId: Long,
    var name: String,
    var screenName: String,
    val avatarUrl: String,
    var lastFetchDate: Long,
)

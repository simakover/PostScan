package ru.nyxsed.postscan.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val groupId: String = "-2355707",
    val lastFetchDate: String = "1730925001",
)

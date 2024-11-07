package ru.nyxsed.postscan.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val postId : Long = 1,
    val ownerId: Long = 1 ,
    val ownerName: String = "some name for a group",
    val ownerImageUrl: String = "https://sun9-51.userapi.com/s/v1/ig2/ox1k9csdJ9v6nJqhDGtwAb-jDcRcv2RtZlTe3emRHILprQveGVaYeMUxktUKTRi-Y-AP-odX9GzFGenjXTy4SEB2.jpg?quality=95&crop=135,0,530,530&as=32x32,48x48,72x72,108x108,160x160,240x240,360x360,480x480&ava=1&cs=50x50",
    val publicationDate: Long = 1730925069000,
    val contentText: String = "some content text for testing",
    val contentImageUrl: String? = "https://sun9-14.userapi.com/impg/F4ATtq58Pm5bpSPl6jlGv0EVK07vP3Z-dX8WCA/rGBB4skflLE.jpg?size=720x960&quality=96&sign=bff4cded0a5d3ed984985d28c443322d&type=album",
    val isLiked: Boolean = false,
)

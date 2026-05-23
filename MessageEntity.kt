package com.pentester.wcd.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "messages")
data class MessageEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val senderId: String,
    val receiverId: String?,
    val message: String? = null,
    val imageUrl: String? = null,
    val timestamp: Long,

    val isDeleted: Boolean = false,
    val isImage: Boolean = false,
    val isGroup: Boolean = false,
    val groupName: String? = null
)

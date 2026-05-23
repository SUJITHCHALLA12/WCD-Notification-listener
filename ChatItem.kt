package com.pentester.wcd.model

data class ChatItem(
    val id: String,
    val sender: String,
    val message: String,
    val timestamp: Long,
    val type: String,          // "incoming" | "outgoing" | "TEXT" | "IMAGE" | "VIDEO" | "FILE"
    val category: String = "chats", // "chats" | "channels" | "groups" | "calls"
    val isDeleted: Boolean = false,
    val isGroup: Boolean = false,
    val groupName: String? = null
)

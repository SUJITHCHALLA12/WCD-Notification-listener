package com.pentester.wcd.model

data class ChatThread(
    val sender: String,
    val lastMessage: String,
    val timestamp: Long,
    val lastMessageType: String
)

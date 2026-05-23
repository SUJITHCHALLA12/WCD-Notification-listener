package com.pentester.wcd.model

data class Message(
    val sender: String,
    val content: String,
    val timestamp: Long,
    val type: String
)

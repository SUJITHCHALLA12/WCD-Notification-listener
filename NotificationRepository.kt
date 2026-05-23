package com.pentester.wcd.data

import com.pentester.wcd.data.local.NotificationDao
import com.pentester.wcd.data.local.NotificationEntity
import com.pentester.wcd.model.ChatItem

class NotificationRepository(private val dao: NotificationDao) {

    val allNotifications = dao.getAllNotifications()
    val latestPerSender = dao.getLatestPerSender()

    fun getMessagesBySender(sender: String) = dao.getMessagesBySender(sender)

    suspend fun insert(item: ChatItem) {
        val notification = NotificationEntity(
            timestamp = item.timestamp,
            appName = item.type,
            title = item.sender,
            content = item.message,
            isDeleted = item.isDeleted,
            isGroup = item.isGroup,
            groupName = item.groupName,
            category = item.category
        )
        dao.insert(notification)
    }
}

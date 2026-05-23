package com.pentester.wcd

import android.app.Notification
import android.os.Build
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.pentester.wcd.data.local.NotificationEntity
import com.pentester.wcd.data.local.WcdDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WcdNotificationListener : NotificationListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName != "com.whatsapp" && sbn.packageName != "com.whatsapp.w4b") return

        val extras = sbn.notification.extras ?: return
        val title = extras.getString(Notification.EXTRA_TITLE) ?: return
        val notificationCategory = sbn.notification.category
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) sbn.notification.channelId else null
        val notifWhen = sbn.notification.`when`.takeIf { it > 0 } ?: System.currentTimeMillis()

        Log.d("WCD_LOG", "Notification from: ${sbn.packageName} | title: $title")

        scope.launch {
            val db = WcdDatabase.getDatabase(applicationContext)
            val dao = db.notificationDao()

            // Handle bundled notifications (multiple messages in one notification)
            val messages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                extras.getParcelableArray(Notification.EXTRA_MESSAGES)
            } else null

            if (messages != null && messages.isNotEmpty()) {
                for (msg in messages) {
                    val bundle = msg as? Bundle ?: continue
                    val sender = bundle.getString("sender") ?: title
                    val text = bundle.getString("text") ?: continue
                    val msgTimestamp = bundle.getLong("time").takeIf { it > 0 } ?: notifWhen

                    val isDeleted = isDeletedMessage(text)
                    val isGroup = title.contains("@") || isGroupNotification(extras)
                    val groupName = if (isGroup) title else null
                    val category = detectCategory(
                        title = sender,
                        content = text,
                        isGroup = isGroup,
                        notificationCategory = notificationCategory,
                        channelId = channelId,
                        conversationTitle = extras.getCharSequence(Notification.EXTRA_CONVERSATION_TITLE)?.toString(),
                        subText = extras.getString(Notification.EXTRA_SUB_TEXT)
                    )

                    val entity = NotificationEntity(
                        timestamp = msgTimestamp,
                        appName = detectType(text),
                        title = sender,
                        content = text,
                        isDeleted = isDeleted,
                        isGroup = isGroup,
                        groupName = groupName,
                        category = category
                    )
                    dao.insert(entity)
                    Log.d("WCD_LOG", "Saved bundled msg from: $sender | deleted: $isDeleted")
                }
            } else {
                // Single notification
                val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
                    ?: extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString()
                    ?: return@launch

                val isDeleted = isDeletedMessage(text)
                val isGroup = isGroupNotification(extras)
                val groupName = if (isGroup) title else null

                // Determine sender: for groups it's in subtext or conversationTitle
                val conversationTitle = extras.getCharSequence(Notification.EXTRA_CONVERSATION_TITLE)?.toString()
                val subText = extras.getString(Notification.EXTRA_SUB_TEXT)
                val sender = when {
                    isGroup -> subText ?: title
                    else -> title
                }
                val category = detectCategory(
                    title = sender,
                    content = text,
                    isGroup = isGroup,
                    notificationCategory = notificationCategory,
                    channelId = channelId,
                    conversationTitle = conversationTitle,
                    subText = subText
                )

                val entity = NotificationEntity(
                    timestamp = notifWhen,
                    appName = detectType(text),
                    title = sender,
                    content = text,
                    isDeleted = isDeleted,
                    isGroup = isGroup,
                    groupName = groupName ?: conversationTitle,
                    category = category
                )
                dao.insert(entity)
                Log.d("WCD_LOG", "Saved single msg from: $sender | deleted: $isDeleted")
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("WCD_LOG", "Notification removed: ${sbn.packageName}")
    }

    private fun isDeletedMessage(text: String): Boolean {
        val lower = text.lowercase()
        return lower.contains("this message was deleted") ||
            lower.contains("you deleted this message") ||
            lower.contains("message was deleted") ||
            lower == "null" ||
            text.isBlank()
    }

    private fun isGroupNotification(extras: Bundle): Boolean {
        val conversationTitle = extras.getCharSequence(Notification.EXTRA_CONVERSATION_TITLE)
        return conversationTitle != null
    }

    private fun detectType(text: String): String {
        val lower = text.lowercase()
        return when {
            lower.contains("video") -> "VIDEO"
            lower.contains("image") || lower.contains("photo") -> "IMAGE"
            lower.contains("pdf") || lower.contains("document") -> "FILE"
            lower.contains("sticker") || lower.contains("gif") -> "STICKER"
            lower.contains("audio") || lower.contains("voice") -> "AUDIO"
            lower.contains("liked") || lower.contains("reacted") -> "REACTION"
            else -> "TEXT"
        }
    }

    private fun detectCategory(
        title: String,
        content: String,
        isGroup: Boolean,
        notificationCategory: String?,
        channelId: String?,
        conversationTitle: String?,
        subText: String?
    ): String {
        val t = title.lowercase()
        val c = content.lowercase()
        val conv = conversationTitle?.lowercase() ?: ""
        val sub = subText?.lowercase() ?: ""
        val channel = channelId?.lowercase() ?: ""

        val isCall = notificationCategory == Notification.CATEGORY_CALL ||
            c.contains("call") || c.contains("video call") || c.contains("voice call") ||
            c.contains("audio call") || c.contains("missed call")
        if (isCall) return "calls"

        val isChannel = t.contains("channel") || conv.contains("channel") || sub.contains("channel") ||
            c.contains("channel") || c.contains("new post") || c.contains("posted") ||
            channel.contains("channel") || channel.contains("broadcast")
        if (isChannel) return "channels"

        if (isGroup || t.contains("group") || conv.contains("group") || t.contains("@")) return "groups"

        return "chats"
    }
}

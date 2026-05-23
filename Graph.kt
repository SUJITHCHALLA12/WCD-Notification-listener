package com.pentester.wcd

import android.content.Context
import com.pentester.wcd.data.NotificationRepository
import com.pentester.wcd.data.local.WcdDatabase
import com.pentester.wcd.data.repository.MessageRepository

object Graph {

    private var database: WcdDatabase? = null

    val notificationRepository: NotificationRepository by lazy {
        val db = requireDb()
        NotificationRepository(db.notificationDao())
    }

    val messageRepository: MessageRepository by lazy {
        val db = requireDb()
        MessageRepository(db.dao())
    }

    fun provide(context: Context) {
        if (database == null) {
            database = WcdDatabase.getDatabase(context)
        }
    }

    private fun requireDb(): WcdDatabase =
        database ?: throw IllegalStateException(
            "Graph has not been initialized. Call Graph.provide(context) in your Application class."
        )
}
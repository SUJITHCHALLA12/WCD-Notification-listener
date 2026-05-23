package com.pentester.wcd

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class WcdApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = getSystemService(NotificationManager::class.java) ?: return
        val channel = NotificationChannel(
            WcdNotificationIds.CHANNEL_ID,
            "Messages",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Incoming message notifications"
        }
        manager.createNotificationChannel(channel)
    }
}

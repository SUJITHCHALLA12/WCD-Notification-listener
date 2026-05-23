package com.pentester.wcd.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query(
        """
        SELECT * FROM notifications
        WHERE id IN (
            SELECT MAX(id) FROM notifications
            GROUP BY title
        )
        ORDER BY timestamp DESC
        """
    )
    fun getLatestPerSender(): Flow<List<NotificationEntity>>

    /**
     * Get all messages for a specific sender (title field).
     * Ordered oldest-first so the chat view shows correct chronological order.
     */
    @Query("SELECT * FROM notifications WHERE title = :sender ORDER BY timestamp ASC")
    fun getMessagesBySender(sender: String): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE isDeleted = 1 ORDER BY timestamp DESC")
    fun getDeletedMessages(): Flow<List<NotificationEntity>>
}
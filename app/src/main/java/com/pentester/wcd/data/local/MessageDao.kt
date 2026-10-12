package com.pentester.wcd.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface MessageDao {

    @Insert
    suspend fun insert(message: MessageEntity)

    @Query("SELECT senderId FROM messages GROUP BY senderId ORDER BY MAX(timestamp) DESC")
    fun getAllSenders(): Flow<List<String>>

    @Query("SELECT * FROM messages WHERE senderId = :senderId ORDER BY timestamp DESC")
    fun getMessagesBySender(senderId: String): Flow<List<MessageEntity>>

    @Query("""
        SELECT * FROM messages 
        WHERE senderId LIKE '%' || :query || '%'
        OR message LIKE '%' || :query || '%'
        ORDER BY timestamp DESC
    """)
    fun search(query: String): Flow<List<MessageEntity>>
}

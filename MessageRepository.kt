package com.pentester.wcd.data.repository

import com.pentester.wcd.data.local.MessageDao
import com.pentester.wcd.data.local.MessageEntity
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val dao: MessageDao) {

    suspend fun insert(message: MessageEntity) = dao.insert(message)

    fun getSenders(): Flow<List<String>> = dao.getAllSenders()

    fun getMessages(sender: String): Flow<List<MessageEntity>> =
        dao.getMessagesBySender(sender)

    fun search(query: String): Flow<List<MessageEntity>> = dao.search(query)
}
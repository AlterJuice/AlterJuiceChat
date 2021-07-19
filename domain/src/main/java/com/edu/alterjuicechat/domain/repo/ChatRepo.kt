package com.edu.alterjuicechat.domain.repo

import com.edu.alterjuicechat.domain.model.Message
import kotlinx.coroutines.flow.Flow


interface ChatRepo {
    suspend fun sendMessage(receiverID: String, message: String)
    fun getMessagesByID(userID: String): Flow<List<Message>>
    fun clearUnreadCounter(userID: String)

}



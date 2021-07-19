package com.edu.alterjuicechat.domain.repo

import com.edu.alterjuicechat.socket.dto.entities.MessageDto
import kotlinx.coroutines.flow.Flow


interface ChatRepo {
    suspend fun sendMessage(receiverID: String, message: String)
    fun getMessagesByID(userID: String): Flow<List<MessageDto>>
    fun clearUnreadCounter(userID: String)

}



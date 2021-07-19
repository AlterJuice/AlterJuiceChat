package com.edu.alterjuicechat.data.repo.chat

import com.edu.alterjuicechat.socket.dto.entities.MessageDto
import kotlinx.coroutines.flow.MutableStateFlow

interface ChatRepo {
    suspend fun sendMessage(receiverID: String, message: String)
    fun getMessagesByID(userID: String): MutableStateFlow<List<MessageDto>>
    fun clearUnreadCounter(userID: String)

}
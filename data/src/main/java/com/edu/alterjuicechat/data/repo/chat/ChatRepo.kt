package com.edu.alterjuicechat.data.repo.chat

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.socket.dto.entities.MessageDto

interface ChatRepo {
    suspend fun sendMessage(receiverID: String, message: String)
    fun getMessagesByID(userID: String): MutableLiveData<List<MessageDto>>
    fun clearUnreadCounter(userID: String)

}
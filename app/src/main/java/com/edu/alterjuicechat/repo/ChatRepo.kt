package com.edu.alterjuicechat.repo

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.ui.adapters.items.Message

interface ChatRepo {
    suspend fun sendMessage(sessionID: String, receiverID: String, message: String)
    fun getMessagesByID(userID: String): MutableLiveData<List<Message>>
    fun clearUnreadCounter(userID: String)

}
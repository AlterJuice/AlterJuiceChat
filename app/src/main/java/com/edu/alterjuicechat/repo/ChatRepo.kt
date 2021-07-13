package com.edu.alterjuicechat.repo

import androidx.lifecycle.MutableLiveData
import com.edu.alterjuicechat.data.network.model.dto.MessageDto
import com.edu.alterjuicechat.ui.adapters.items.Message

interface ChatRepo {

    suspend fun sendMessage(sessionID: String, receiverID: String, message: String)
    // suspend fun saveMessage(sess)

    // fun getMessages(): List<MessageDto>{
    //     return listOf()
    // }
    fun getMessagesByID(userID: String): MutableLiveData<List<Message>>
    fun clearUnreadCounter(userID: String)



}
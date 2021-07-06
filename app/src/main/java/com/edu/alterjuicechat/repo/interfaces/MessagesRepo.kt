package com.edu.alterjuicechat.repo.interfaces

import com.edu.alterjuicechat.data.network.model.dto.MessageDto

const val countMessages = 20


interface MessagesRepo {
    fun getMessages(): List<MessageDto>{
        return getMessages(0)
    }
    fun getMessages(offset: Int): List<MessageDto>
    fun saveMessage(id: String, text: String)

    fun sendMessage(messageId: String, receiver: String, message: String): MessageDto


}